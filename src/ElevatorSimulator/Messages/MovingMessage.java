package ElevatorSimulator.Messages;

/**
 * A special type of message used to indicate the elevator has arrived.
 * 
 * @author Andre Hazim
 * @author Guy Morgenshtern
 *
 */
public class MovingMessage extends Message {
	
	private DirectionType direction;
	/**
	 * The constructor for the arrived elevator message.
	 * 
	 * @param timestamp the message timestamp.
	 * @param arrivedFloor the floor at which the elevator arrived at.
	 */
	public MovingMessage(String timestamp, DirectionType direction) {
		super(SenderType.ELEVATOR, timestamp, MessageType.MOVING);
		this.direction = direction;
	}
		
	/**
	 * Returns the description for the arrived message.
	 * 
	 * @return the description of the arrived message.
	 */
	@Override
	public String getDescription() {
		return super.getDescription() + "going: " + direction;
	}

}
