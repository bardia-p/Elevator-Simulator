package ElevatorSimulator.Elevator;

import java.util.ArrayList;

import ElevatorSimulator.Messages.*;
import ElevatorSimulator.Messaging.ClientRPC;
import ElevatorSimulator.Messaging.MessageQueue;

/**
 * The elevator - Receives and replies to messages.
 * 
 * @author Andre Hazim
 * @author Kyra Lothrop
 * @author Guy Morgenshtern
 * @author Bardia Parmoun
 *
 */
public class Elevator implements Runnable {
	// Elevator's current state
	private ElevatorState state;
	
	private ElevatorController controller;
	
	// Check if the elevator is to continue running.
	private boolean shouldRun;

	// elevator's current floor
	private int floor;

	// direction of movement
	private DirectionType direction;

	// elevator designation number
	private int elevatorNumber;

	// current request being fulfilled
	private Message currentRequest;
	
	// list of destination floors
	private ArrayList<ElevatorTrip> trips;
	
	// stop type of an arrived
	private StopType stopType;
	
	// list of lights corresponding to active request for each floor
	private boolean[] floorLights;
	
	private boolean canKill;
		
	/**
	 * Constructor for the elevator.
	 * 
	 * @param queue, the message queue.
	 */
	public Elevator(ElevatorController controller, int id, int numFloors) {
		this.shouldRun = true;
		this.state = ElevatorState.POLL;
		this.floor = 1; // not sure if we should pass in start position
		this.direction = DirectionType.UP;
		this.elevatorNumber = id;
		this.currentRequest = null;
		
		this.trips = new ArrayList<>();
		this.floorLights = new boolean[numFloors];
		
		this.stopType = null;
		this.canKill = false;
		
		this.controller = controller;
		
		//changeState(ElevatorState.POLL);
	}

	/**
	 * Requests an update from the scheduler.
	 * 
	 * @return the update received from the scheduler.
	 */
	private Message requestUpdate() {
		Message update = controller.getElevatorUpdate(elevatorNumber);
		if (update == null && trips.size() == 0 && canKill) {
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
		}
	}
	
	/**
	 * check to see if you have a trip in that direction.
	 * If not change directions.
	 */
	private void updateDirection() {
		if (trips.size() == 0) {
			return;
		}
		
		if (hasDropoffInDirection()) {
			return;
		}
		
		if (hasPickupInDirection()) {
			return;
		}
		
		// cannot fulfill requests in the direction, toggle directions.
		this.direction = direction == DirectionType.UP ? DirectionType.DOWN : DirectionType.UP;
		
		controller.sendRequest(new UpdateElevatorInfoMessage(new ElevatorInfo(direction,state , floor, elevatorNumber, trips.size())));
	}

	private boolean hasDropoffInDirection() {
		for (ElevatorTrip trip: trips) {
			// can dropoff a trip.
			if (trip.isPickedUp() && trip.getDirectionType() == direction) {
				// going up to drop off
				if (direction == DirectionType.UP && floor <= trip.getDropoff() && trip.isPickedUp()) {
					return true;
				}
				
				// going down to drop off
				if (direction == DirectionType.DOWN && floor >= trip.getDropoff() && trip.isPickedUp()) {
					return true;
				}	
			}
		}
		
		return false;
	}
	
	private boolean hasPickupInDirection() {
		for (ElevatorTrip trip: trips) {
			// can pickup a trip.
			if (!trip.isPickedUp()) {
				// can pickup a trip.
				if (floor - trip.getPickup() >= 0 && direction == DirectionType.DOWN) {
					return true;
				}
				
				if (floor - trip.getPickup() <= 0 && direction == DirectionType.UP) {
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
			Thread.sleep(5000); // change to calculated time
			changeState(ElevatorState.ARRIVED);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * arrived state behaviour - send arrived message and determine whether or not to open doors
	 * arrived -> poll OR
	 * arrived -> open
	 */
	private void arrived() {
		Message reply = new ArrivedElevatorMessage(currentRequest.getTimestamp(), this.floor);
		controller.sendRequest(reply);
		
		boolean isPickUp = false;
		boolean isDropoff = false;
		ArrayList<ElevatorTrip> removalList = new ArrayList<>();
		
		for (ElevatorTrip trip: trips) {
			if (trip.getPickup() == floor && !trip.isPickedUp() && (trip.getDirectionType() == direction || !hasDropoffInDirection())) {
				isPickUp = true;
				this.stopType = StopType.PICKUP;
				trip.setPickedUp(true);
				this.floorLights[trip.getDropoff() - 1] = true;

			}
			
			if (trip.getDropoff() == floor && trip.isPickedUp()) { //checking isPickedUp is redundant if only good requests are sent
				isDropoff = true;
				this.stopType = StopType.DROPOFF;
				this.floorLights[floor-1] = false;
				removalList.add(trip);
			}
		}
		
		trips.removeAll(removalList);
		
		if (isPickUp && isDropoff) {
			this.stopType = StopType.PICKUP_AND_DROPOFF;
		}
		
		updateDirection();
	
		if (isPickUp || isDropoff) {			
			
			changeState(ElevatorState.OPEN);

			DoorOpenedMessage doorOpen = new DoorOpenedMessage(currentRequest.getTimestamp(), floor, stopType, this.direction);
			controller.sendRequest(doorOpen);
		} else {
			changeState(ElevatorState.POLL);
		}
	}
	
	/**
	 * open state behaviour - add door opening delay of X seconds
	 * open -> boarding
	 */
	private void open() {
		try {
			Thread.sleep(1000); // change to calculated time
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
		// how to do multiple ppl boarding???
		try {
			Thread.sleep(1000); // change to calculated time
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
			Thread.sleep(1000); // change to calculated time
			changeState(ElevatorState.POLL);
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
			printMessage(incoming, "RECEIVED");
			currentRequest = incoming;
			processMessage(incoming);
		} else if (trips.size() != 0) {
			changeState(ElevatorState.MOVING);
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
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

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
		controller.sendRequest(new UpdateElevatorInfoMessage(new ElevatorInfo(direction,state , floor, elevatorNumber, trips.size())));
	}
	
	private void printMessage(Message m, String type) {
		
		String result = "";
		String addResult = "";
		String messageToPrint = "";
				
		if (m != null) {
			
			result += "\n---------------------" + Thread.currentThread().getName() +"-----------------------\n";
			result += String.format("| %-15s | %-10s | %-10s | %-3s |\n", "REQUEST", "ACTION", "RECEIVED", "SENT");
			result += new String(new char[52]).replace("\0", "-");
			
			addResult += String.format("\n| %-15s | %-10s | ", (m.getType() == MessageType.KILL ? "KILL" : m.getDescription()), m.getDirection());
			addResult += String.format(" %-10s | %-3s |", type == "RECEIVED" ? "*" : " ", type == "RECEIVED" ? " " : "*");
			
			System.out.println(messageToPrint + result + addResult);
		}
		
	}
	
}