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
	
	Elevator(Scheduler scheduler){
		this.scheduler = scheduler;
	}
	
	@Override
	public void run() {
		while(true) {
			RequestElevatorMessage reply = (RequestElevatorMessage) this.scheduler.receive();
			ArrivedElevatorMessage message = new ArrivedElevatorMessage(reply.getTimestamp(), reply.getDestination());
			this.scheduler.send(message);
		}
	}
}
