package ElevatorSimulator.Messages;

import java.util.Date;

/**
 * The message sent by the subsystem to get an update.
 * 
 * @author Sarah Chow
 *
 */
@SuppressWarnings("serial")
public class GetUpdateMessage extends Message {
	// Number of the elevator
	private int elevatorNumber;
	
	/**
	 * Constructor of the class.
	 * @param timestamp timestamp of message
	 * @param sender sender type of message
	 */
	public GetUpdateMessage(Date timestamp, SenderType sender) {
		super(sender, timestamp, MessageType.GET_UPDATE);
	}
	
	/**
	 * Constructor of the class.
	 * @param timestamp timestamp of message
	 * @param sender sender type of message
	 * @param elevatorNumber number of the elevator
	 */
	public GetUpdateMessage(Date timestamp, SenderType sender, int elevatorNumber) {
		this(timestamp, sender);
		this.elevatorNumber = elevatorNumber;
	}
	
	/**
	 * Getter for elevator number.
	 * @return number of the elevator
	 */
	public int getElevatorNumber() {
		return elevatorNumber;
	}
}
