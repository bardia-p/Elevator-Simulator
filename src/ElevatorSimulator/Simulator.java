package ElevatorSimulator;

import ElevatorSimulator.Elevator.ElevatorController;
import ElevatorSimulator.Floor.Floor;
import ElevatorSimulator.Messaging.MessageQueue;
import ElevatorSimulator.Scheduler.Scheduler;

/**
 * The class in charge of starting up the subsystems.
 * 
 * @author Guy Morgenshtern
 * @author Sarah Chow
 *
 */
public class Simulator {
	// Keeps track of the input file name for the simulator.
	public static String INPUT = "src/ElevatorSimulator/Resources/elevator_input.csv";
	
	public static int NUM_ELEVATORS = 2;
	
	public static int NUM_FLOORS = 4;
	
	/**
	 * The main method and the starting point for the program.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MessageQueue queue = new MessageQueue();
		
		Thread schedulerThread, elevatorControlThread, floorThread;
				
		ElevatorController elevatorController = new ElevatorController(queue, NUM_ELEVATORS, NUM_FLOORS);
		
		schedulerThread = new Thread(new Scheduler(queue, elevatorController), "SCHEDULER");
		elevatorControlThread = new Thread(elevatorController, "ELEVATOR");
		floorThread = new Thread(new Floor(queue, INPUT,NUM_FLOORS), "FLOOR");
				
		schedulerThread.start();
		elevatorControlThread.start();
		floorThread.start();
	}

}
