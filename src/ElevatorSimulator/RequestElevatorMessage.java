package ElevatorSimulator;

public class RequestElevatorMessage extends Message {
	private int floorNumber;
	private int destination;
	private DirectionType direction;
	
	public RequestElevatorMessage(String timestamp, int floorNumber,  DirectionType direction, int destination) {
		super(SenderType.FLOOR, timestamp);
		this.floorNumber = floorNumber;
		this.destination = destination;
		this.direction = direction;
	}
	
	@Override
	String getDescription() {
		return super.getDescription() + floorNumber + "\n" + destination + "\n" + direction + "\n";
	}
	
	public int getDestination() {
		return this.destination;
	}

}
