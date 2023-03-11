package ElevatorSimulator.Messages;

import ElevatorSimulator.Elevator.ElevatorInfo;

@SuppressWarnings("serial")
public class UpdateElevatorInfoMessage extends Message{
	private ElevatorInfo info;
	
	public UpdateElevatorInfoMessage(ElevatorInfo info) {
		super(SenderType.ELEVATOR, null, MessageType.UPDATE_ELEVATOR_INFO);
		this.info = info;
	}	
	
	public ElevatorInfo getInfo() {
		return info;
	}
}
