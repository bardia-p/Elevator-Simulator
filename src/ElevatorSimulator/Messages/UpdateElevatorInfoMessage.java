package ElevatorSimulator.Messages;

import ElevatorSimulator.Elevator.ElevatorInfo;

/**
 * The message used to send the update elevator information.
 * 
 * @author Kyra Lothrop
 *
 */
@SuppressWarnings("serial")
public class UpdateElevatorInfoMessage extends Message{
	// The elevator info kept in the message.
	private ElevatorInfo info;
	
	/**
	 * The constructor for the elevator message.
	 * 
	 * @param info, the elevator info.
	 */
	public UpdateElevatorInfoMessage(ElevatorInfo info) {
		super(SenderType.ELEVATOR, null, MessageType.UPDATE_ELEVATOR_INFO);
		this.info = info;
	}	
	
	/**
	 * Returns the elevator info.
	 * 
	 * @return the elevator info.
	 */
	public ElevatorInfo getInfo() {
		return info;
	}
}
