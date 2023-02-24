package ElevatorSimulator;

import java.util.ArrayList;
import java.util.Collections;

import ElevatorSimulator.Messages.*;

/**
 * The elevator - Receives and replies to messages.
 * 
 * @author Andre Hazim
 * @author Kyra Lothrop
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
	
	private ArrayList<Integer> destinations;
	
	private STOP_TYPE stopType;
		
	/**
	 * Constructor for the elevator.
	 * 
	 * @param queue, the message queue.
	 */
	public Elevator(MessageQueue queue, int id) {
		this.queue = queue;
		this.shouldRun = true;
		this.state = ElevatorState.POLL;
		this.floor = 1; // not sure if we should pass in start position
		this.direction = DirectionType.UP;
		this.elevatorNumber = id;
		this.currentRequest = null;
		
		this.destinations = new ArrayList<>();
		
		this.stopType = null;
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
			destinations.add(requestElevatorMessage.getFloor());
			destinations.add(requestElevatorMessage.getDestination());
			
			if (requestElevatorMessage.getFloor() != floor) {
				this.direction = ((requestElevatorMessage.getFloor() - floor) >= 0) ? DirectionType.UP : DirectionType.DOWN;
				this.state = ElevatorState.MOVING;
			} else {
				this.state = ElevatorState.ARRIVED;
			}
		}
	}

	/**
	 * Moves the elevator to the appropriate floor.
	 * 
	 * @param timestamp the timestamp for the event.
	 * @param floor     the floor to move the elevator to.
	 */
	private void arrived() {
		Message reply = new ArrivedElevatorMessage(currentRequest.getTimestamp(), this.floor);
		queue.send(reply);
	}

	/**
	 * Kills the elevator subsystem.
	 */
	private void kill() {
		this.shouldRun = false;
	}

	/**
	 * sends moving message to scheduler
	 */
	private void moveTo() {
		Message reply = new MovingMessage(currentRequest.getTimestamp(), this.direction);
		queue.send(reply); 
	}
	
	/**
	 * 
	 */
	private void toggleDirection() {
		this.direction = (this.direction == DirectionType.UP) ? DirectionType.DOWN : DirectionType.UP;
	}
	
	public ElevatorState getState() {
		return state;
	}
	
	public int getFloorNumber() {
		return floor;
	}

	public DirectionType getDirection() {
		return direction;
	}
	
	
	public int getID() {
		return elevatorNumber;
	}
	
	/**
	 * The run method for the main logic of the elevator.
	 */
	@Override
	public void run() {
		while (this.shouldRun) {
			System.out.println("ELEVATOR STATE: --------- " + this.state + " ---------");
			if (state.equals(ElevatorState.MOVING)) {
				if (this.direction == DirectionType.UP) {
					this.floor++;
				} else {
					this.floor--;
				}
				
				try {
					Thread.sleep(5000); // change to calculated time
					this.state = ElevatorState.ARRIVED;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (state.equals(ElevatorState.ARRIVED)) {
				arrived();
								
				if (!destinations.contains(floor)) {
					this.state = ElevatorState.POLL;
				} else {
					destinations.remove((Integer) floor);
					
					if (destinations.size() == 1) {
						this.direction = (destinations.get(0) - floor >= 0) ? DirectionType.UP : DirectionType.DOWN;
						this.stopType = STOP_TYPE.PICKUP;
					} else {
						this.stopType = STOP_TYPE.DROPOFF;
					}
					this.state = ElevatorState.OPEN;
				}
			
			} else if (state.equals(ElevatorState.OPEN)) {
				try {
					Thread.sleep(1000); // change to calculated time
					this.state = ElevatorState.BOARDING;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			} else if (state.equals(ElevatorState.BOARDING)) {
				// how to do multiple ppl boarding???
				try {
					Thread.sleep(1000); // change to calculated time
					this.state = ElevatorState.CLOSE;
					
					DoorClosed reply = new DoorClosed(currentRequest.getTimestamp(), floor, stopType);
					
					queue.send(reply);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			} else if (state.equals(ElevatorState.CLOSE)) {
				try {
					Thread.sleep(1000); // change to calculated time
					this.state = ElevatorState.POLL;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			} else {
				Message incoming = requestUpdate();
				if (incoming != null) {
					currentRequest = incoming;
					processMessage(incoming);
				} else {
					this.state = ElevatorState.MOVING;
				}
			}
		}
	}
}


