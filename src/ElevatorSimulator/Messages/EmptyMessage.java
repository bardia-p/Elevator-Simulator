package ElevatorSimulator.Messages;

/**
 * The empty message returned when there are no requests to send to the subsystem.
 * 
 * @author Guy Morgenshtern.
 *
 */
@SuppressWarnings("serial")
public class EmptyMessage extends Message{
	public EmptyMessage() {
		super(null, null, MessageType.EMPTY);
	}
}
