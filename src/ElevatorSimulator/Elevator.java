/**
 * 
 */
package ElevatorSimulator;

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
			Message reply = this.scheduler.receive();
			Message message = new Message(SenderType.ELEVATOR, "<timestamp>", reply.floorNumber, "HI", MessageType.READY_TO_MOVE);
			this.scheduler.send(message);
		}
	}
}
