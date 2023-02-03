/**
 * 
 */
package ElevatorSimulator;

import ElevatorSimulator.Messages.*;

/**
 * The elevator
 * - Receives and replies to messages.
 * 
 * @author Andre Hazim
 * @author Kyra Lothrop
 *
 */
public class Elevator implements Runnable {
	// The scheduler.
	Scheduler scheduler; 
	
	// Check if the elevator is to continue running.
	private boolean shouldRun;
	
	/**
	 * Constructor for the elevator.
	 * 
	 * @param scheduler, the scheduler
	 */
	Elevator(Scheduler scheduler){
		this.scheduler = scheduler;
		this.shouldRun = true;
	}
	
	/**
	 * The run method for the main logic of the elevator.
	 */
	@Override
	public void run() {
		while(this.shouldRun) {
			Message received = this.scheduler.receive();
			
			Message reply;
			if (received.getType() == MessageType.KILL){
				this.shouldRun = false;
				reply = new KillMessage(SenderType.ELEVATOR, "No more floor requests remaining");
			} else {
				RequestElevatorMessage request = (RequestElevatorMessage) received;
				reply = new ArrivedElevatorMessage(request.getTimestamp(), request.getDestination());
			}
			
			scheduler.send(reply);
		}
	}
}
