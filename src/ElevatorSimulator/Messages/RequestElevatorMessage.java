package ElevatorSimulator.Messages;

/**
 * A special type of message used for requesting the elevator.
 * 
 * @author Guy Morgenshtern
 * @author Sarah Chow
 *
 */
public class RequestElevatorMessage extends Message {
	// The floor that the elevator was requested at.
	private int floorNumber;
	
	// The elevator button that was pressed.
	private int destination;
	
	// The direction that was requested.
	private DirectionType direction;
	
	/**
	 * The constructor for the request elevator message.
	 * 
	 * @param timestamp the timestamp of the request.
	 * @param floorNumber the floor number.
	 * @param direction the request direction.
	 * @param destination the destination.
	 */
	public RequestElevatorMessage(String timestamp, int floorNumber,  DirectionType direction, int destination) {
		super(SenderType.FLOOR, timestamp, MessageType.REQUEST);
		this.floorNumber = floorNumber;
		this.destination = destination;
		this.direction = direction;
	}
	
	/**
	 * Returns a detailed description of the request.
	 */
	@Override
	public String getDescription() {
		return super.getDescription() + "from: " + floorNumber + " to: " + destination + " " + direction;
	}
	
	/**
	 * Returns the requested destination.
	 * 
	 * @return the destination requested.
	 */
	public int getDestination() {
		return this.destination;
	}
	
	public int getFloor() {
		return floorNumber;
	}
	
	public DirectionType getDirection() {
		return direction;
	}
}
