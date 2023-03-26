package ElevatorSimulator;

import ElevatorSimulator.Elevator.Elevator;
import ElevatorSimulator.Messages.ErrorType;

/**
 * The class in charge of generating an elevator for the elevator.
 * 
 * @author Guy Morgenshtern.
 * @author Bardia Parmoun
 *
 */
public class ErrorGenerator implements Runnable{
	// The time to generate the error.
	private int time;
	
	// Error type.
	private ErrorType errorType;

	// Elevator object.
	private Elevator elevator;
	
	// Elevator thread.
	private Thread thread;

	/**
	 * The constructor for the error generator class.
	 * 
	 * @param time
	 * @param error
	 * @param elevator
	 * @param thread
	 */
	public ErrorGenerator(int time, ErrorType error, Elevator elevator, Thread thread) {
		this.elevator = elevator;
		this.time = time;
		this.errorType = error;
		this.thread = thread;
	}
	
	/**
	 * Sleeps for the fault delay value and then interrupts the elevator thread.
	 */
	@Override
	public void run() {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		thread.interrupt();
		
		if (errorType == ErrorType.DOOR_INTERRUPT) {
			elevator.handleDoorFault();
		} else {
			elevator.handleElevatorStuck();
		}

	}

}
