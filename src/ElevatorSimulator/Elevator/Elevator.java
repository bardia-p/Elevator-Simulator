package ElevatorSimulator.Elevator;

import java.util.ArrayList;

import ElevatorSimulator.Messages.*;
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
	// The message queue.
	private MessageQueue queue;

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

	// current request being fulfilled
	private Message currentRequest;
	
	// list of destination floors
	private ArrayList<ElevatorTrip> destinations;
	
	// stop type of an arrived
	private StopType stopType;
	
	// list of lights corresponding to active request for each floor
	private boolean[] floorLights;
		
	/**
	 * Constructor for the elevator.
	 * 
	 * @param queue, the message queue.
	 */
	public Elevator(MessageQueue queue, int id, int numFloors) {
		this.queue = queue;
		this.shouldRun = true;
		this.state = null;
		this.floor = 1; // not sure if we should pass in start position
		this.direction = DirectionType.UP;
		this.elevatorNumber = id;
		this.currentRequest = null;
		
		this.destinations = new ArrayList<>();
		this.floorLights = new boolean[numFloors];
		
		this.stopType = null;
		
		changeState(ElevatorState.POLL);
	}

	/**
	 * Requests an update from the scheduler.
	 * 
	 * @return the update received from the scheduler.
	 */
	private Message requestUpdate() {
		if (destinations.size() != 0 && !queue.elevatorHasRequest(elevatorNumber)) {
			return null;
		}
		return queue.receiveFromElevator(elevatorNumber);
	}

	/**
	 * Processes the received message and to send a proper reply.
	 * 
	 * @param message the message to process.
	 */
	private void processMessage(Message message) {
		if (message.getType() == MessageType.KILL) {
			kill(); 
		} else {
			RequestElevatorMessage requestElevatorMessage = (RequestElevatorMessage) message;
			ElevatorTrip elevatorTrip = new ElevatorTrip(requestElevatorMessage.getFloor(), requestElevatorMessage.getDestination(), requestElevatorMessage.getDirection());
			destinations.add(elevatorTrip);
			this.floorLights[requestElevatorMessage.getDestination()-1] = true;
			if (requestElevatorMessage.getFloor() != floor) {
				this.direction = ((destinations.get(0).getPickup() - floor) >= 0) ? DirectionType.UP : DirectionType.DOWN;
				changeState(ElevatorState.MOVING);
			} else {
				changeState(ElevatorState.ARRIVED);
			}
		}
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
		queue.send(reply);
		
		DirectionType newDirection = direction;
		boolean isPickUp = false;
		boolean isDropoff = false;
		ArrayList<ElevatorTrip> removalList = new ArrayList<>();
		for (ElevatorTrip trip: destinations) {
			if(trip.getPickup() == floor) {
				isPickUp = true;
				this.stopType = StopType.PICKUP;

			} else if (trip.getDropoff() == floor && trip.isPickedUp()) { //checking isPickedUp is redundant if only good requests are sent
				isDropoff = true;
				this.stopType = StopType.DROPOFF;
				this.floorLights[floor-1] = false;
				removalList.add(trip);
			}
		}
		
		if (isPickUp && isDropoff) {
			this.stopType = StopType.PICKUP_AND_DROPOFF;
		}
		destinations.removeAll(removalList);
		
		if (!(isPickUp || isDropoff) || destinations.size() == 0) {
			changeState(ElevatorState.POLL);
		} else {			
			
			changeState(ElevatorState.OPEN);

			DoorOpenedMessage doorOpen = new DoorOpenedMessage(currentRequest.getTimestamp(), floor, stopType, this.direction);
			queue.send(doorOpen);
			
			this.direction = newDirection;
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * close state behaviour - add doors closing delay of X seconds
	 * close -> poll
	 */
	private void close() {
		printFloorLightStatus();
		
		try {
			Thread.sleep(1000); // change to calculated time
			changeState(ElevatorState.POLL);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * polling state behaviour - receives any incoming messages and processes them
	 * polling -> moving
	 */
	private void polling() {
		Message incoming = requestUpdate();
		if (incoming != null) {
			currentRequest = incoming;
			processMessage(incoming);
		} else {
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
				close();
			
			} else {
				polling();
			}
		}
	}

	/**
	 * prints floor light statuses
	 */
	private void printFloorLightStatus() {
		String elevatorLights = "\nELEVATOR LIGHTS STATUS\n------------------------------------------------";
		for (int i = 0; i < this.floorLights.length; i++) {
			elevatorLights += "\n| Floor " + (i + 1) + " light on: " + this.floorLights[i] + " |";
			
		}
		elevatorLights += "\n------------------------------------------------\n";
		System.out.println(elevatorLights);
	}
	
	/**
	 * change and print state
	 * @param newState
	 */
	private void changeState(ElevatorState newState) {
		System.out.println("\nELEVATOR STATE: --------- " + newState + " ---------");
		state = newState;

	}
}


