package ElevatorSimulator;

import ElevatorSimulator.Messages.*;

/**
 * The scheduler subsystem:
 * - Removes a message from the elevator and sends to the floor.
 * - Removes a message from the floor and sends to the elevator.
 * 
 * @author Andre Hazim
 * @author Bardia Parmoun
 *
 */
public class Scheduler implements Runnable{
	private MessageQueue queue;
	
	// Keeps track of whether the scheduler should keep running or not.
	private boolean shouldRun;
	
	/**
	 * Default constructor for the Scheduler.
	 * 
	 */
	public Scheduler(MessageQueue queue) {
		this.queue = queue;
		this.shouldRun = true;
	}
	
	
	/**
	 * Polls its newMessages buffer to see if a new message is sent.
	 * 
	 * @return the new message.
	 */
	private Message checkForNewMessages() {
		return queue.pop();	
	}
	
	/**
	 * Forwards the new message to the proper receiving buffer.
	 * 
	 * @param m the message to forward to the appropriate buffer.
	 */
	private void forwardMessage(Message m) {		
		if (m.getSender() == SenderType.ELEVATOR) {
			queue.reply(m, SenderType.FLOOR);
		} else {
			queue.reply(m, SenderType.ELEVATOR);
		}
	}
	
	/**
	 * Stops the scheduler thread from running.
	 */
	private void kill() {
		this.shouldRun = false;
	}
	
	/**
	 * The run method for the main logic of the scheduler.
	 */
	@Override
	public void run() {
		while(this.shouldRun) {
			Message newMessage = checkForNewMessages();
			
			if (newMessage.getType() == MessageType.KILL){
				kill();
			}
			
			forwardMessage(newMessage);
		}
	}
	
}