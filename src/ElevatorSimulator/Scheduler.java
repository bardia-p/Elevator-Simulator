/**
 * 
 */
package ElevatorSimulator;

import java.util.ArrayList;

/**
 * @author Andre Hazim
 * @author Bardia Parmoun
 *
 */
public class Scheduler implements Runnable{
	// All the new messages for the scheduler.
	private ArrayList<Message> newMessages;
	
	// All the messages sent to the elevator.
	private ArrayList<Message> toElevator;

	// All the messages sent to the floor.
	private ArrayList<Message> toFloor;
	
	/**
	 * Default constructor for the Scheduler.
	 * 
	 * Initializes all the appropriate queues.
	 */
	public Scheduler() {
		this.newMessages = new ArrayList<Message>();
		this.toElevator = new ArrayList<Message>();
		this.toFloor = new ArrayList<Message>();
	}
	
	/**
	 * Adds a message to the newMessages queue.
	 * 
	 * @param m the message to send to the queue.
	 */
	public synchronized void send(Message m) {
		// Ensures the newMessages queue is empty.
		while(!newMessages.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}
		
		System.out.println(Thread.currentThread().getName() + " sent\n" + m.getDescription() + "\n");
		newMessages.add(m);
		
		notifyAll();
	}
	
	/**
	 * Receives a message from the message queue.
	 * Decides the appropriate queue to choose from based on thread name.
	 *
	 * @return the message within the queue.
	 */
	public synchronized Message receive() {
		// Finds the appropriate the caller based on the thread name.
		SenderType caller;
		if (Thread.currentThread().getName().equals("ELEVATOR")) {
			caller = SenderType.ELEVATOR;
		} else {
			caller = SenderType.FLOOR;
		}

		// Ensures the queue is empty.
		while ((toFloor.isEmpty() && caller == SenderType.FLOOR) || 
				(toElevator.isEmpty() && caller == SenderType.ELEVATOR)){
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}
		
		// Finds the received message.
		Message received;

		if (!toFloor.isEmpty() && caller == SenderType.FLOOR){
			received = toFloor.remove(0);
		} else {
			received = toElevator.remove(0);
		}
		
		System.out.println(Thread.currentThread().getName() + " received\n" + received.getDescription() + "\n");

		notifyAll();
		
		return received;
	}
	
	/**
	 * Polls its newMessages queue to see if a new message is sent.
	 * Receives the new message and forwards it to its appropriate queue.
	 * 
	 */
	private synchronized void poll() {
		// Polling the newMessages queue.
		while (newMessages.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}
		
		// Receives the message from the newMessages queue.
		Message received = newMessages.remove(0);
		
		// Puts the message in the respective send queue.
		if (received.sender == SenderType.ELEVATOR) {
			toFloor.add(received);
		} else {
			toElevator.add(received);
		}
		
		System.out.println(Thread.currentThread().getName() + " forwarded\n" + received.getDescription() + "\n");
		
		notifyAll();
	}
	
	@Override
	public void run() {
		while(true) {
			this.poll();
		}
	}
	
}