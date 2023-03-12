package ElevatorSimulator.Messages;


/**
 * The message sent by the elevator controller
 * 
 * @author Bardia Parmoun
 *
 */
@SuppressWarnings("serial")
public class StartMessage extends Message {

	public StartMessage() {
		super(null, null, MessageType.START);
		// TODO Auto-generated constructor stub
	}

}
