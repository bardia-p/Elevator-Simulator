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
	// All the new messages for the scheduler.
	private Buffer newMessages;
	
	// All the messages sent to the elevator.
	private Buffer toElevator;

	// All the messages sent to the floor.
	private Buffer toFloor;
	
	// Keeps track of whether the scheduler should keep running or not.
	private boolean shouldRun;
	
	/**
	 * Default constructor for the Scheduler.
	 * Initializes all the appropriate buffer.
	 * 
	 */
	public Scheduler() {
		this.newMessages = new Buffer();
		this.toElevator = new Buffer();
		this.toFloor = new Buffer();
		this.shouldRun = true;
	}
	
	/**
	 * Adds a message to the newMessages buffer.
	 * 
	 * @param m the message to send to the buffer.
	 */
	public void send(Message m) {		
		newMessages.put(m);
	}
	
	/**
	 * Receives a message from the message buffer.
	 * Decides the appropriate queue to choose from based on thread name.
	 *
	 * @param sender the thread requesting the message.
	 * 
	 * @return the message within the buffer.
	 */
	public Message receive(SenderType sender) {
		Message received;
		
		if (sender == SenderType.ELEVATOR) {
			received = toElevator.get();
		} else {
			received = toFloor.get();
		}
		
		return received;		
	}
	
	/**
	 * Polls its newMessages buffer to see if a new message is sent.
	 * 
	 * @return the new message.
	 */
	private Message checkForNewMessages() {
		return newMessages.get();		
	}
	
	/**
	 * Forwards the new message to the proper receiving buffer.
	 * 
	 * @param m the message to forward to the appropriate buffer.
	 */
	private void forwardMessage(Message m) {		
		if (m.getSender() == SenderType.ELEVATOR) {
			toFloor.put(m);
		} else {
			toElevator.put(m);;
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