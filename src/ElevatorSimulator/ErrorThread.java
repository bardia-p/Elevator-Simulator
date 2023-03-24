package ElevatorSimulator;

import ElevatorSimulator.Elevator.Elevator;
import ElevatorSimulator.Messages.ErrorType;

public class ErrorThread implements Runnable{
	private int time;
	private ErrorType errorType;
	private Elevator elevator;
	private Thread thread;

	public ErrorThread(int time, ErrorType error, Elevator elevator, Thread thread) {
		this.elevator = elevator;
		this.time = time;
		this.errorType = error;
		this.thread = thread;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
