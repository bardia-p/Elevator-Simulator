package ElevatorSimulator;

import java.util.ArrayList;

import ElevatorSimulator.Messages.Message;
import ElevatorSimulator.Messages.SenderType;

public class MessageQueue {
	// All the new messages for the scheduler.
	private Buffer newMessages;
	
	// All the messages sent to the elevator.
	private ArrayList<Buffer> toElevator;

	// All the messages sent to the floor.
	private Buffer toFloor;
	
	public MessageQueue() {
		this.newMessages = new Buffer();
		this.toElevator = new ArrayList<Buffer>();
		this.toFloor = new Buffer();
	}
	
	public void addElevator() {
		this.toElevator.add(new Buffer());
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
	 * Send a reply and add it to the appropriate queue.
	 * 
	 * @param m the message to send to the subsystem.
	 * @param receiver the subsystem in charge of receiving the message.
	 */
	public void replyToFloor(Message m) {
		toFloor.put(m);
	}
	
	/**
	 * Sends a reply to the elevator 
	 * 
	 * @param m the message 
	 * @param id the elevator id
	 */
	public void replyToElevator(Message m, int id) {
		toElevator.get(id).put(m);
	}
	
	/**
	 * Receive message from floor 
	 * @return The message in the floor queue
	 */
	public Message receiveFromFloor() {
		
		return toFloor.get();
	}
	
	/**
	 * Receive message from elevator 
	 * @return The message in the elevator queue
	 */
	public Message receiveFromElevator(int id) {
		return toElevator.get(id).get();
	}
	
	
	/**
	 * Returns a message from the message queue.
	 * Used by the scheduler.
	 * 
	 * @return the latest message in the queue.
	 */
	public Message pop() {
		return newMessages.get();
	}
	
	/**
	 * Checks if the elevator has a request
	 * @param id the id of the elevator
	 * @return a boolean if the elevator has a request
	 */
	public boolean elevatorHasRequest(int id) {
		return !toElevator.get(id).isEmpty();
	}
}
