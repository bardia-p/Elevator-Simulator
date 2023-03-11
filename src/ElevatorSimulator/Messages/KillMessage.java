package ElevatorSimulator.Messages;

/**
 * A special type of message used for killing the system.
 * 
 * @author Guy Morgenshtern
 * @author Kyra Lothrop
 *
 */
public class KillMessage extends Message{
	// The reason for killing the system.
	private String reason;
	
	/**
	 * The constructor for the kill message.
	 * 
	 * @param sender the sender of the message.
	 * @param reason the reason for killing the system.
	 */
	public KillMessage(SenderType sender, String reason) {
		super(sender,"00:00:00", MessageType.KILL);
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
