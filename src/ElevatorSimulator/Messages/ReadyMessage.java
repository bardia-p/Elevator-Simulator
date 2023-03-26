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
	// Information of elevator
	private ElevatorInfo elevatorInfo;

	/**
	 * Constructor of the class.
	 * @param type type of message
	 * @param elevatorInfo information object for elevator
	 */
	public ReadyMessage(MessageType type, ElevatorInfo elevatorInfo) {
		super(SenderType.ELEVATOR, null, type);
		this.elevatorInfo = elevatorInfo;
	}

	/**
	 * Getter for elevator information.
	 * @return elevator information object
	 */
	public ElevatorInfo getElevatorInfo() {
		return elevatorInfo;
	}
}
