/**
 * 
 */
package ElevatorSimulator;

/**
 * @author Andre Hazim
 * @author Bardia Parmoun
 * @author Guy Morgenshtern
 * @author Kyra Lothrop
 * @author Sarah Chow
 *
 */
public class Scheduler implements Runnable{
	// The queue used to receive messages from the elevator.
	private MessageQueue fromElevator;
	// The queue used to send messages to the elevator
	private MessageQueue toElevator;

	// The queue used to receive messages from the floor.
	private MessageQueue fromFloor;	
	// The queue used to send messages to the floor.
	private MessageQueue toFloor;
	
	/**
	 * Default constructor for the Scheduler.
	 * 
	 * Initializes all the appropriate queues.
	 */
	public Scheduler() {
		this.fromElevator = new MessageQueue();
		this.fromFloor = new MessageQueue();
		
		this.toElevator = new MessageQueue();
		this.toFloor = new MessageQueue();
	}
	
	/**
	 * Sends a message to the message queue.
	 * 
	 * @param m the message to send to the queue.
	 */
	public synchronized void send(Message m) {
		// Finds which queue to include the message to.
		MessageQueue activeQueue;
		if (m.sender == SenderType.ELEVATOR) {
			activeQueue = fromElevator;
		} else {
			activeQueue = fromFloor;
		}
		
		// Ensures the queue is empty.
		while(!activeQueue.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}
		
		activeQueue.tryPut(m);
		
		notifyAll();
	}
	
	/**
	 * Receives a message from the message queue.
	 * Decides the appropriate queue to choose from based on thread name.
	 *
	 * @return the message within the queue.
	 */
	public synchronized Message receive() {
		// Finds the appropriate the caller based on the thread name.
		SenderType caller;
		if (Thread.currentThread().getName().equals("ELEVATOR")) {
			caller = SenderType.ELEVATOR;
		} else if (Thread.currentThread().getName().equals("FLOOR")){
			caller = SenderType.FLOOR;
		} else {
			caller = null;
		}

		// Ensures the queue is empty.
		while ((toFloor.isEmpty() && caller == SenderType.FLOOR) || 
				(toElevator.isEmpty() && caller == SenderType.ELEVATOR)){
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}
		
		// Finds the reply message.
		Message reply;
		
		if (!toFloor.isEmpty() && caller == SenderType.FLOOR){
			reply = toFloor.tryGet();
		} else {
			reply = toElevator.tryGet();
		}
		
		notifyAll();
		
		return reply;
	}
	
	/**
	 * Polls its receive queues to see if a new message is out.
	 */
	private synchronized void poll() {
		// Polling both input queues.
		while (fromElevator.isEmpty() && fromFloor.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}
		
		// Receives the message from the appropriate queue.
		Message received;
		if (!fromElevator.isEmpty()) {
			received = fromElevator.tryGet();
		} else {
			received = fromFloor.tryGet();
		}
		
		/*Message reply = received;
		
		switch(received.type) {
		case REQUEST_ELEVATOR, ADD_STOP:
			reply.type = MessageType.MOVE_TO;
			break;
		case READY_TO_MOVE:
			reply.type = MessageType.ARRIVED;
			break;
		default:
			break;
		}*/
		
		
		// Puts the message in the respective send queue.
		if (received.sender == SenderType.ELEVATOR) {
			toFloor.tryPut(received);
		} else {
			toElevator.tryPut(received);
		}
		
		notifyAll();
	}
	
	@Override
	public void run() {
		while(true) {
			this.poll();
		}
	}
	
}