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
	
	/**
	 * The constructor 
	 * @param timestamp The timestamp
	 * @param arrivedFloor the floor it arrived at
	 * @param type the type of message
	 */
	public DoorOpenedMessage(Date timestamp, int arrivedFloor, StopType type, DirectionType direction, int numPickups, int numDropoffs){
		super(SenderType.ELEVATOR, timestamp, MessageType.DOORS_OPENED);
		this.type = type;
		this.arrivedFloor = arrivedFloor;
		this.direction = direction;
		this.numPickups = numPickups;
		this.numDropoffs = numDropoffs;
		
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
	
	public int getNumPickups(){
		return numPickups;
	}
	
	public int getNumDropoffs(){
		return numDropoffs;
	}
	
	/**
	 * Gets the direction of the elevator 
	 * 
	 * @return the direction
	 */
	@Override
	public DirectionType getDirection() {
		return direction;
	}
	
	
	/**
     * @Override
     */
    public String getDescription() {
        return String.valueOf(this.type);
    }

}
