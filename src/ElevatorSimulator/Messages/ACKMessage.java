package ElevatorSimulator.Messages;

/**
 * The ACK message returned by the scheduler.
 * 
 * @author Bardia Parmoun
 *
 */
@SuppressWarnings("serial")
public class ACKMessage extends Message {
	public ACKMessage() {
		super(null, null, MessageType.ACKMESSAGE);
	}

}
