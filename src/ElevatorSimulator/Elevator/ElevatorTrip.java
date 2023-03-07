/**
 * 
 */
package ElevatorSimulator.Elevator;

import ElevatorSimulator.Messages.DirectionType;

/**
 * @author Andre Hazim
 * @author Guy Morgenshtern
 *
 */
public class ElevatorTrip {
	private int pickup, dropoff; // floor numbers 
	boolean isPickedUp; // not really needed 
	private DirectionType directionType;
	
	public ElevatorTrip(int pickUp, int dropOff, DirectionType directionType) {
		this.directionType = directionType;
		this.dropoff = dropOff;
		this.pickup = pickUp;
		this.isPickedUp = false;
		
	}

	public boolean isPickedUp() {
		return isPickedUp;
	}

	public void setPickedUp(boolean isPickedUp) {
		this.isPickedUp = isPickedUp;
	}

	public int getPickup() {
		return pickup;
	}

	public int getDropoff() {
		return dropoff;
	}

	public DirectionType getDirectionType() {
		return directionType;
	}
	
	
}
