package ElevatorSimulator.Messages;

/**
 * An enum to keep track of the different message types passed in the buffer.
 *
 */
public enum MessageType {
	ARRIVE,
	REQUEST,
	MOVING,
	KILL,
	DOORS_OPENED,
	READY,
	START,
	GET_UPDATE,
	ACKMESSAGE,
	UPDATE_ELEVATOR_INFO,
	EMPTY,
	ELEVATOR_STUCK,
	DOOR_INTERRUPT
}
