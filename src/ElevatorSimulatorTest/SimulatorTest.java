package ElevatorSimulatorTest;

import org.junit.jupiter.api.Test;

import ElevatorSimulator.Elevator.ElevatorController;
import ElevatorSimulator.Floor.Floor;
import ElevatorSimulator.Messaging.MessageQueue;
import ElevatorSimulator.Scheduler.Scheduler;

/**
 * @author Bardia Parmoun
 * @author Andre Hazim
 *
 */
public class SimulatorTest {
	
	private String INPUT = "";
	
	private int NUM_ELEVATORS = 1;
	private int NUM_FLOORS = 4;

	/**
	 * Starts the simulator with the default filename.
	 * Validates that the simulator fully runs and does not get stuck.
	 * 
	 */
	@Test
	public void testSimulatorDefaultFile() throws Exception {
		
		System.out.println("\n----------testSimulatorDefaultFile----------\n");
		
		INPUT = "src/ElevatorSimulator/Resources/elevator_input.csv";
		
		MessageQueue queue = new MessageQueue();
		
		Thread schedulerThread, elevatorControlThread, floorThread;
				
		ElevatorController elevatorController = new ElevatorController(queue, NUM_ELEVATORS, NUM_FLOORS);
		
		schedulerThread = new Thread(new Scheduler(queue, elevatorController), "SCHEDULER");
		elevatorControlThread = new Thread(elevatorController, "ELEVATOR");
		floorThread = new Thread(new Floor(queue, INPUT,4), "FLOOR");
				
		schedulerThread.start();
		elevatorControlThread.start();
		floorThread.start();
		
		schedulerThread.join();
		elevatorControlThread.join();
		floorThread.join();
	}
	
	/**
	 * Starts the simulator with a custom filename.
	 * Validates that the simulator fully runs and does not get stuck.
	 * 
	 */
	@Test
	public void testSimulatorTestFile() throws Exception {
		
		System.out.println("\n----------testSimulatorTestFile----------\n");

		
		INPUT = "src/ElevatorSimulatorTest/TestFiles/elevator_test-1.csv";
		
		MessageQueue queue = new MessageQueue();
		
		Thread schedulerThread, elevatorControlThread, floorThread;
				
		ElevatorController elevatorController = new ElevatorController(queue, NUM_ELEVATORS, NUM_FLOORS);
		
		schedulerThread = new Thread(new Scheduler(queue, elevatorController), "SCHEDULER");
		elevatorControlThread = new Thread(elevatorController, "ELEVATOR");
		floorThread = new Thread(new Floor(queue, INPUT,NUM_FLOORS), "FLOOR");
				
		schedulerThread.start();
		elevatorControlThread.start();
		floorThread.start();
		
		schedulerThread.join();
		elevatorControlThread.join();
		floorThread.join();
		
	}
}
