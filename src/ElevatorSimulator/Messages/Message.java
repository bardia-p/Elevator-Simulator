/**
 * 
 */
package ElevatorSimulator.Messages;

// THIS IS A TEMPORARY CLASS FOR MESSAGES IDEALLY WE SHOULD HAVE A CLASS FOR EACH MESSAGE TYPE.

/**
 * @author Bardia Parmoun
 *
 */
public class Message {
	private SenderType sender;
	private String timestamp;
	private MessageType type;

	Message(SenderType sender, String timestamp, MessageType type){
		this.sender = sender;
		this.timestamp = timestamp;
		this.type = type;
	}
	
	/**
	 * 
	 * @return SenderType - message sender
	 */
	public SenderType getSender() {
		return sender;
	}

	/**
	 * 
	 * @return String - timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}
	
	/**
	 * 
	 * @return MessageType
	 */
	public MessageType getType() {
		return type;
	}

	/**
	 * 
	 * @return String - message description
	 */
	public String getDescription() {
		return type.toString() + " " + timestamp + " " + sender + "\n";
	}
	
	
}