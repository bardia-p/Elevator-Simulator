/**
 * 
 */
package ElevatorSimulator;

// THIS IS A TEMPORARY CLASS FOR MESSAGES IDEALLY WE SHOULD HAVE A CLASS FOR EACH MESSAGE TYPE.

/**
 * @author Bardia Parmoun
 *
 */
public class Message {
	SenderType sender;
	String timestamp;
	int floorNumber;
	String content;
	MessageType type;
	
	Message(SenderType sender, String timestamp, int floorNumber, String content, MessageType type){
		this.sender = sender;
		this.timestamp = timestamp;
		this.floorNumber = floorNumber;
		this.content = content;
		this.type = type;
	}
	
	String getDescription() {
		return timestamp + "\n" + floorNumber + "\n" + content + "\n" + type;
	}
}

enum MessageType{
	READY_TO_MOVE,
	REQUEST_ELEVATOR,
	MOVE_TO,
	ADD_STOP,
	ARRIVED,
}

enum SenderType {
	ELEVATOR,
	FLOOR,
}
