package ElevatorSimulator.Messages;

import java.util.Date;

/**
 * The message for when the elevator doors are open.
 * 
 * @author Kyra Lothrop
 *
 */
@SuppressWarnings("serial")
public class DoorOpenedMessage extends Message{
	private StopType type;
	
	private int arrivedFloor;
	private DirectionType direction;
	private int numPickups;
	private int numDropoffs;
	private int elevatorID;
	
	/**
	 * The constructor 
	 * @param timestamp The timestamp
	 * @param arrivedFloor the floor it arrived at
	 * @param type the type of message
	 */
	public DoorOpenedMessage(Date timestamp, int elevatorID, int arrivedFloor, StopType type, DirectionType direction, int numPickups, int numDropoffs){
		this(timestamp, arrivedFloor, type, direction,numPickups, numDropoffs);
		this.elevatorID = elevatorID;
		
	}
	
	public DoorOpenedMessage(Date timestamp, int arrivedFloor, StopType type, DirectionType direction, int numPickups, int numDropoffs){
		super(SenderType.ELEVATOR, timestamp, MessageType.DOORS_OPENED);
		this.type = type;
		this.arrivedFloor = arrivedFloor;
		this.direction = direction;
		this.numPickups = numPickups;
		this.numDropoffs = numDropoffs;
		this.elevatorID = -1;
		
	}
	/**
	 * Gets the stop type
	 * 
	 * @return the stop type
	 */
	public StopType getStopType() {
		return type;
	}
	/**
	 * Gets the arrived floor number
	 * 
	 * @return The arrived floor number
	 */
	public int getArrivedFloor() {
		return arrivedFloor;
	}
	
	/**
	 * Getter for numPickups.
	 * 
	 * @return numPickups value
	 */
	public int getNumPickups(){
		return numPickups;
	}
	
	/**
	 * Getter for numDropoffs.
	 * 
	 * @return numDropoffs value
	 */
	public int getNumDropoffs(){
		return numDropoffs;
	}
	
	/**
	 * Getter for elevatorID.
	 * 
	 * @return elevatorID value
	 */
	public int getID(){
		return elevatorID;
	}
	
	/**
	 * Gets the direction of the elevator 
	 * 
	 * @return the direction, DirectionType
	 */
	@Override
	public DirectionType getDirection() {
		return direction;
	}
	
	/**
	 * Override method to get description.
	 * 
	 * @return the description, String
	 */
	@Override
    public String getDescription() {
        return String.valueOf(this.type);
    }

}
