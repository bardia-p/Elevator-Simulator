/**
 * 
 */
package ElevatorSimulator;

import ElevatorSimulator.Messages.*;

/**
 * @author Andre Hazim
 * @author Kyra Lothrop
 *
 */
public class Elevator implements Runnable {
	Scheduler scheduler; 
	private boolean shouldRun;
	
	
	public Elevator(Scheduler scheduler){
		this.scheduler = scheduler;
		this.shouldRun = true;
	}
	
	private Message requestUpdate() {
		return scheduler.receive(SenderType.ELEVATOR);
	}
	
	private void processMessage(Message message) {
		if (message.getType() == MessageType.KILL){
			kill();
		} else {
			RequestElevatorMessage request = (RequestElevatorMessage) message;
			moveTo(request.getDestination());
		}
	}
	
	private void moveTo(int floor) {
		Message reply = new ArrivedElevatorMessage("<TIMESTAMP>", floor);
		scheduler.send(reply);
	}
	
	private void kill() {
		this.shouldRun = false;
	}

	
	@Override
	public void run() {
		while(this.shouldRun) {
			Message received = requestUpdate();
			
			processMessage(received);		
		}
	}
}
