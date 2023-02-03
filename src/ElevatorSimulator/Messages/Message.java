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

	public Message(SenderType sender, String timestamp, MessageType type){
		this.sender = sender;
		this.timestamp = timestamp;
		this.type = type;
	}
	
	public SenderType getSender() {
		return sender;
	}

	public String getTimestamp() {
		return timestamp;
	}
	
	public MessageType getType() {
		return type;
	}

	public String getDescription() {
		return type.toString() + " " + timestamp + " " + sender + "\n";
	}
	
	
}