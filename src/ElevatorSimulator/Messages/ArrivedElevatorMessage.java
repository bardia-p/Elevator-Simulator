package ElevatorSimulator.Messages;

public class ArrivedElevatorMessage extends Message {

	private int floorNumber;
	public ArrivedElevatorMessage(String timestamp, int arrivedFloor) {
		super(SenderType.ELEVATOR, timestamp, MessageType.ARRIVE);
		this.floorNumber = arrivedFloor;
	}
		
	@Override
	public String getDescription() {
		return super.getDescription() + "at: " + floorNumber;
	}
}
