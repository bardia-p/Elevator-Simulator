/**
 * 
 */
package ElevatorSimulator.Elevator;

/**
 * The states of the elevators
 * 
 * @author Andre Hazim
 */
public enum ElevatorState {
	POLL,
	CLOSE,
	BOARDING,
	OPEN,
	MOVING,
	ARRIVED,
	OPERATIONAL,
	ELEVATOR_STUCK,
	DOOR_INTERRUPT
}
