package ElevatorSimulatorTest;

import java.util.ArrayList;

import ElevatorSimulator.Elevator.Elevator;
import ElevatorSimulator.Elevator.ElevatorController;
import ElevatorSimulator.Messages.*;
import ElevatorSimulator.Messaging.MessageQueue;

/**
 * Class to mock the ElevatorController class for testing purpose.
 * @author Kyra Lothrop
 * @author Sarah Chow
 *
 */
public class MockElevatorController extends ElevatorController{
	
	private ArrayList<Elevator> elevators;
	
	public static int NUM_FLOORS = 4;

	private MessageQueue queue;
	
	private int numElevators;
	private int numFloors;

	/**
	 * Constructor for the mock class. Calls the super class with
	 * the arguments.
	 * @param queue
	 * @param numElevators
	 */
	MockElevatorController(MessageQueue queue, int numElevators){
		super(queue, numElevators, NUM_FLOORS);

		this.queue = queue;
		elevators = new ArrayList<Elevator>();
		this.numElevators = numElevators;
		
		this.initializeElevators();
		
	}
	
	/**
	 * Method to initialize the elevators in the ArrayList and add 
	 * a new elevator to the queue.
	 */
	private void initializeElevators() {
		for (int i = 0; i < numElevators; i++) {
			Elevator elevator = new Elevator(queue, i, this.numFloors);
			elevators.add(elevator);
			queue.addElevator();
		}
	}
	
	/**
	 * Method to return the elevators ArrayList for testing purposes.
	 */
	public ArrayList<Elevator> getAvailableElevators(RequestElevatorMessage message){		
		return this.elevators;
	}
	
	
}
