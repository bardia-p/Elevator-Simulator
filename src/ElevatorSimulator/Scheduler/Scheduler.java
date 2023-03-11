package ElevatorSimulator.Scheduler;

import java.util.ArrayList;

import ElevatorSimulator.Logger;
import ElevatorSimulator.Elevator.ElevatorInfo;
import ElevatorSimulator.Elevator.ElevatorState;
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
	private MessageQueue queue;
	private SchedulerState state;
	private Message currentRequest;
	
	
	
	public static final int ELEVATOR_PORT = 23, FLOOR_PORT = 69;

	// Keeps track of whether the scheduler should keep running or not.
	private boolean shouldRun;

	/**
	 * Default constructor for the Scheduler.
	 * 
	 */
	public Scheduler() {
		this.queue = new MessageQueue();
		this.shouldRun = true;
		this.currentRequest = null;
		this.state = null;

		Thread floorThread = new Thread(new ServerRPC(queue, FLOOR_PORT), "FLOOR MESSAGE THREAD");
		Thread elevatorThread = new Thread(new ServerRPC(queue, ELEVATOR_PORT), "ELEVATOR MESSAGE THREAD");
		
		floorThread.start();
		elevatorThread.start();

		changeState(SchedulerState.POLL);
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
	private ArrayList<ElevatorInfo> getAvailableElevators(){
		ArrayList<ElevatorInfo> availableElevators = new ArrayList<>();

		for (ElevatorInfo e : queue.getElevatorInfos().values()) {
						
			if (e.getState() == ElevatorState.POLL) {
				
				//System.out.println(e.getElevatorId());
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

		// Tries to find an elevator in going the same direction.
		// Going up and elevator is below request floor OR
		// Going down and elevator is above request floor
		for (ElevatorInfo elevator : availableElevators) {
			if ((elevator.getState() == ElevatorState.POLL) &&
					
					(elevator.getDirection() == requestMessage.getDirection()) &&
					
					((elevator.getDirection() == DirectionType.UP
							&& elevator.getFloorNumber() <= requestMessage.getFloor()) ||

							(elevator.getDirection() == DirectionType.DOWN
									&& elevator.getFloorNumber() >= requestMessage.getFloor())))
			{
				possibleCandidates.add(elevator);
			}
		}

		if (possibleCandidates.size() != 0) {
			// return the one with the least number of passengers.
			return possibleCandidates.stream()
					.min((first, second) -> Integer.compare(first.getNumRequest(), second.getNumRequest())).get().getElevatorId();
		} 
		else {
			// tries to find an empty elevator.
			for (ElevatorInfo elevator : availableElevators) {
				if (elevator.getState() == ElevatorState.POLL && elevator.getNumRequest() == 0) {
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
		}

		// could not find an elevator.
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
				queue.replyToElevator(currentRequest, id);
			} else {
				return;
			}

		} else if (currentRequest.getType() == MessageType.DOORS_OPENED) {
			DoorOpenedMessage request = (DoorOpenedMessage) currentRequest;
			queue.replyToFloor(request);
		}
		
		changeState(SchedulerState.POLL);
	}

	/**
	 * The run method for the main logic of the scheduler.
	 */
	@Override
	public void run() {
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
		System.out.println("\nSCHEDULER STATE: --------- " + newState + " ---------");
		state = newState;
	}
	

	public void addToQueue(ElevatorInfo e) {
		this.queue.addElevator(e.getElevatorId(), e);

	/**
	 * Stops the scheduler thread from running.
	 */
	private void kill(KillMessage message) {
		this.shouldRun = false;

		for (ElevatorInfo info: queue.getElevatorInfos().values()) {
			queue.replyToElevator(message, info.getElevatorId());
		}

	}
	
	public static void main(String[] args) {
		Thread schedulerThread = new Thread(new Scheduler(), "SCHEDULER THREAD");
		schedulerThread.start();
	}
}