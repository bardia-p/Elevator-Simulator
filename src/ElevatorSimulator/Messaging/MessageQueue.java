package ElevatorSimulator.Messaging;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import ElevatorSimulator.Messages.EmptyMessage;
import ElevatorSimulator.Messages.Message;
import ElevatorSimulator.Messages.MessageType;

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
	
	// All the messages sent to the elevator.
	private HashMap<Integer, ConcurrentLinkedDeque<Message>> toElevator;

	// All the messages sent to the floor.
	private ConcurrentLinkedDeque<Message> toFloor;
	
	// String for scheduler messages.
	
	private String schedulerMessages;
	// String for floor messages.
	private String floorMessages;
	
	// String for elevator messages.
	private String elevatorMessages;
	
	/**
	 * Constructor for the class. Initialzies
	 * the newMessages Buffer object, toElevator ArrayList,
	 * toFloor Buffer object, and the String messages.
	 */
	public MessageQueue() {
		this.newMessages = new ConcurrentLinkedDeque<Message>();
		this.toElevator = new HashMap<Integer, ConcurrentLinkedDeque<Message>>();
		this.toFloor = new ConcurrentLinkedDeque<Message>();
		this.schedulerMessages = "";
		this.floorMessages = "";
		this.elevatorMessages = "";
	}
	
	/**
	 * Method to add a new Buffer object to the
	 * toElevator ArrayList.
	 */
	public void addElevator(int id) {
		this.toElevator.put(id, new ConcurrentLinkedDeque<Message>());
	}
	
	/**
	 * Adds a message to the newMessages buffer.
	 * 
	 * @param m the message to send to the buffer.
	 */
	public void send(Message m) {
		//this.printMessage(m, "SEND");
		newMessages.offer(m);
	}
	
	/**
	 * Send a reply and add it to the appropriate queue.
	 * 
	 * @param m the message to send to the subsystem.
	 * @param receiver the subsystem in charge of receiving the message.
	 */
	public void replyToFloor(Message m) {
		//this.printMessage(m, "SEND");
		toFloor.offer(m);
	}
	
	/**
	 * Sends a reply to the elevator 
	 * 
	 * @param m the message 
	 * @param id the elevator id
	 */
	public void replyToElevator(Message m, int id) {
		printMessage(m, "SENT");
		toElevator.get(id).offer(m);
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
		this.printMessage(m, "RECEIVED");

		if (m == null) {
			return new EmptyMessage();
		}
		return m;	
	}
	
	/**
	 * Method to display a received or sent message.
	 * @param m message in reference
	 * @param type receive or send type
	 */
	private void printMessage(Message m, String type) {
		
		String result = "";
		String messageToPrint = "";
				
		if (m != null) {
			result += "\n---------------------" + Thread.currentThread().getName() +"-----------------------\n";
			result += String.format("| %-15s | %-10s | %-10s | %-3s |\n", "REQUEST", "ACTION", "RECEIVED", "SENT");
			result += new String(new char[52]).replace("\0", "-");
			
			result += String.format("\n| %-15s | %-10s | ", (m.getType() == MessageType.KILL ? "KILL" : m.getDescription()), m.getDirection());
			result += String.format(" %-10s | %-3s |", type == "RECEIVED" ? "*" : " ", type == "RECEIVED" ? " " : "*");
			
			
			if (Thread.currentThread().getName().contains("SCHEDULER")) {				
				messageToPrint = this.schedulerMessages;
			}
			else if (Thread.currentThread().getName().contains("FLOOR")) {
				messageToPrint = this.floorMessages;
			}
			else if (Thread.currentThread().getName().contains("ELEVATOR")) {
				messageToPrint = this.elevatorMessages;
			}
			
			System.out.println(messageToPrint + result);
		}
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
