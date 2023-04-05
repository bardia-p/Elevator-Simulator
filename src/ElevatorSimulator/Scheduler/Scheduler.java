package ElevatorSimulator.Scheduler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import ElevatorSimulator.Logger;
import ElevatorSimulator.Simulator;
import ElevatorSimulator.Elevator.ElevatorInfo;
import ElevatorSimulator.Elevator.ElevatorState;
import ElevatorSimulator.Elevator.ElevatorTrip;
import ElevatorSimulator.Messages.*;
import ElevatorSimulator.Messaging.MessageQueue;
import ElevatorSimulator.Messaging.ServerRPC;

/**
 * The scheduler subsystem: - Removes a message from the elevator and sends to
 * the floor. - Removes a message from the floor and sends to the elevator.
 * 
 * @author Andre Hazim
 * @author Bardia Parmoun
 *
 */
public class Scheduler implements Runnable {
	// Keeps track of the shared message queue.
	private MessageQueue queue;

	// Keeps track of the scheduler state.
	private SchedulerState state;

	// Keeps track of the current request for the scheduler.
	private Message currentRequest;
	
	// Keeps track of whether the scheduler should keep running or not.
	private boolean shouldRun;
	
	// Keeps track of whether or not the servers are started externally or not.
	private boolean initializeServers;
	
	// The scheduler ports used for UDP.
	public static final int ELEVATOR_PORT = 23;
	public static final int FLOOR_PORT = 69;
	public static final int UI_PORT = 4445;


	/**
	 * Default constructor for the Scheduler.
	 * 
	 */
	public Scheduler() {
		this(new MessageQueue(), true);
	}
	
	/**
	 * Scheduler constructor with the option of an external queue.
	 * 
	 * @param external message queue.
	 * 
	 */
	public Scheduler(MessageQueue queue, boolean initializeServers) {
		this.queue = queue;
		this.shouldRun = true;
		this.currentRequest = null;
		this.state = null;
		this.initializeServers = initializeServers;

		changeState(SchedulerState.POLL);
	}

	/**
	 * Initializes the server RPC threads.
	 */
	private void initializeServerRPCs() {
		Thread floorThread = new Thread(new ServerRPC(queue, FLOOR_PORT), "FLOOR MESSAGE THREAD");
		Thread elevatorThread = new Thread(new ServerRPC(queue, ELEVATOR_PORT), "ELEVATOR MESSAGE THREAD");
		Thread uiThread = new Thread(new ServerRPC(queue, UI_PORT), "UI MESSAGE THREAD");

		floorThread.start();
		elevatorThread.start();
		uiThread.start();
	}

	/**
	 * Polls its newMessages buffer to see if a new message is sent.
	 * 
	 * @return the new message.
	 */
	private Message checkForNewMessages() {
		return queue.pop();
	}

	/**
	 * Gets all the available elevators
	 * 
	 * @param message A request elevator message
	 * 
	 * @return A list of availble elevators
	 */
	public ArrayList<ElevatorInfo> getAvailableElevators() {
		ArrayList<ElevatorInfo> availableElevators = new ArrayList<>();

		for (ElevatorInfo e : queue.getElevatorInfos().values()) {

			if (e.getParentState() == ElevatorState.OPERATIONAL) {
				availableElevators.add(e);
			}

		}
		return availableElevators;
	}

	/**
	 * Checks to see the closest elevator and adds it to the list
	 * 
	 * @param requestMessage the request messsage
	 * @return the id of the closest elevator
	 */
	public int getClosestElevator(RequestElevatorMessage requestMessage) {
		ArrayList<ElevatorInfo> availableElevators = this.getAvailableElevators();
		ArrayList<ElevatorInfo> possibleCandidates = new ArrayList<>();

		if (availableElevators.size() == 0) {
			return -1;
		}
		
		// tries to find an empty elevator.
		for (ElevatorInfo elevator : availableElevators) {
			// The elevator has no requests and no pending trips.
			if (elevator.getNumRequest() == 0 && queue.numPendingElevatorRequest(elevator.getElevatorId()) == 0) {
				possibleCandidates.add(elevator);
			}
		}

		if (possibleCandidates.size() != 0) {
			// return the closest elevator.
			return possibleCandidates.stream()
					.min((first, second) -> Integer.compare(
							Math.abs(first.getFloorNumber() - requestMessage.getFloor()),
							Math.abs(second.getNumRequest() - requestMessage.getFloor())))
					.get().getElevatorId();
		}

		// Tries to find an elevator in going the same direction.
		// Going up and elevator is below request floor OR
		// Going down and elevator is above request floor
		for (ElevatorInfo elevator : availableElevators) {
			if ((elevator.getDirection() == requestMessage.getDirection()) &&

					((elevator.getDirection() == DirectionType.UP
							&& elevator.getFloorNumber() <= requestMessage.getFloor()) ||

							(elevator.getDirection() == DirectionType.DOWN
									&& elevator.getFloorNumber() >= requestMessage.getFloor()))) {
				possibleCandidates.add(elevator);
			}
		}

		if (possibleCandidates.size() != 0) {
			// return the one with the least number of passengers.
			return possibleCandidates.stream()
					.min((first, second) -> Integer.compare(first.getNumRequest(), second.getNumRequest())).get()
					.getElevatorId();
		}

	
		// Could not find a suitable elevator.
		return -1;
	}

