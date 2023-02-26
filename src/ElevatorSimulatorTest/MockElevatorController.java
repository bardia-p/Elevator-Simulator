package ElevatorSimulatorTest;

import java.util.ArrayList;

import ElevatorSimulator.Elevator.Elevator;
import ElevatorSimulator.Elevator.ElevatorController;
import ElevatorSimulator.Messages.*;
import ElevatorSimulator.Messaging.Buffer;
import ElevatorSimulator.Messaging.MessageQueue;
import ElevatorSimulator.Scheduler.Scheduler;

/**

 * 
 * @author Bardia Parmoun
 * @author Sarah Chow
 *
 */
public class MockElevatorController extends ElevatorController{
	
	private ArrayList<Elevator> elevators;
	
	public static int NUM_FLOORS = 4;

	private MessageQueue queue;
	
	private int numElevators;
	private int numFloors;

 

	MockElevatorController(MessageQueue queue, int numElevators){
		super(queue, numElevators, NUM_FLOORS);

		this.queue = queue;
		elevators = new ArrayList<Elevator>();
		this.numElevators = numElevators;
		
		this.initializeElevators();
		
	}
	

	private void initializeElevators() {
		for (int i = 0; i < numElevators; i++) {
			Elevator elevator = new Elevator(queue, i, this.numFloors);
			elevators.add(elevator);
			queue.addElevator();
		}
	}
	
	public ArrayList<Elevator> getAvailableElevators(RequestElevatorMessage message){
		
		System.out.println(elevators.size());
		return this.elevators;
	}
	
	
}
