package ElevatorSimulator.Elevator;

import java.util.ArrayList;
import java.util.Iterator;

import ElevatorSimulator.Messages.KillMessage;
import ElevatorSimulator.Messages.MessageType;
import ElevatorSimulator.Messages.ReadyMessage;
import ElevatorSimulator.Messages.RequestElevatorMessage;
import ElevatorSimulator.Messages.StartMessage;
import ElevatorSimulator.Messaging.ClientRPC;
import ElevatorSimulator.Messaging.MessageQueue;
import ElevatorSimulator.Scheduler.Scheduler;

/**
 * 
 * The Elevator controller class responsible for controlling the multiple elevators 
 * @author Andre Hazim, 
 * @author Bardia Parmoun
 */
public class ElevatorController extends ClientRPC implements Runnable {
	
	private ArrayList<Elevator> elevators;
	private int numElevators;
	private int numFloors;
	private MessageQueue queue;
	
	/**
	 * The constructor of the Elevator Controller 
	 * 
	 * @param queue The message queue of the system
	 * @param numElevators The number of Elevators
	 * @param numFloors The number of Floors 
	 */
	public ElevatorController(MessageQueue queue, int numElevators, int numFloors) {
		super(Scheduler.ELEVATOR_PORT);
		this.elevators = new ArrayList<>();
		this.numElevators = numElevators;
		this.queue = queue;		
		this.numFloors = numFloors;
	}
	
	/**
	 * Starts the number of elevator threads specified in the constructor
	 */
	private void initializeElevators() {
		for (int i = 0; i < numElevators; i++) {
			Elevator elevator = new Elevator(queue, i, this.numFloors);
			sendRequest(new ReadyMessage(null, MessageType.READY, 
					new ElevatorInfo(elevator.getDirection(),elevator.getState() , elevator.getFloorNumber(), elevator.getID()
							, elevator.getNumTrips())));
			elevators.add(elevator);
			queue.addElevator();
			Thread elevatorThread = new Thread(elevators.get(i), "ELEVATOR " + (i+1));
			elevatorThread.start();
			
			
		}
		sendRequest(new StartMessage()); 
	}
	
	/**
	 * Checks to see if the elevator is in a valid state
	 * 
	 * @param elevator The elevator you want to check
	 * 
	 * @return Boolean true of false
	 */
	private boolean checkElevatorValid(Elevator elevator) {
		
		if (elevator.getState() == ElevatorState.POLL) {
			return true;
		}
		
		return false;
		
	}
	

	/**
	 * Kills all the elevators
	 * 
	 * @param message a kill message
	 */
	public void kill(KillMessage message) {
		for (int i =0; i < elevators.size(); i++) {
			queue.replyToElevator(message, i);
		}
	}

	@Override
	/**
	 * Starts all the elevator threads
	 */
	public void run() {
		initializeElevators();
	}
}