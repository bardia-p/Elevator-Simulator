package ElevatorSimulator.Messages;

public class DoorClosed extends Message{
	private STOP_TYPE type;
	
	private int arrivedFloor;
	private DirectionType direction;
	/**
	 * The constructor 
	 * @param timestamp The timestamp
	 * @param arrivedFloor the floor it arrived at
	 * @param type the type of message
	 */
	public DoorClosed(String timestamp, int arrivedFloor, STOP_TYPE type, DirectionType direction){
		super(SenderType.ELEVATOR, timestamp, MessageType.DOORS_CLOSED);
		this.type = type;
		this.arrivedFloor = arrivedFloor;
		this.direction = direction;
		
	}
	/**
	 * Gets the stop type
	 * @return the stop type
	 */
	public STOP_TYPE getStopType() {
		return type;
	}
	
	
	
}