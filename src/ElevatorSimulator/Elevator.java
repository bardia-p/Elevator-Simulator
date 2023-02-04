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
	public Elevator(Scheduler scheduler){
		this.scheduler = scheduler;
		this.shouldRun = true;
	}
	
	/**
	 * Requests an update from the scheduler.
	 * 
	 * @return the update received from the scheduler.
	 */
	private Message requestUpdate() {
		return scheduler.receive(SenderType.ELEVATOR);
	}
	
	/**
	 * Processes the received message and to send a proper reply.
	 * 
	 * @param message the message to process.
	 */
	private void processMessage(Message message) {
		if (message.getType() == MessageType.KILL){
			kill();
		} else {
			RequestElevatorMessage request = (RequestElevatorMessage) message;
			moveTo(request.getTimestamp(), request.getDestination());
		}
	}
	
	/**
	 * Moves the elevator to the appropriate floor.
	 * 
	 * @param timestamp the timestamp for the event.
	 * @param floor the floor to move the elevator to.
	 */
	private void moveTo(String timestamp, int floor) {
		Message reply = new ArrivedElevatorMessage(timestamp, floor);
		scheduler.send(reply);
	}
	
	/**
	 * Kills the elevator subsystem.
	 */
	private void kill() {
		this.shouldRun = false;
	}

	
	/**
	 * The run method for the main logic of the elevator.
	 */
	@Override
	public void run() {
		while(this.shouldRun) {
			Message received = requestUpdate();
			
			processMessage(received);		
		}
	}
}
