package ElevatorSimulator.Elevator;

import java.io.Serializable;

import ElevatorSimulator.Messages.DirectionType;

/**
 * The class in charge of holding the elevator status used for transferring to the scheduler.
 * 
 * @author Andre Hazim
 * @author Guy Morgenshtern
 *
 */
@SuppressWarnings("serial")
public class ElevatorInfo implements Serializable{
	// Keeps track of the direction of the elevator.
	private DirectionType directionType;
	
	// Keeps track of the parent state of the elevator.
	private ElevatorState parentState; 
	
	// Keeps track of the state of the elevator.
	private ElevatorState state; 

	/**
	 * The current floor for the elevator
	 */
	private int curFloor;
	
	/**
	 * The elevator id.
	 */
	private int elevatorId;
	
	/**
	 * The number of elevator requests.
	 */
	private int numRequest;
	
	/**
	 * The constructor for elevator info.
	 * 
	 * @param directionType, the direction of the elevator.
	 * @param parentState, the parent state of the elevator.
	 * @param state, the state of the elevator.
	 * @param curFloor, the current floor for the elevator.
	 * @param elevatorId, the elevator id.
	 * @param numRequest, number of requests for the elevator.
	 */
	public ElevatorInfo(DirectionType directionType, ElevatorState parentState, ElevatorState state, int curFloor, int elevatorId, int numRequest) {
		this.directionType = directionType;
		this.parentState = parentState;
		this.state = state;
		this.curFloor = curFloor;
		this.elevatorId = elevatorId;
		this.numRequest = numRequest;
	}
	
	/**
	 * Returns the elevator direction.
	 * 
	 * @return the elevator direction.
	 */
	public DirectionType getDirection() {
		return directionType;
	}
	
	/**
	 * Return the elevator parent state.
	 * 
	 * @return the elevator parent state.
	 */
	public ElevatorState getParentState() {
		return parentState;
	}
	
	
	/**
	 * Return the elevator state.
	 * 
	 * @return the elevator state.
	 */
	public ElevatorState getState() {
		return state;
	}
	
	/**
	 * Return the elevator floor number.
	 * 
	 * @return the elevator floor number.
	 */
	public int getFloorNumber() {
		return curFloor;
	}
	
	/**
	 * Return the elevator id.
	 * 
	 * @return the elevator number.
	 */
	public int getElevatorId() {
		return elevatorId;
	}
	
	/**
	 * Return the number of elevator requests.
	 * 
	 * @return the number of elevator requests.
	 */
	public int getNumRequest() {
		return numRequest;
	} 

}
