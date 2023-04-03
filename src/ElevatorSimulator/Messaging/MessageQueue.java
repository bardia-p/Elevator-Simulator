package ElevatorSimulator.Messaging;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import ElevatorSimulator.Logger;
import ElevatorSimulator.Elevator.ElevatorInfo;
import ElevatorSimulator.Messages.EmptyMessage;
import ElevatorSimulator.Messages.Message;

/**
 * File representing the MessageQueue class.
 * 
 * @author Bardia Parmoun
 * @author Andre Hazim
 * @author Sarah Chow
 */
public class MessageQueue {
	// All the new messages for the scheduler.
	private ConcurrentLinkedDeque<Message> newMessages;
	
	// All the messages sent to the elevators.
	private HashMap<Integer, ConcurrentLinkedDeque<Message>> toElevator;

	// All the messages sent to the floor.
	private ConcurrentLinkedDeque<Message> toFloor;
	
	// All the messages sent to the floor.
	private ConcurrentLinkedDeque<Message> toUI;
	
	// Keeps track of all the elevator statuses.
	private HashMap<Integer, ElevatorInfo> elevatorInfos; 
	
	/**
	 * Constructor for the class. Initialzies
	 * the newMessages Buffer object, toElevator ArrayList,
	 * toFloor Buffer object, and the String messages.
	 */
	public MessageQueue() {
		this.newMessages = new ConcurrentLinkedDeque<Message>();
		this.toElevator = new HashMap<Integer, ConcurrentLinkedDeque<Message>>();
		this.toFloor = new ConcurrentLinkedDeque<Message>();
		this.elevatorInfos = new HashMap<>();
		this.toUI = new ConcurrentLinkedDeque<Message>();
	}
	
	/**
	 * Returns the elevator statuses.
	 * 
	 * @return
	 */
	public HashMap<Integer, ElevatorInfo> getElevatorInfos() {
		return this.elevatorInfos;
	}
	
	/**
	 * Updates info of given elevator
	 * 
	 * @param id -  elevator id
	 * @param info - new info
	 */
	public void updateInfo(Integer id, ElevatorInfo info) {
		this.elevatorInfos.put(id, info);
	}
	
	/**
	 * Method to add a new Buffer object to the
	 * toElevator ArrayList.
	 */
	public void addElevator(int id, ElevatorInfo info) {
		this.updateInfo(id, info);
		this.toElevator.put(id, new ConcurrentLinkedDeque<Message>());
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
		toFloor.offer(m);
		Logger.printMessage(m, "SENT");
	}
	
	/**
	 * Sends a reply to the elevator 
	 * 
	 * @param m the message 
	 * @param id the elevator id
	 */
	public void replyToElevator(Message m, int id) {
		toElevator.get(id).offer(m);
		Logger.printMessage(m, "SENT");
	}
	
	/**
	 * Receive message from floor 
	 * @return The message in the floor queue
	 */
	public Message receiveFromFloor() {
		Message m = toFloor.poll();
		if (m == null) {
			return new EmptyMessage();
		}
		return m;
	}
	
	/**
	 * 
	 */
	public void updateUI(Message m) {
		toUI.offer(m);
		Logger.printMessage(m, "SENT");
	}
	
	public Message receiveFromUI() {
		Message m = toUI.poll();
		if (m == null) {
			return new EmptyMessage();
		}
		
		return m;
	}
	
	/**
	 * Receive message from elevator 
	 * @return The message in the elevator queue
	 */
	public Message receiveFromElevator(int id) {
		Message m = toElevator.get(id).poll();
		if (m == null) {
			return new EmptyMessage();
		}
		return m;
	}
	
	
	/**
	 * Returns a message from the message queue.
	 * Used by the scheduler.
	 * 
	 * @return the latest message in the queue.
	 */
	public Message pop() {
		Message m = newMessages.poll();


		if (m == null) {
			return new EmptyMessage();
		}
		return m;	
	}
}
