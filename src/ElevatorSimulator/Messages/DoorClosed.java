package ElevatorSimulator.Messages;

public class DoorClosed extends Message{
	private STOP_TYPE type;
	
	public DoorClosed(String timestamp, int arrivedFloor, STOP_TYPE type){
		super(SenderType.ELEVATOR, timestamp, MessageType.DOORS_CLOSED);
		this.type = type;
	}
	
	public STOP_TYPE getStopType() {
		return type;
	}
}
