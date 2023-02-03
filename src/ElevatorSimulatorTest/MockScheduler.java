package ElevatorSimulatorTest;

import ElevatorSimulator.Buffer;
import ElevatorSimulator.Scheduler;
import ElevatorSimulator.Messages.*;

public class MockScheduler extends Scheduler {
	private Buffer messagesBuffer;
	private int sendCount;
	private int receiveCount;
	private boolean shouldKill;
	
	public MockScheduler() {
		messagesBuffer = new Buffer();
		sendCount = 0;
		receiveCount = 0;
		shouldKill = false;
	}
	
	@Override
	public void send(Message m) {
		if (shouldKill) {
			messagesBuffer.put(new KillMessage(SenderType.FLOOR, "end test"));
		} else {
			messagesBuffer.put(m);	
		}
		sendCount++;
	}
	
	@Override
	public Message receive(SenderType sender) {
		Message received = messagesBuffer.get();
		receiveCount++;
		shouldKill = true;
		return received;
	}
	
	public void killMock() {
		
	}
	
	
	public int getSendCount() {
		return sendCount;
	}
	
	public int getReceiveCount() {
		return receiveCount;
	}
	
}
