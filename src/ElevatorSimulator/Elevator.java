package ElevatorSimulator;

import java.util.ArrayDeque;

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
	private ArrayDeque<Integer> destinations;

	/**
	 * Constructor for the elevator.
	 * 
	 * @param queue, the message queue.
	 */
	public Elevator(MessageQueue queue) {
		this.queue = queue;
		this.shouldRun = true;
		this.state = ElevatorState.POLL;
		this.floor = 1; // not sure if we should pass in start position
		this.direction = null;
		this.elevatorNumber = 1; // in future iterations pass in number
		this.currentRequest = null;
		
		this.destinations = new ArrayDeque<>();
	}

	/**
	 * Requests an update from the scheduler.
	 * 
	 * @return the update received from the scheduler.
	 */
	private Message requestUpdate() {
		return queue.receive(SenderType.ELEVATOR);
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
			destinations.add(((RequestElevatorMessage)message).getDestination());
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
	 * The run method for the main logic of the elevator.
	 */
	@Override
	public void run() {
		while (this.shouldRun) {
			System.out.println("--------- " + this.state + " ---------");
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
				if (this.floor != destinations.peek()) {
					this.state = ElevatorState.POLL;
				} else {
					destinations.pop();
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
					processMessage(currentRequest);
				}
				
				this.state = ElevatorState.MOVING;
			}
		}
	}
}
