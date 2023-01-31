/**
 * 
 */
package ElevatorSimulator;

import java.util.ArrayList;

/**
 * @author Bardia Parmoun
 *
 */
public class MessageQueue {
	// Holds all the messages.
	private ArrayList<Message> queue;

	/**
	 * Default constructor for the queue.
	 */
	MessageQueue(){
		this.queue = new ArrayList<>();
	}

	/**
	 * Attempts to put a message in the message queue.
	 * 
	 * @param m the message to put in the queue.
	 * 
	 * @return true if the operation was successful.
	 */
	public synchronized boolean tryPut(Message m) {
		if (!queue.isEmpty()) {
			return false;
		}
		
		System.out.println(Thread.currentThread().getName() + " sent:\n" + m.getDescription() + "\n");
		queue.add(m);
				
		return true;
	}

	/**
	 * Attempts to remove a message from the message queue.
	 * 
	 * @return the message that was returned.
	 */
	public synchronized Message tryGet() {
		if (queue.isEmpty()) {
			return null;
		}
		
		Message newMessage = queue.remove(0);
		System.out.println(Thread.currentThread().getName() + " received:\n" + newMessage.getDescription() + "\n");
		
		return newMessage;
	}
	
	/**
	 * Checks whether the queue is empty or not.
	 * 
	 * @return true if empty false if not.
	 */
	public boolean isEmpty() {
		return queue.isEmpty();
	}
}
