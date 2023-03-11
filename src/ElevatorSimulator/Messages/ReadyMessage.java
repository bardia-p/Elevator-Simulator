package ElevatorSimulator.Messages;

import java.util.Date;

import ElevatorSimulator.Elevator.ElevatorInfo;

public class ReadyMessage extends Message{
	private ElevatorInfo elevatorInfo;

	public ReadyMessage( Date timestamp, MessageType type, ElevatorInfo elevatorInfo) {
		super(SenderType.ELEVATOR, timestamp, type);
		this.elevatorInfo = elevatorInfo;
	}

	public ElevatorInfo getElevatorInfo() {
		return elevatorInfo;
	}
	
	
	
	

}
