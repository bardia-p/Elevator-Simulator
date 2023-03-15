package ElevatorSimulator.Messages;

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

	public ReadyMessage(MessageType type, ElevatorInfo elevatorInfo) {
		super(SenderType.ELEVATOR, null, type);
		this.elevatorInfo = elevatorInfo;
	}

	public ElevatorInfo getElevatorInfo() {
		return elevatorInfo;
	}
}
