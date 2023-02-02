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
	
	Message(SenderType sender, String timestamp){
		this.sender = sender;
		this.timestamp = timestamp;
	}
	
	String getDescription() {
		return timestamp + "\n" + sender + "\n";
	}
}

enum SenderType {
	ELEVATOR,
	FLOOR,
}
