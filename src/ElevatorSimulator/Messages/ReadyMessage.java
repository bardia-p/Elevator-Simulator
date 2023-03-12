package ElevatorSimulator.Messages;

import java.util.Date;

import ElevatorSimulator.Elevator.ElevatorInfo;

/**
 * The message sent by the elevator to say an elevator is ready.
 * 
 * @author Guy Morgenshtern
 *
 */
@SuppressWarnings("serial")
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
