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
	
	/**
	 * Default constructor for the Scheduler.
	 * Initializes all the appropriate buffer.
	 * 
	 */
	public Scheduler() {
		this.newMessages = new Buffer();
		this.toElevator = new Buffer();
		this.toFloor = new Buffer();
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
	 * @return the message within the buffer.
	 */
	public Message receive() {
		Message received;
		
		if (Thread.currentThread().getName().equals("ELEVATOR")) {
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
	 * The run method for the main logic of the scheduler.
	 */
	@Override
	public void run() {
		while(true) {
			Message newMessage = checkForNewMessages();
			forwardMessage(newMessage);
		}
	}
	
}