package ElevatorSimulator.Messages;

public class DoorOpenedMessage extends Message{
	private StopType type;
	
	private int arrivedFloor;
	private DirectionType direction;
	/**
	 * The constructor 
	 * @param timestamp The timestamp
	 * @param arrivedFloor the floor it arrived at
	 * @param type the type of message
	 */
	public DoorOpenedMessage(String timestamp, int arrivedFloor, StopType type, DirectionType direction){
		super(SenderType.ELEVATOR, timestamp, MessageType.DOORS_OPENED);
		this.type = type;
		this.arrivedFloor = arrivedFloor;
		this.direction = direction;
		
	}
	/**
	 * Gets the stop type
	 * 
	 * @return the stop type
	 */
	public StopType getStopType() {
		return type;
	}
	/**
	 * Gets the arrived floor number
	 * 
	 * @return The arrived floor number
	 */
	public int getArrivedFloor() {
		return arrivedFloor;
	}
	
	/**
	 * Gets the direction of the elevator 
	 * 
	 * @return the direction
	 */
	@Override
	public DirectionType getDirection() {
		return direction;
	}
	/**
     * @Override
     */
    public String getDescription() {
        return String.valueOf(this.type);
    }

}
