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
	Scheduler messageQueue; 
	
	Elevator(Scheduler messageQueue){
		this.messageQueue = messageQueue;
	}
	
	@Override
	public void run() {
		/* while true
			messageQueue.send()
			messageQueue.receive()
		*/
	}

}
