package ElevatorSimulator.Messages;

import java.util.Date;

/**
 * A special type of message used for requesting the elevator.
 * 
 * @author Guy Morgenshtern
 * @author Sarah Chow
 *
 */
@SuppressWarnings("serial")
public class RequestElevatorMessage extends Message {
	// The floor that the elevator was requested at.
	private int floorNumber;
	
	// The elevator button that was pressed.
	private int destination;
	
	// The direction that was requested.
	private DirectionType direction;
	
	// Time of error.
	private int errorTime;
	
	// Error type.
	private ErrorType error;
	
	/**
	 * The constructor for the request elevator message.
	 * 
	 * @param timestamp the timestamp of the request.
	 * @param floorNumber the floor number.
	 * @param direction the request direction.
	 * @param destination the destination.
	 * @param errorTime the time of error.
	 * @param error the error.
	 */
	public RequestElevatorMessage(Date timestamp, int floorNumber,  DirectionType direction, int destination, int errorTime, ErrorType error) {
		super(SenderType.FLOOR, timestamp, MessageType.REQUEST);
		this.floorNumber = floorNumber;
		this.destination = destination;
		this.direction = direction;
		this.errorTime = errorTime;
		this.error = error;
	}
	
	/**
	 * Returns a detailed description of the request.
	 */
	@Override
	public String getDescription() {
		return "From: " + floorNumber + " To: " + destination + " With " + String.valueOf(error) + " at time: " + errorTime;
	}
	
	/**
	 * Returns the error time.
	 * 
	 * @return the error time.
	 */
	public int getErrorTime() {
		return this.errorTime;
	}
	
	/**
	 * Returns the error.
	 * 
	 * @return the error.
	 */
	public ErrorType getError() {
		return this.error;
	}
	
	/**
	 * Returns the requested destination.
	 * 
	 * @return the destination requested.
	 */
	public int getDestination() {
		return this.destination;
	}
	
	/**
	 * Gets the floor from the request
	 * 
	 * @return floor number
	 */
	public int getFloor() {
		return floorNumber;
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
}
