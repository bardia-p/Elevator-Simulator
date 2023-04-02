package ElevatorSimulator.Messages;

import java.util.Date;

/**
 * transient error message class
 * @author guymorgenshtern
 *
 */
public class DoorInterruptMessage extends Message {

	private int elevatorID;
	
	public DoorInterruptMessage(int elevatorID, Date timestamp, MessageType type) {
		super(SenderType.ELEVATOR, timestamp, type);
		this.elevatorID = elevatorID;
	}
	
	/**
	 * Returns the ID of elevator that sent the message
	 * @return int
	 */
	public int getElevatorID() {
		return elevatorID;
	}

}
