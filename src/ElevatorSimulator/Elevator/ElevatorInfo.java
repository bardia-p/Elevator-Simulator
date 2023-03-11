package ElevatorSimulator.Elevator;

import java.io.Serializable;

import ElevatorSimulator.Messages.DirectionType;

@SuppressWarnings("serial")
public class ElevatorInfo implements Serializable{
	
	private DirectionType directionType;
	private ElevatorState state; 
	private int curFloor, elevatorId, numRequest;
	
	public ElevatorInfo(DirectionType directionType, ElevatorState state, int curFloor, int elevatorId,
			int numRequest) {
		this.directionType = directionType;
		this.state = state;
		this.curFloor = curFloor;
		this.elevatorId = elevatorId;
		this.numRequest = numRequest;
	}
	public DirectionType getDirection() {
		return directionType;
	}
	public ElevatorState getState() {
		return state;
	}
	public int getFloorNumber() {
		return curFloor;
	}
	public int getElevatorId() {
		return elevatorId;
	}
	public int getNumRequest() {
		return numRequest;
	} 
	public void setState(ElevatorState state) {
		this.state = state;
	}
	

}
