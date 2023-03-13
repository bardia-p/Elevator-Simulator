package ElevatorSimulator.Elevator;

import java.util.ArrayList;
import java.util.Date;

import ElevatorSimulator.Logger;
import ElevatorSimulator.Messages.*;
import ElevatorSimulator.Messaging.ClientRPC;
import ElevatorSimulator.Scheduler.Scheduler;

/**
 * The elevator - Receives and replies to messages.
 * 
 * @author Andre Hazim
 * @author Kyra Lothrop
 * @author Guy Morgenshtern
 * @author Bardia Parmoun
 * @author Sarah Chow
 *
 */
public class Elevator extends ClientRPC implements Runnable {
	// Elevator's current state
	private ElevatorState state;
	
	// Check if the elevator is to continue running.
	private boolean shouldRun;

	// elevator's current floor
	private int floor;

	// direction of movement
	private DirectionType direction;

	// elevator designation number
	private int elevatorNumber;
	
	// list of destination floors
	private ArrayList<ElevatorTrip> trips;
	
	// stop type of an arrived
	private StopType stopType;
	
	// list of lights corresponding to active request for each floor
	private boolean[] floorLights;
 
	// Time for the current event.
	private Date currentEventTime;
	
	// Indicates that the elevator has no more incoming requests.
	private boolean canKill;
	
	public static int MOVE_DELAY = 5000;
			
	public static int DOOR_DELAY = 1000;
	
	public static int BOARDING_DELAY = 1000;
	

	
	/**
	 * Constructor for the elevator.
	 * 
	 * @param queue, the message queue.
	 */
	public Elevator(int id, int numFloors) {
		super(Scheduler.ELEVATOR_PORT);
		this.shouldRun = true;
		this.state = null;
		this.floor = 1; // not sure if we should pass in start position
		this.direction = DirectionType.UP;
		this.elevatorNumber = id;
		
		this.trips = new ArrayList<>();
		this.floorLights = new boolean[numFloors];
		
		this.stopType = null;
		this.canKill = false;
		
		this.currentEventTime = null;
		
		changeState(ElevatorState.POLL);
	}

	/**
	 * Requests an update from the scheduler.
	 * 
	 * @return the update received from the scheduler.
	 */
	private Message requestUpdate() {
		Message update = getElevatorUpdate(elevatorNumber);
		if (update == null) {
			return null;
		}
		
		if (update.getType() == MessageType.EMPTY && trips.size() == 0 && canKill) {
			kill();
		}
		
		return update;
	}

	/**
	 * Processes the received message and to send a proper reply.
	 * 
	 * @param message the message to process.
	 */
	private void processMessage(Message message) {
		if (message.getType() == MessageType.KILL) {
			canKill = true;
		} else if (message.getType() == MessageType.REQUEST){
			RequestElevatorMessage requestElevatorMessage = (RequestElevatorMessage) message;
			ElevatorTrip elevatorTrip = new ElevatorTrip(requestElevatorMessage.getFloor(), requestElevatorMessage.getDestination(), requestElevatorMessage.getDirection());
			trips.add(elevatorTrip);
			if (elevatorTrip.getPickup() == floor) {
				changeState(ElevatorState.ARRIVED);
			} else {
				changeState(ElevatorState.MOVING);
			}
			
			if (trips.size() == 1) {
				updateDirection();
			}
			
			sendRequest(new UpdateElevatorInfoMessage(new ElevatorInfo(direction,state , floor, elevatorNumber, trips.size())));
		}
	}
	
	/**
	 * check to see if you have a trip in that direction.
	 * If not change directions.
	 */
	private void updateDirection() {
		if (trips.size() == 0 || hasDropoffInDirection() || hasPickupInDirection()) {
			return;
		}
		
		// cannot fulfill requests in the direction, toggle directions.
		this.direction = direction == DirectionType.UP ? DirectionType.DOWN : DirectionType.UP;
		
		sendRequest(new UpdateElevatorInfoMessage(new ElevatorInfo(direction,state , floor, elevatorNumber, trips.size())));
	}

