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
	
	/**
	 * The constructor for the arrived elevator message.
	 * 
	 * @param timestamp the message timestamp.
	 * @param arrivedFloor the floor at which the elevator arrived at.
	 */
	public ArrivedElevatorMessage(String timestamp, int arrivedFloor) {
		super(SenderType.ELEVATOR, timestamp, MessageType.ARRIVE);
		this.floorNumber = arrivedFloor;
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
}
