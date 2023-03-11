package ElevatorSimulator.Messages;

@SuppressWarnings("serial")
public class ACKMessage extends Message {
	public ACKMessage() {
		super(null, null, MessageType.ACKMessage);
	}

}
