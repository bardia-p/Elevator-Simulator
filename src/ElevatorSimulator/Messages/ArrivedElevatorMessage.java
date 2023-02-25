package ElevatorSimulator.Messages;

/**
 * A special type of message used to indicate the elevator has arrived.
 * 
 * @author Andre Hazim
 * @author Kyra Lothrop
 *
 */
public class ArrivedElevatorMessage extends Message {
	// The floor number the elevator arrived at.
	private int floorNumber;
	private DirectionType direction;
	private STOP_TYPE type;
	
	/**
	 * The constructor for the arrived elevator message.
	 * 
	 * @param timestamp the message timestamp.
	 * @param arrivedFloor the floor at which the elevator arrived at.
	 */
	public ArrivedElevatorMessage(String timestamp, int arrivedFloor,STOP_TYPE type, DirectionType direction) {
		super(SenderType.ELEVATOR, timestamp, MessageType.ARRIVE);
		this.floorNumber = arrivedFloor;
		this.direction =direction;
	}
		
	/**
	 * Returns the description for the arrived message.
	 * 
	 * @return the description of the arrived message.
	 */
	@Override
	public String getDescription() {
		return super.getDescription() + "at: " + floorNumber;
	}
	/**
	 * gets the arrived floor number
	 * @return arrived floor number
	 */
	public int getArrivedFloor() {
		return floorNumber;
	}

	/**
	 * gets the direction
	 * @return direction
	 */
	public DirectionType getDirection() {
		return direction;
	}
	/**
	 * Gets the stop type
	 * @return the stop type
	 */
	public STOP_TYPE getStopType() {
		return type;
	}
}
