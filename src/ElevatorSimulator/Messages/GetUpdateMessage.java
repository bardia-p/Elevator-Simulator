package ElevatorSimulator.Messages;

import java.util.Date;

@SuppressWarnings("serial")
public class GetUpdateMessage extends Message {
	private int elevatorNumber;
	
	public GetUpdateMessage(Date timestamp, SenderType sender) {
		super(sender, timestamp, MessageType.GET_UPDATE);
	}
	
	public GetUpdateMessage(Date timestamp, SenderType sender, int elevatorNumber) {
		this(timestamp, sender);
		this.elevatorNumber = elevatorNumber;
	}
	
	public int getElevatorNumber() {
		return elevatorNumber;
	}
}
