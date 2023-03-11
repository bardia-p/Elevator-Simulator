package ElevatorSimulator.Messages;

import java.io.Serializable;
import java.util.Date;

/**
 * The default message class used for holding the information that is passed in the buffers.
 * 
 * @author Guy Morgenshtern
 * @author Bardia Parmoun
 *
 */
@SuppressWarnings("serial")
public class Message implements Serializable {

	// The sender of the message.
	private SenderType sender;
	
	// The timestamp used in the message.
	private Date timestamp;
	
	// The message type.
	private MessageType type;

	/**
	 * The constructor for the message.
	 * 
	 * @param sender the sender of the message.
	 * @param timestamp the timestamp for the message.
	 * @param type the type of the message
	 */
	public Message(SenderType sender, Date timestamp, MessageType type){
		this.sender = sender;
		this.timestamp = timestamp;
		this.type = type;
	}
	
	/**
	 * Returns the sender of the message.
	 * 
	 * @return SenderType - message sender
	 */
	public SenderType getSender() {
		return sender;
	}

	/**
	 * Returns the timestamp for the message.
	 * 
	 * @return String - timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Returns the type of the message.
	 * 
	 * @return MessageType
	 */
	public MessageType getType() {
		return type;
	}

	/**
	 * Returns the message description.
	 * 
	 * @return String - message description
	 */
//	public String getDescription() {
//		return type.toString() + " " + timestamp + " " + sender + " ";
//	}
	
	public String getDescription() {
		 return "";
	}
	
	public DirectionType getDirection() {
		return null;
	}
	
	
}