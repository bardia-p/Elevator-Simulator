/**
 * 
 */
package ElevatorSimulator;

/**
 * @author Guy Morgenshtern
 * @author Sarah Chow
 *
 */
public class Floor implements Runnable {
	Scheduler messageQueue; 
	
	Floor(Scheduler messageQueue){
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
