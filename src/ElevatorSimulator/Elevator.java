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
	
	// destination queue
	private ArrayList<Message> pickup;

	// destination queue
	private ArrayList<Message> dropoff;
	
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
		
		this.pickup = new ArrayList<>();
		this.dropoff = new ArrayList<>();
		
		this.stopType = null;
	}

	/**
	 * Requests an update from the scheduler.
	 * 
	 * @return the update received from the scheduler.
	 */
	private Message requestUpdate() {
		if (queue.elevatorHasRequest(elevatorNumber)) {
			return queue.receiveFromElevator(elevatorNumber);
		} else if (pickup.size() != 0 || dropoff.size() != 0) {
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
			pickup.add(requestElevatorMessage);
			this.direction = ((requestElevatorMessage.getFloor() - floor) >= 0) ? DirectionType.UP : DirectionType.DOWN;
		}
	}

	/**
	 * Moves the elevator to the appropriate floor.
	 * 
	 * @param timestamp the timestamp for the event.
	 * @param floor     the floor to move the elevator to.
	 */
	private void arrived() {
		if (this.direction == DirectionType.UP) {
			this.floor++;
		} else {
			this.floor--;
		}
		
		if (floor == 1 || floor == 4) {
			//toggleDirection();
		}
		
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
	
	private STOP_TYPE canStop() {
		for (Message r : dropoff) {
			if (((RequestElevatorMessage)r).getDestination() == this.floor) {
				dropoff.remove(r);
				return STOP_TYPE.DROPOFF;
			}
		}
		
		for (Message r : pickup) {
			if (((RequestElevatorMessage)r).getFloor() == this.floor) {
				pickup.remove(r);
				dropoff.add(r);
				this.direction = ((RequestElevatorMessage)r).getDirection();
				return STOP_TYPE.PICKUP;
			}
		}
		

		
		return null;
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
				try {
					Thread.sleep(5000); // change to calculated time
					this.state = ElevatorState.ARRIVED;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (state.equals(ElevatorState.ARRIVED)) {
				arrived();
				
				stopType = canStop();
				
				if (stopType == null) {
					this.state = ElevatorState.POLL;
				} else {
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
				}
				
				this.state = ElevatorState.MOVING;
			}
		}
	}
}


