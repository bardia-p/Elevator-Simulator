/**
 * 
 */
package ElevatorSimulator.Elevator;

import ElevatorSimulator.Messages.DirectionType;

/**
 * Class defining an elevator trip in a given flow.
 * 
 * @author Andre Hazim
 * @author Guy Morgenshtern
 */
public class ElevatorTrip {
	private int pickup, dropoff; // floor numbers 
	private boolean pickedUp; // not really needed 
	private DirectionType directionType;
	
	/**
	 * Constructor of the elevator trip class.
	 * @param pickUp floor to pick up
	 * @param dropOff floor to drop off
	 * @param directionType direction required
	 */
	public ElevatorTrip(int pickUp, int dropOff, DirectionType directionType) {
		this.directionType = directionType;
		this.dropoff = dropOff;
		this.pickup = pickUp;
		this.pickedUp = false;
		
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
}
