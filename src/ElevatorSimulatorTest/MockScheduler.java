package ElevatorSimulatorTest;

import ElevatorSimulator.Buffer;
import ElevatorSimulator.DirectionType;
import ElevatorSimulator.Scheduler;
import ElevatorSimulator.Messages.*;

public class MockScheduler extends Scheduler {
	private Buffer messagesBuffer;
	private int sendCount;
	private int receiveCount;
	
	public MockScheduler() {
		messagesBuffer = new Buffer();
		sendCount = 0;
		receiveCount = 0;
	}
	
	@Override
	public void send(Message m) {
		messagesBuffer.put(m);
		sendCount++;
	}
	
	@Override
	public Message receive(SenderType sender) {
		Message received = messagesBuffer.get();
		receiveCount++;
		return received;
	}
	
	public int getSendCount() {
		return sendCount;
	}
	
	public int getReceiveCount() {
		return receiveCount;
	}
}
