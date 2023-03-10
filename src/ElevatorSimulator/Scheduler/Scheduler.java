package ElevatorSimulator.Scheduler;

import java.util.ArrayList;

import ElevatorSimulator.Elevator.Elevator;
import ElevatorSimulator.Elevator.ElevatorController;
import ElevatorSimulator.Elevator.ElevatorState;
import ElevatorSimulator.Messages.*;
import ElevatorSimulator.Messaging.MessageQueue;

/**
 * The scheduler subsystem:
 * - Removes a message from the elevator and sends to the floor.
 * - Removes a message from the floor and sends to the elevator.
 * 
 * @author Andre Hazim
 * @author Bardia Parmoun
 *
 */
public class Scheduler implements Runnable{
	private MessageQueue queue;
	private SchedulerState state;
	private Message currentRequest;

	private ElevatorController elevatorController;


	// Keeps track of whether the scheduler should keep running or not.
	private boolean shouldRun;

	/**
	 * Default constructor for the Scheduler.
	 * 
	 */
	public Scheduler(MessageQueue queue, ElevatorController elevatorController) {
		this.queue = queue;
		this.shouldRun = true;
		this.currentRequest = null;
		this.state = null;
		this.elevatorController = elevatorController;

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
	 * Checks to see the closest elevator and adds it to the list 
	 * @param requestMessage the request messsage
	 * @return the id of the closest elevator 
	 */
	public int getClosestElevator(RequestElevatorMessage requestMessage) {
		ArrayList<Elevator> availableElevators = elevatorController.getAvailableElevators(requestMessage);

		if (availableElevators.size() == 0) {
			return -1;
		}
		for (Elevator elevator : availableElevators) {

			if (elevator.getState() == ElevatorState.POLL) {
				if (elevator.getDirection() == requestMessage.getDirection()) {

					if ((elevator.getDirection() == DirectionType.UP &&
							elevator.getFloorNumber() <= requestMessage.getFloor()) ||

							(elevator.getDirection() == DirectionType.DOWN &&
							elevator.getFloorNumber() >= requestMessage.getFloor())) {
						// Going up and elevator is below request floor OR
						// Going down and elevator is above request floor

						return elevator.getID();
					}
				}
				else if (elevator.getNumTrips() == 0) {
					return elevator.getID();
				}
			}
		}

		return -1;
	}

	/**
	 * processes the message
	 */
	private void processMessage() {

		if (currentRequest.getType() == MessageType.KILL){
			kill((KillMessage) currentRequest);
		}
		else if (currentRequest.getType() == MessageType.REQUEST) {
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
	 * Stops the scheduler thread from running.
	 */
	private void kill(KillMessage message) {
		this.shouldRun = false;
		elevatorController.kill(message);
	}

	/**
	 * The run method for the main logic of the scheduler.
	 */
	@Override
	public void run() {
		while(this.shouldRun) {
			if (state == SchedulerState.POLL) {
				currentRequest = checkForNewMessages();
				if (currentRequest != null) {
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
	 * @param newState
	 */
	private void changeState(SchedulerState newState) {
		System.out.println("\nSCHEDULER STATE: --------- " + newState + " ---------");
		state = newState;

	}

}