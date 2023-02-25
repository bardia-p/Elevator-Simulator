package ElevatorSimulatorTest;

import ElevatorSimulator.Messages.*;
import ElevatorSimulator.Messaging.Buffer;
import ElevatorSimulator.Scheduler.Scheduler;

/**
 * A mock version of the Scheduler class simulating its behaviour for testing.
 * The mock scheduler will receive a single message, record it in its buffer 
 * and return it upon a receive() call.
 * 
 * To help stop the other threads the scheduler will send a kill thread after
 * sending one message.
 * 
 * @author Bardia Parmoun
 * @author Sarah Chow
 *
 */
public class MockScheduler extends Scheduler {
	// Keeps track of the messages in the scheduler.
	private Buffer messagesBuffer;
	
	// Keeps track of the number of messages sent by the mock scheduler.
	
	private int sendCount;
	
	// Keeps track of the number of messages received by the scheduler.
	private int receiveCount;
	
	// Keeps track of whether the mock scheduler should be killed or not.
	private boolean shouldKill;
	
	/**
	 * The default constructor for the scheduler.
	 * Initializes all the required variables.
	 * 
	 */
	public MockScheduler() {
		messagesBuffer = new Buffer();
		sendCount = 0;
		receiveCount = 0;
		shouldKill = false;
	}
	
	/**
	 * A simplified version of the send function.
	 * 
	 * @param m the message to send.
	 */
	@Override
	public void send(Message m) {
		if (shouldKill) {
			messagesBuffer.put(new KillMessage(SenderType.FLOOR, "end test"));
		} else {
			messagesBuffer.put(m);	
		}
		sendCount++;
	}
	
	/**
	 * A simplified version of the receive function.
	 * 
	 * @param sender the type of the sender for receiving the message.
	 * 
	 * @return the message that was received.
	 */
	@Override
	public Message receive(SenderType sender) {
		Message received = messagesBuffer.get();
		receiveCount++;
		kill();
		return received;
	}
	
	/**
	 * Kills the scheduler.
	 */
	public void kill() {
		shouldKill = true;
	}
	
	
	/**
	 * Get the number of messages sent by the scheduler.
	 * 
	 * @return the sendCount parameter.
	 */
	public int getSendCount() {
		return sendCount;
	}
	
	/**
	 * Get the number of messages received by the scheduler.
	 * 
	 * @return the receiveCount parameter.
	 */
	public int getReceiveCount() {
		return receiveCount;
	}
	
}
