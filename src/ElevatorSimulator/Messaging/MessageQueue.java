package ElevatorSimulator.Messaging;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.sound.midi.Soundbank;

import ElevatorSimulator.Messages.Message;

/**
 * File representing the MessageQueue class.
 * @author Bardia Parmoun
 * @author Andre Hazim
 *
 */
public class MessageQueue {
	// All the new messages for the scheduler.
	private ConcurrentLinkedDeque<Message> newMessages;
	
	// All the messages sent to the elevator.
	private ArrayList<ConcurrentLinkedDeque<Message>> toElevator;

	// All the messages sent to the floor.
	private ConcurrentLinkedDeque<Message> toFloor;
	
	/**
	 * Constructor for the class. Initialzies
	 * the newMessages Buffer object, toElevator ArrayList,
	 * and the toFloor Buffer object.
	 */
	public MessageQueue() {
		this.newMessages = new ConcurrentLinkedDeque<Message>();
		this.toElevator = new ArrayList<ConcurrentLinkedDeque<Message>>();
		this.toFloor = new ConcurrentLinkedDeque<Message>();
	}
	
	/**
	 * Method to add a new Buffer object to the
	 * toElevator ArrayList.
	 */
	public void addElevator() {
		this.toElevator.add(new ConcurrentLinkedDeque<Message>());
	}
	
	/**
	 * Adds a message to the newMessages buffer.
	 * 
	 * @param m the message to send to the buffer.
	 */
	public void send(Message m) {
		
		newMessages.offer(m);
	}
	
	/**
	 * Send a reply and add it to the appropriate queue.
	 * 
	 * @param m the message to send to the subsystem.
	 * @param receiver the subsystem in charge of receiving the message.
	 */
	public void replyToFloor(Message m) {
		System.out.println("message to floor");
		toFloor.offer(m);
	}
	
	/**
	 * Sends a reply to the elevator 
	 * 
	 * @param m the message 
	 * @param id the elevator id
	 */
	public void replyToElevator(Message m, int id) {
		System.out.println("message to elevator");
		toElevator.get(id).offer(m);
	}
	
	/**
	 * Receive message from floor 
	 * @return The message in the floor queue
	 */
	public Message receiveFromFloor() {
		System.out.println("getting message for floor");
		return toFloor.poll();
	}
	
	/**
	 * Receive message from elevator 
	 * @return The message in the elevator queue
	 */
	public Message receiveFromElevator(int id) {
		System.out.println("getting message from elevator");
		return toElevator.get(id).poll();
	}
	
	
	/**
	 * Returns a message from the message queue.
	 * Used by the scheduler.
	 * 
	 * @return the latest message in the queue.
	 */
	public Message pop() {
		return newMessages.poll();
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
