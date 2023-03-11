package ElevatorSimulator.Messages;

import java.util.Date;

/**
 * A special type of message used for killing the system.
 * 
 * @author Guy Morgenshtern
 * @author Kyra Lothrop
 *
 */
@SuppressWarnings("serial")
public class KillMessage extends Message{
	// The reason for killing the system.
	private String reason;
	
	/**
	 * The constructor for the kill message.
	 * 
	 * @param sender the sender of the message.
	 * @param reason the reason for killing the system.
	 */
	public KillMessage(SenderType sender, Date timestamp, String reason) {
		super(sender,timestamp, MessageType.KILL);
		this.reason = reason;
	}
	
	/**
	 * Returns the message description.
	 * 
	 * @return String - description
	 */
	public String getDescription() {
		return super.getDescription() + "\n" + reason;
	}
}
