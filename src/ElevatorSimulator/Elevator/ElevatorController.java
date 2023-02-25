package ElevatorSimulator.Elevator;

import java.util.ArrayList;

import ElevatorSimulator.Messages.KillMessage;
import ElevatorSimulator.Messages.RequestElevatorMessage;
import ElevatorSimulator.Messaging.MessageQueue;

/**
 * 
 * @author Andre Hazim, 
 * @author Bardia Parmoun
 * 
 * The Elevator controller class responsible for controlling the multiple elevators 
 *
 */
public class ElevatorController implements Runnable {
	
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
			elevators.add(elevator);
			queue.addElevator();
			Thread elevatorThread = new Thread(elevator, "ELEVATOR " + (i+1));
			elevatorThread.start();
		}
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
	 * Gets all the available elevators 
	 * 
	 * @param message A request elevator message 
	 * 
	 * @return A list of availble elevators 
	 */
	public ArrayList<Elevator> getAvailableElevators(RequestElevatorMessage message){
		ArrayList<Elevator> availableElevators = new ArrayList<>();

		for (Elevator e : elevators) {
			if (checkElevatorValid(e)) {
				availableElevators.add(e);
			}
			
		}
		return availableElevators;
	}
	
	/**
	 * kills all the elevators
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
	 * starts all the elevator threads
	 */
	public void run() {
		initializeElevators();
	}
}