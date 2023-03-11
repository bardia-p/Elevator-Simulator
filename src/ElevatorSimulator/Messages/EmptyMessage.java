package ElevatorSimulator.Messages;

@SuppressWarnings("serial")
public class EmptyMessage extends Message{
	public EmptyMessage() {
		super(null, null, MessageType.EMPTY);
	}
}
