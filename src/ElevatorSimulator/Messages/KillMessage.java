package ElevatorSimulator.Messages;

public class KillMessage extends Message{
	private String message;
	
	public KillMessage(String message) {
		super(SenderType.FLOOR,"00:00:00", MessageType.KILL);
		this.message = message;
	}
	
	/**
	 * @return String - description
	 */
	public String getDescription() {
		return super.getDescription() + "\n" + message;
	}
}
