package ElevatorSimulator;

public class ArrivedElevatorMessage extends Message {

	private int floorNumber;
	ArrivedElevatorMessage(String timestamp, int arrivedFloor) {
		super(SenderType.ELEVATOR, timestamp);
		this.floorNumber = arrivedFloor;
	}
		
	@Override
	String getDescription() {
		return super.getDescription() + floorNumber + "\n";
	}
}
