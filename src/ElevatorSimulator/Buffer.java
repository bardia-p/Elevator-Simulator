package ElevatorSimulator;

import java.util.ArrayDeque;

import ElevatorSimulator.Messages.*;

/**
 * An implementation of a simple blocking queue.
 * 
 * @author Bardia Parmoun
 * @author Kyra Lothrop
 *
 */
public class Buffer {
	// Holds all the messages.
	ArrayDeque<Message> messages;
	
	/**
	 * The main controller for the buffer.
	 */
	public Buffer(){
		this.messages = new ArrayDeque<>();
	}
	
	/**
	 * Adds the new message to the buffer.
	 * 
	 * @param m the message to add to the buffer.
	 */
	public synchronized  void put(Message m) {
		while(!messages.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}
		
		messages.offer(m);
		System.out.println(Thread.currentThread().getName() + " sent\n" + m.getDescription() + "\n");

		notifyAll();
	}
	
	/**
	 * Removes a message from the buffer.
	 * 
	 * @return the message that was removed from the buffer.
	 */
	public synchronized Message get() {
		while(messages.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}
		
		Message received = messages.poll();
		System.out.println(Thread.currentThread().getName() + " received\n" + received.getDescription() + "\n");

		notifyAll();
		
		return received;
	}
	
	/**
	 * Returns whether the buffer is empty or not.
	 * 
	 * @return true if the buffer is empty false otherwise.
	 */
	public boolean isEmpty() {
		return messages.isEmpty();
	}
}