	/**
	 * Method to process the message.
	 */
	private void processMessage() {

		if (currentRequest.getType() == MessageType.KILL) {
			kill((KillMessage) currentRequest);
		} else if (currentRequest.getType() == MessageType.REQUEST) {
			int id = this.getClosestElevator((RequestElevatorMessage) currentRequest);

			if (id != -1) {
				if (Simulator.DEBUG_MODE) {
					System.out.println("PICKED ELEVATOR " + id);
				}
				queue.replyToElevator(currentRequest, id);
			} else {
				// Send the message to the back of the queue.
				queue.send(currentRequest);
			}

		} else if (currentRequest.getType() == MessageType.ELEVATOR_STUCK) {
			ElevatorStuckMessage message = (ElevatorStuckMessage) currentRequest;
			rerouteTrips(message.getElevatorId(), message.getRemainingTrips());
		}

		changeState(SchedulerState.POLL);
	}

	/**
	 * Reroute all the trips assigned to that elevator and send them back to the scheduler.
	 * Reassigns the given trips back the scheduler.
	 * Also checks the outgoing queue for the elevator and removes the trips from there.
	 * 
	 * @param elevatorId, the elevator id.
	 * @param trips, the trips to reroute.
	 */
	private void rerouteTrips(int elevatorId, ArrayList<ElevatorTrip> trips) {
		// Send all the new requests back to the queue.
		for (ElevatorTrip trip : trips) {
			RequestElevatorMessage newRequest = new RequestElevatorMessage(null, trip.getPickup(),
					trip.getDirectionType(), trip.getDropoff(), trip.getTimeToFault(), trip.getFault());
			queue.send(newRequest);
		}
		
		// Check to queue to ensure no new messages were going to be sent to the elevator.
		// If there are any message send them back to the scheduler.
		boolean elevatorHasMessage = true;
		do {
			Message m = queue.receiveFromElevator(elevatorId);
			
			if (m.getType() == MessageType.EMPTY) {
				elevatorHasMessage = false;
			} else if (m.getType() == MessageType.REQUEST) {
				queue.send((RequestElevatorMessage)m);
			}
		} while (elevatorHasMessage);
	}

	/**
	 * The run method for the main logic of the scheduler.
	 */
	@Override
	public void run() {
		if (initializeServers) {
			initializeServerRPCs();
		}

		while (this.shouldRun) {
			if (state == SchedulerState.POLL) {
				currentRequest = checkForNewMessages();
				if (currentRequest != null && currentRequest.getType() != MessageType.EMPTY) {
					Logger.printMessage(currentRequest, "RECEIVED");
					changeState(SchedulerState.PROCESSING);
				}
			} else {
				processMessage();
			}

			// Avoids busy looping.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * change and print state
	 * 
	 * @param newState
	 */
	private void changeState(SchedulerState newState) {
		if (Simulator.DEBUG_MODE) {
			System.out.println("\nSCHEDULER STATE: --------- " + newState + " ---------");
		}
		state = newState;
	}

	/**
	 * Stops the scheduler thread from running.
	 */
	private void kill(KillMessage message) {
		this.shouldRun = false;

		for (ElevatorInfo info : queue.getElevatorInfos().values()) {
			queue.replyToElevator(message, info.getElevatorId());
		}

	}

	public static void main(String[] args) {
		if (Simulator.DEBUG_MODE) {
			try {
				System.out.println("The scheduler IP is: " + InetAddress.getLocalHost());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		Thread schedulerThread = new Thread(new Scheduler(), "SCHEDULER THREAD");
		schedulerThread.start();
	}
}