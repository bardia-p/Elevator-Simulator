package ElevatorSimulator;

import ElevatorSimulator.Messages.Message;
import ElevatorSimulator.Messages.SenderType;

public class MessageQueue {
	// All the new messages for the scheduler.
	private Buffer newMessages;
	
	// All the messages sent to the elevator.
	private Buffer toElevator;

	// All the messages sent to the floor.
	private Buffer toFloor;
	
	public MessageQueue() {
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
	 * Send a reply and add it to the appropriate queue.
	 * 
	 * @param m the message to send to the subsystem.
	 * @param receiver the subsystem in charge of receiving the message.
	 */
	public void reply(Message m, SenderType receiver) {
		if (receiver == SenderType.ELEVATOR) {
			toElevator.put(m);
		} else if (receiver == SenderType.FLOOR) {
			toFloor.put(m);
		}
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
	 * Returns a message from the message queue.
	 * Used by the scheduler.
	 * 
	 * @return the latest message in the queue.
	 */
	public Message pop() {
		return newMessages.get();
	}
}
