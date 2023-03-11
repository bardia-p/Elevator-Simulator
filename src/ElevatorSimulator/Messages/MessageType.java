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
	ACKMessage,
	UPDATE_ELEVATOR_INFO,
	EMPTY
}
