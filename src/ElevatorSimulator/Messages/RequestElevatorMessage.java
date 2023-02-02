package ElevatorSimulator.Messages;

import ElevatorSimulator.DirectionType;

public class RequestElevatorMessage extends Message {
	private int floorNumber;
	private int destination;
	private DirectionType direction;
	
	public RequestElevatorMessage(String timestamp, int floorNumber,  DirectionType direction, int destination) {
		super(SenderType.FLOOR, timestamp, MessageType.REQUEST);
		this.floorNumber = floorNumber;
		this.destination = destination;
		this.direction = direction;
	}
	
	@Override
	public String getDescription() {
		return super.getDescription() + "from: " + floorNumber + " to: " + destination + " " + direction + " ";
	}
	
<<<<<<< HEAD:src/ElevatorSimulator/RequestElevatorMessage.java
	public int getDestination() {
		return this.destination;
	}

=======
	
>>>>>>> 68f26bcd99a2450ba25164f5becac837046c48fe:src/ElevatorSimulator/Messages/RequestElevatorMessage.java
}
