package ElevatorSimulator.Messages;

import java.util.ArrayList;
import java.util.Date;

import ElevatorSimulator.Elevator.ElevatorTrip;

/**
 * Depicts when an elevator is stuck.
 * @author Sarah Chow
 *
 */
@SuppressWarnings("serial")
public class ElevatorStuckMessage extends Message{

	// Trips that have been picked up and people are on the elevator.
	private ArrayList<ElevatorTrip> currentTrips;
	
	// Trips that have not been picked up yet,
	private ArrayList<ElevatorTrip> remainingTrips;
	
	// Id of elevator that is stuck.
	private int elevatorId;
	
	/**
	 * Constructor for the ElevatorStuck class.
	 * 
	 * @param timestamp time of message, Date
	 * @param currentTrips trips on elevator, ArrayList<ELevatorTrip>
	 * @param remainingTrips trips yet to be picked up, ArrayList<ElevatorTrip>
	 */
	public ElevatorStuckMessage(Date timestamp, ArrayList<ElevatorTrip> currentTrips, ArrayList<ElevatorTrip> remainingTrips, int elevatorId){
		super(SenderType.FLOOR, timestamp, MessageType.ELEVATOR_STUCK);
		this.currentTrips = currentTrips;
		this.remainingTrips = remainingTrips;
		this.elevatorId = elevatorId;
	}
	
	/**
	 * Method to print description of elevator stuck message.
	 */
	@Override
	public String getDescription() {
		return super.getType() + " Elevator ID: " + this.elevatorId;
	}
	
	/**
	 * Method to get the current trips.
	 * 
	 * @return ArrayList of current trips.
	 */
	public ArrayList<ElevatorTrip> getCurrentTrips(){
		return this.currentTrips;
	}
	
	/**
	 * Method to get the remaining trips.
	 * 
	 * @return ArrayList of remaining trips.
	 */
	public ArrayList<ElevatorTrip> getRemainingTrips(){
		return this.remainingTrips;
	}
}
