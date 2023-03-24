/**
 * 
 */
package ElevatorSimulator.Elevator;

import java.io.Serializable;

import ElevatorSimulator.Messages.DirectionType;
import ElevatorSimulator.Messages.ErrorType;

/**
 * Class defining an elevator trip in a given flow.
 * 
 * @author Andre Hazim
 * @author Guy Morgenshtern
 */
@SuppressWarnings("serial")
public class ElevatorTrip implements Serializable{
	// Pickup and dropoff floors for the elevator.
	private int pickup, dropoff; 
	
	// Checks to see if the trip was picked up.
	private boolean pickedUp;
	private DirectionType directionType;
	
	private ErrorType fault;
	private int timeToFault;
	
	/**
	 * Constructor of the elevator trip class.
	 * @param pickUp floor to pick up
	 * @param dropOff floor to drop off
	 * @param directionType direction required
	 */
	public ElevatorTrip(int pickUp, int dropOff, DirectionType directionType) {
		this(pickUp, dropOff, directionType, null, -1);
	}
	
	/**
	 * Constructor of the elevator trip class.
	 * @param pickUp floor to pick up
	 * @param dropOff floor to drop off
	 * @param directionType direction required
	 * @param fault denotes a fault the trip should have
	 * @param timeToFault time before fault should occur relative to pickup time
	 */
	public ElevatorTrip(int pickUp, int dropOff, DirectionType directionType, ErrorType fault, int timeToFault) {
		this.directionType = directionType;
		this.dropoff = dropOff;
		this.pickup = pickUp;
		this.pickedUp = false;
		this.fault = fault;
		this.timeToFault = timeToFault;
		
	}

	/**
	 * Method indicating whether trip has been picked up.
	 * @return boolean indicating true if picked up
	 */
	public boolean isPickedUp() {
		return pickedUp;
	}

	/**
	 * Set the value of isPickedUp.
	 * @param isPickedUp boolean to be set
	 */
	public void setPickedUp(boolean isPickedUp) {
		this.pickedUp = isPickedUp;
	}

	/**
	 * Get the value of isPickedUp.
	 * @return boolean indicating true if picked up
	 */
	public int getPickup() {
		return pickup;
	}

	/**
	 * Get the value of getDropoff.
	 * @return boolean indicating true if dropped off
	 */
	public int getDropoff() {
		return dropoff;
	}

	/**
	 * Getter for the direction type of trip.
	 * @return DirectionType indicating the direction
	 */
	public DirectionType getDirectionType() {
		return directionType;
	}

	/**
	 * Getter for Elevator error type
	 * @return ErrorType indicating error
	 */
	public ErrorType getFault() {
		return fault;
	}

	/**
	 * Getter for time to fault
	 * @return int denoting time until fault occurs relative to pickup
	 */
	public int getTimeToFault() {
		return timeToFault;
	}
	
	public void setFault(ErrorType error) {
		this.fault = error;
	}
	
	
}