	/**
	 * Method to determine if the elevator is dropping off in the same direction of request.
	 * @return true if same direction
	 */
	private boolean hasDropoffInDirection() {
		for (ElevatorTrip trip: trips) {
			// Can dropoff a trip
			if (trip.isPickedUp() && trip.getDirectionType() == direction) {
				// Going up to drop off or going down to drop off
				if ((direction == DirectionType.UP && floor <= trip.getDropoff()) ||
						(direction == DirectionType.DOWN && floor >= trip.getDropoff()))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Method to determine if the elevator is picking up in the same direction of request.
	 * @return true if same direction
	 */
	private boolean hasPickupInDirection() {
		for (ElevatorTrip trip: trips) {
			// Can pickup a trip
			if (!trip.isPickedUp()) {
				if ((floor - trip.getPickup() > 0 && direction == DirectionType.DOWN) ||
						(floor - trip.getPickup() < 0 && direction == DirectionType.UP))	
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Kills the elevator subsystem.
	 */
	private void kill() {
		this.shouldRun = false;
	}
	
	/**
	 * returns current elevator state
	 * @return ElevatorState
	 */
	public ElevatorState getState() {
		return state;
	}
	
	/**
	 * returns current floor elevator is at
	 * @return int - floor number
	 */
	public int getFloorNumber() {
		return floor;
	}

	/**
	 * returns direction of elevator
	 * @return DirectionType
	 */
	public DirectionType getDirection() {
		return direction;
	}
	
	/**
	 * returns elevator ID
	 * @return
	 */
	public int getID() {
		return elevatorNumber;
	}
	
	/**
	 * moving state behaviour - determine direction to do
	 * moving -> arrived
	 */
	private void moving() {
		if (this.direction == DirectionType.UP) {
			this.floor++;
		} else {
			this.floor--;
		}
		
		try {
			currentEventTime.setTime(currentEventTime.getTime() + MOVE_DELAY);
			Thread.sleep(MOVE_DELAY);
			changeState(ElevatorState.ARRIVED);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * arrived state behaviour - send arrived message and determine whether or not to open doors
	 * arrived -> poll OR
	 * arrived -> open
	 */
	private void arrived() {
		Message reply = new ArrivedElevatorMessage(currentEventTime, this.floor);
		Logger.printMessage(reply, "SENT");
		
		boolean isPickUp = false;
		boolean isDropoff = false;
		ArrayList<ElevatorTrip> removalList = new ArrayList<>();
		
		int numDropoffs = 0;
		int numPickups = 0;

		DirectionType stopDirection = direction;
		
		for (ElevatorTrip trip: trips) {
			if (trip.getDropoff() == floor && trip.isPickedUp()) { //checking isPickedUp is redundant if only good requests are sent
				isDropoff = true;
				this.stopType = StopType.DROPOFF;
				this.floorLights[floor-1] = false;
				removalList.add(trip);
				numDropoffs++;
				stopDirection = trip.getDirectionType();
			}
		}
		
		
		trips.removeAll(removalList);
		
		// check for pickups in the same direction.
		for(ElevatorTrip trip : trips) {
			// Only pickup if the trip is in your direction OR you no longer have trips in that direction.
			if (trip.getPickup() == floor && !trip.isPickedUp() && trip.getDirectionType() == direction) {
				isPickUp = true;
				this.stopType = StopType.PICKUP;
				trip.setPickedUp(true);
				this.floorLights[trip.getDropoff() - 1] = true;
				stopDirection = trip.getDirectionType();
				numPickups++;
			}
		}
		
		// if no pickups in the that direction check for picks up in the opposite direction.
		if (!isPickUp) {
			// check for pickups in the same direction.
			for(ElevatorTrip trip : trips) {
				// Only pickup if the trip is in your direction OR you no longer have trips in that direction.
				if (trip.getPickup() == floor && !trip.isPickedUp() && (!hasDropoffInDirection() && !hasPickupInDirection())) {
					isPickUp = true;
					this.stopType = StopType.PICKUP;
					trip.setPickedUp(true);
					this.floorLights[trip.getDropoff() - 1] = true;
					stopDirection = trip.getDirectionType();
					numPickups++;
				}
			}
		}
		
		if (isPickUp && isDropoff) {
			this.stopType = StopType.PICKUP_AND_DROPOFF;
		}
		
		updateDirection();
	
		if (isPickUp || isDropoff) {	
			changeState(ElevatorState.OPEN);
			
			DoorOpenedMessage doorOpen = new DoorOpenedMessage(currentEventTime, floor, stopType, stopDirection, numPickups, numDropoffs);
			sendRequest(doorOpen);
			Logger.printMessage(doorOpen, "SENT");
		} else {
			changeState(ElevatorState.POLL);
		}
		
		sendRequest(new UpdateElevatorInfoMessage(new ElevatorInfo(direction,state , floor, elevatorNumber, trips.size())));
	}
	
	/**
	 * open state behaviour - add door opening delay of X seconds
	 * open -> boarding
	 */
	private void open() {
		try {
			currentEventTime.setTime(currentEventTime.getTime() + DOOR_DELAY);
			Thread.sleep(DOOR_DELAY); // change to calculated time
			changeState(ElevatorState.BOARDING);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * boarding state behaviour - add boarding delay of X seconds
	 * boarding -> close
	 */
	private void boarding() {
		try {
			currentEventTime.setTime(currentEventTime.getTime() + BOARDING_DELAY);
			Thread.sleep(BOARDING_DELAY);
			changeState(ElevatorState.CLOSE);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * close state behaviour - add doors closing delay of X seconds
	 * close -> poll
	 */
	private void closeDoors() {
		printFloorLightStatus();
		
		try {
			currentEventTime.setTime(currentEventTime.getTime() + DOOR_DELAY);
			Thread.sleep(DOOR_DELAY); 
			changeState(ElevatorState.POLL);
			sendRequest(new UpdateElevatorInfoMessage(new ElevatorInfo(direction,state , floor, elevatorNumber, trips.size())));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * polling state behaviour - receives any incoming messages and processes them
	 * polling -> moving
	 */
	private void polling() {
		Message incoming = requestUpdate();
		if (incoming != null && incoming.getType() != MessageType.EMPTY) {
			Logger.printMessage(incoming, "RECEIVED");
			
			if (currentEventTime == null) {
				currentEventTime = incoming.getTimestamp();
			}
			processMessage(incoming);
		} else if (trips.size() != 0) {
			changeState(ElevatorState.MOVING);
			sendRequest(new UpdateElevatorInfoMessage(new ElevatorInfo(direction,state , floor, elevatorNumber, trips.size())));
		}
	}
	
	
	/**
	 * The run method for the main logic of the elevator.
	 */
	@Override
	public void run() {
		while (this.shouldRun) {
			if (state.equals(ElevatorState.MOVING)) {
				moving();
			} else if (state.equals(ElevatorState.ARRIVED)) {
				arrived();
			
			} else if (state.equals(ElevatorState.OPEN)) {
				open();
			
			} else if (state.equals(ElevatorState.BOARDING)) {
				boarding();
			
			} else if (state.equals(ElevatorState.CLOSE)) {
				closeDoors();
			
			} else{
				polling();
			}
		}
		
		close();
	}

	/**
	 * Returns the number of trips for the elevator.
	 * 
	 * @return the number of elevator trips.
	 */
	public int getNumTrips() {
		return trips.size();
	}
	
	/**
	 * prints floor light statuses
	 */
	private void printFloorLightStatus() {
		String elevatorLights = "\nELEVATOR " + (elevatorNumber + 1) + " LIGHTS STATUS\n----------------------";
		for (int i = 0; i < this.floorLights.length; i++) {
			elevatorLights += "\n| Floor " + (i + 1) + " light: " + (this.floorLights[i] ? "on " : "off") + " |";
		}
		elevatorLights += "\n----------------------\n";
		System.out.println(elevatorLights);
	}
	
	/**
	 * change and print state
	 * @param newState
	 */
	private void changeState(ElevatorState newState) {
		System.out.println("\nELEVATOR " + (elevatorNumber + 1) + " STATE: --------- " + newState + " ---------");
		state = newState;
	}
	
}
