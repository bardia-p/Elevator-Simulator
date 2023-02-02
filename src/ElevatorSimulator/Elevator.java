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
	
	
	Elevator(Scheduler scheduler){
		this.scheduler = scheduler;
		this.shouldRun = true;
	}
	
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
