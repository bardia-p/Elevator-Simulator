package ElevatorSimulator.Elevator;

import java.util.ArrayList;
import java.util.Date;

import ElevatorSimulator.ErrorGenerator;
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
	private ElevatorState childState;

	// parent state for elevator denoting upper most state
	private ElevatorState parentState;

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

	private static final int MAX_FAULT_TIME = 1000000;

	public static int DOOR_DELAY = 1000;

	public static int BOARDING_DELAY = 3000;

	public static int DOOR_INTERRUPT_DELAY = 1000;

	/**
	 * Constructor for the elevator.
	 * 
	 * @param queue, the message queue.
	 */
	public Elevator(int id, int numFloors) {
		super(Scheduler.ELEVATOR_PORT);
		this.childState = null;
		this.parentState = ElevatorState.OPERATIONAL;
		this.floor = 1;
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
		} else if (message.getType() == MessageType.REQUEST) {
			RequestElevatorMessage requestElevatorMessage = (RequestElevatorMessage) message;
			ElevatorTrip elevatorTrip = new ElevatorTrip(requestElevatorMessage.getFloor(),
					requestElevatorMessage.getDestination(), requestElevatorMessage.getDirection(),
					requestElevatorMessage.getError(), requestElevatorMessage.getErrorTime());
			trips.add(elevatorTrip);

			// pick the direction of the first assigned message.
			if (trips.size() == 1) {
				updateDirection();
			}
		}
	}

	/**
	 * check to see if you have a trip in that direction. If not change directions.
	 */
	private void updateDirection() {
		if (trips.size() == 0 || hasDropoffInDirection() || hasPickupInDirection()) {
			return;
		}

		// cannot fulfill requests in the direction, toggle directions.
		this.direction = direction == DirectionType.UP ? DirectionType.DOWN : DirectionType.UP;

		sendRequest(new UpdateElevatorInfoMessage(
				new ElevatorInfo(direction, parentState, childState, floor, elevatorNumber, trips.size())));
	}

	/**
	 * Method to determine if the elevator is dropping off in the same direction of
	 * request.
	 * 
	 * @return true if same direction
	 */
	private boolean hasDropoffInDirection() {
		for (ElevatorTrip trip : trips) {
			// Can dropoff a trip
			if (trip.isPickedUp() && trip.getDirectionType() == direction) {
				// Going up to drop off or going down to drop off
				if ((direction == DirectionType.UP && floor <= trip.getDropoff())
						|| (direction == DirectionType.DOWN && floor >= trip.getDropoff())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Method to determine if the elevator is picking up in the same direction of
	 * request.
	 * 
	 * @return true if same direction
	 */
	private boolean hasPickupInDirection() {
		for (ElevatorTrip trip : trips) {
			// Can pickup a trip
			if (!trip.isPickedUp()) {
				if ((floor - trip.getPickup() > 0 && direction == DirectionType.DOWN)
						|| (floor - trip.getPickup() < 0 && direction == DirectionType.UP)) {
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
		this.parentState = null;
	}

	/**
	 * returns current elevator state
	 * 
	 * @return ElevatorState
	 */
	public ElevatorState getState() {
		return childState;
	}
	
	/**
	 * returns the parent state for the elevator.
	 * 
	 * @return ElevatorState
	 */
	public ElevatorState getParentState() {
		return parentState;
	}

	/**
	 * returns current floor elevator is at
	 * 
	 * @return int - floor number
	 */
	public int getFloorNumber() {
		return floor;
	}

	/**
	 * returns direction of elevator
	 * 
	 * @return DirectionType
	 */
	public DirectionType getDirection() {
		return direction;
	}

	/**
	 * returns elevator ID
	 * 
	 * @return
	 */
	public int getID() {
		return elevatorNumber;
	}

	/**
	 * moving state behaviour - determine direction to do moving -> arrived
	 */
	private void moving() {
		if (this.direction == DirectionType.UP) {
			this.floor++;
		} else {
			this.floor--;
		}

		try {
			Thread.sleep(MOVE_DELAY);
		} catch (InterruptedException e) {
			return;
		}
		
		currentEventTime.setTime(currentEventTime.getTime() + MOVE_DELAY);
		changeState(ElevatorState.ARRIVED);

	}

	/**
	 * Checks to see if the elevator can perform a pickup.
	 * 
	 * @return
	 */
	private boolean canPerformPickup() {
		// check for pickups in the same direction.
		for (ElevatorTrip trip : trips) {
			// Only pickup if the trip is in your direction OR you no longer have trips in
			// that direction.
			if (trip.getPickup() == floor && !trip.isPickedUp() && trip.getDirectionType() == direction) {
				return true;
			}
		}

		// check for pickups in the same direction.
		for (ElevatorTrip trip : trips) {
			// Only pickup if the trip is in your direction OR you no longer have trips in
			// that direction.
			if (trip.getPickup() == floor && !trip.isPickedUp()
					&& (!hasDropoffInDirection() && !hasPickupInDirection())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * arrived state behaviour - send arrived message and determine whether or not
	 * to open doors arrived -> poll OR arrived -> open
	 */
	private void arrived() {
		Message reply = new ArrivedElevatorMessage(currentEventTime, this.floor);
		Logger.printMessage(reply, "SENT");

		boolean isPickUp = false;
		boolean isDropoff = false;
		ArrayList<ElevatorTrip> removalList = new ArrayList<>();
		int timeToFault = MAX_FAULT_TIME;

		int numDropoffs = 0;
		int numPickups = 0;

		DirectionType stopDirection = direction;

		// check for pickups in the same direction.
		for (ElevatorTrip trip : trips) {
			// Only pickup if the trip is in your direction OR you no longer have trips in
			// that direction.
			if (trip.getPickup() == floor && !trip.isPickedUp() && trip.getDirectionType() == direction) {
				isPickUp = true;
				this.stopType = StopType.PICKUP;
				trip.setPickedUp(true);
				this.floorLights[trip.getDropoff() - 1] = true;
				stopDirection = trip.getDirectionType();
				numPickups++;
				if (trip.getFault() == ErrorType.ELEVATOR_STUCK) {
					timeToFault = timeToFault > trip.getTimeToFault() ? trip.getTimeToFault() : timeToFault;
				}
			}
		}

		// if no pickups in the that direction check for picks up in the opposite
		// direction.
		if (!isPickUp) {
			// check for pickups in the same direction.
			for (ElevatorTrip trip : trips) {
				// Only pickup if the trip is in your direction OR you no longer have trips in
				// that direction.
				if (trip.getPickup() == floor && !trip.isPickedUp()
						&& (!hasDropoffInDirection() && !hasPickupInDirection())) {
					isPickUp = true;
					this.stopType = StopType.PICKUP;
					trip.setPickedUp(true);
					this.floorLights[trip.getDropoff() - 1] = true;
					stopDirection = trip.getDirectionType();
					numPickups++;
					if (trip.getFault() == ErrorType.ELEVATOR_STUCK) {

						timeToFault = timeToFault > trip.getTimeToFault() ? trip.getTimeToFault() : timeToFault;
					}
				}
			}
		}

		// check for dropoffs in the same direction.
		for (ElevatorTrip trip : trips) {
			if (trip.getDropoff() == floor && trip.isPickedUp()) {
				isDropoff = true;
				this.stopType = StopType.DROPOFF;
				this.floorLights[floor - 1] = false;
				removalList.add(trip);
				numDropoffs++;
				stopDirection = trip.getDirectionType();
			}
		}

		trips.removeAll(removalList);

		if (isPickUp && isDropoff) {
			this.stopType = StopType.PICKUP_AND_DROPOFF;
		}

		updateDirection();

		// Checks to see if the lights can be updated.
		if (isPickUp || isDropoff) {
			changeState(ElevatorState.OPEN);

			DoorOpenedMessage doorOpen = new DoorOpenedMessage(currentEventTime, floor, stopType, stopDirection,
					numPickups, numDropoffs);
			sendRequest(doorOpen);
			Logger.printMessage(doorOpen, "SENT");
		} else {
			changeState(ElevatorState.POLL);
		}

		// Checks to see if an elevator fault error can be generated.
		if (isPickUp && timeToFault < MAX_FAULT_TIME) {
			(new Thread(new ErrorGenerator(timeToFault, ErrorType.ELEVATOR_STUCK, this, Thread.currentThread()))).start();
		}

		sendRequest(new UpdateElevatorInfoMessage(
				new ElevatorInfo(direction, parentState, childState, floor, elevatorNumber, trips.size())));
	}

	/**
	 * open state behaviour - add door opening delay of X seconds open -> boarding
	 */
	private void open() {
		try {
			Thread.sleep(DOOR_DELAY); // change to calculated time
		} catch (InterruptedException e) {
			return;
		}

		currentEventTime.setTime(currentEventTime.getTime() + DOOR_DELAY);
		changeState(ElevatorState.BOARDING);
	}

	/**
	 * boarding state behaviour - add boarding delay of X seconds boarding -> close
	 */
	private void boarding() {
		
		for (ElevatorTrip trip : trips) {
			if (trip.getFault() == ErrorType.DOOR_INTERRUPT && trip.isPickedUp() && trip.getPickup() == this.floor) {
				(new Thread(new ErrorGenerator(trip.getTimeToFault(), trip.getFault(), this, Thread.currentThread()))).start();
				trip.setFault(null);
			}
		}
		
		try {
			Thread.sleep(BOARDING_DELAY);

		} catch (InterruptedException e) {
			return;
		}

		currentEventTime.setTime(currentEventTime.getTime() + BOARDING_DELAY);
		changeState(ElevatorState.CLOSE);
	}

	/**
	 * close state behaviour - add doors closing delay of X seconds close -> poll
	 */
	private void closeDoors() {
		printFloorLightStatus();

		try {
			Thread.sleep(DOOR_DELAY);
		} catch (InterruptedException e) {
			return;
		}

		currentEventTime.setTime(currentEventTime.getTime() + DOOR_DELAY);
		changeState(ElevatorState.POLL);
		sendRequest(new UpdateElevatorInfoMessage(
				new ElevatorInfo(direction, parentState, childState, floor, elevatorNumber, trips.size())));
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

			if (canPerformPickup()) {
				changeState(ElevatorState.ARRIVED);
			} else {
				changeState(ElevatorState.MOVING);
			}

			sendRequest(new UpdateElevatorInfoMessage(
					new ElevatorInfo(direction, parentState, childState, floor, elevatorNumber, trips.size())));
		}
	}

	/**
	 * Handles the doorInterrupt state.
	 * boarding -> boorInterrupt
	 */
	private void doorInterrupt() {
		try {
			Thread.sleep(DOOR_INTERRUPT_DELAY);
		} catch (InterruptedException e) {
			return;
		}
		currentEventTime.setTime(currentEventTime.getTime() + DOOR_INTERRUPT_DELAY);
		changeState(ElevatorState.BOARDING);
	}

	/**
	 * The run method for the main logic of the elevator.
	 */
	@Override
	public void run() {
		while (this.parentState == ElevatorState.OPERATIONAL) {
			if (childState.equals(ElevatorState.MOVING)) {
				moving();
			} else if (childState.equals(ElevatorState.ARRIVED)) {
				arrived();

			} else if (childState.equals(ElevatorState.OPEN)) {
				open();

			} else if (childState.equals(ElevatorState.BOARDING)) {
				boarding();

			} else if (childState.equals(ElevatorState.CLOSE)) {
				closeDoors();

			} else if (childState.equals(ElevatorState.DOOR_INTERRUPT)) {
				doorInterrupt();

			} else {
				polling();
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (parentState == ElevatorState.ELEVATOR_STUCK) {
			sendRequest(new ElevatorStuckMessage(currentEventTime, getCurrentTrips(), getRemainingTrips(), elevatorNumber));
		}
		//close();
	}

	/**
	 * Returns the list of trips left for the elevator to schedule.
	 * 
	 * @return
	 */
	private ArrayList<ElevatorTrip> getRemainingTrips() {
		ArrayList<ElevatorTrip> remainingTrips = new ArrayList<>();

		for (ElevatorTrip trip : trips) {
			if (!trip.isPickedUp()) {
				remainingTrips.add(trip);
			}
		}

		return remainingTrips;
	}

	/**
	 * Returns the list of trips that are currently on the elevator.
	 * 
	 * @return
	 */
	private ArrayList<ElevatorTrip> getCurrentTrips() {

		ArrayList<ElevatorTrip> currentTrips = new ArrayList<>();
		for (ElevatorTrip trip : trips) {
			if (trip.isPickedUp()) {
				currentTrips.add(trip);
			}
		}

		return currentTrips;
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
	 * Handles the event when an elevator stuck error is generated.
	 */
	public void handleElevatorStuck() {
		changeParentState(ElevatorState.ELEVATOR_STUCK);
	}

	/**
	 * Handles the event when a door fault is generated.
	 */
	public void handleDoorFault() {
		changeState(ElevatorState.DOOR_INTERRUPT);
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
	 * 
	 * @param newState
	 */
	private synchronized void changeState(ElevatorState newState) {
		System.out.println("\nELEVATOR " + (elevatorNumber + 1) + " STATE: --------- " + newState + " ---------");
		childState = newState;
	}

	/**
	 * Changes the parent state of the elevator.
	 * 
	 * @param newState, the new state for the elevator.
	 */
	private void changeParentState(ElevatorState newState) {
		System.out.println("\nELEVATOR " + (elevatorNumber + 1) + " STATE: --------- " + newState + " ---------");
		this.parentState = newState;
	}

}
