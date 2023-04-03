package ElevatorSimulatorTest.PerformanceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ElevatorSimulator.Simulator;
import ElevatorSimulator.Elevator.ElevatorController;
import ElevatorSimulator.Floor.Floor;
import ElevatorSimulator.Messaging.ConnectionType;
import ElevatorSimulator.Messaging.MessageQueue;
import ElevatorSimulator.Messaging.ServerRPC;
import ElevatorSimulator.Scheduler.Scheduler;

/**
 * Measuring the performance of different parts of the system.
 * 
 * @author Kyra Lothrop
 * @author Bardia Parmoun
 *
 */
public class PerformanceIntegrationTest {
	// Constants used within the test.
	private int numElevators;
	private int numFloors;
	private String inputFileName;
	
	// Making the message queue and the server RPCs.
	private MessageQueue queue;
	private Thread floorServerRPC, elevatorServerRPC;
	
	/**
	 * Initialize the queue and the server RPCs.
	 */
	@BeforeEach
	public void init() {
		Thread.currentThread().setName("PERFORMANCE INTEGRATION TEST THREAD");
		Simulator.DEBUG_MODE = false;
		
		queue = new MessageQueue();
		floorServerRPC = new Thread(new ServerRPC(queue, Scheduler.FLOOR_PORT), "FLOOR MESSAGE THREAD");
		elevatorServerRPC = new Thread(new ServerRPC(queue, Scheduler.ELEVATOR_PORT), "ELEVATOR MESSAGE THREAD");

		floorServerRPC.start();
		elevatorServerRPC.start();
	}

	/**
	 * Measure how long it takes to complete one request with one elevator.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testOneElevatorOneRequest() throws InterruptedException {
		System.out.println("\n----------IntegrationTest.testOneElevatorOneRequest----------\n");

		numFloors = 4;
		numElevators = 1;
		inputFileName = "src/ElevatorSimulatorTest/TestFiles/elevator_test-1.csv";
		
		Thread schedulerThread = new Thread(new Scheduler(queue, false), "SCHEDULER THREAD");
		Thread elevatorControllerThread = new Thread(
				new ElevatorController(numElevators, numFloors, ConnectionType.LOCAL), "ELEVATOR CONTROLLER");
		Thread  floorThread = new Thread(new Floor(inputFileName, numFloors, ConnectionType.LOCAL), "FLOOR THREAD");
		
		schedulerThread.start();
		
		Thread.sleep(1000);
		
		elevatorControllerThread.start();
		
		Thread.sleep(1000);
		
		long startTime = System.currentTimeMillis();
		floorThread.start();
		
		floorThread.join();
		
		long endTime = System.currentTimeMillis();

		System.out.println("Time to complete 1 request with 1 elevator: " + (endTime - startTime));

		floorServerRPC.join();
		elevatorServerRPC.join();
	}
	
	/**
	 * Measures how long it takes to run multiple requests with one elevator.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testOneElevatorMultipleRequests() throws InterruptedException {
		System.out.println("\n----------IntegrationTest.testOneElevatorMultipleRequests----------\n");

		numFloors = 4;
		numElevators = 1;
		inputFileName = "src/ElevatorSimulatorTest/TestFiles/elevator_test-2.csv";
		
		Thread schedulerThread = new Thread(new Scheduler(queue, false), "SCHEDULER THREAD");
		Thread elevatorControllerThread = new Thread(
				new ElevatorController(numElevators, numFloors, ConnectionType.LOCAL), "ELEVATOR CONTROLLER");
		Thread  floorThread = new Thread(new Floor(inputFileName, numFloors, ConnectionType.LOCAL), "FLOOR THREAD");
		
		schedulerThread.start();
		
		Thread.sleep(1000);
		
		elevatorControllerThread.start();
		
		Thread.sleep(1000);
		
		long startTime = System.currentTimeMillis();
		floorThread.start();
		
		floorThread.join();
		
		long endTime = System.currentTimeMillis();

		System.out.println("Time to complete 3 requests with 1 elevator: " + (endTime - startTime));

		floorServerRPC.join();
		elevatorServerRPC.join();
	}
	
	/**
	 * Measures how long it takes to run multiple requests with multiple elevators.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testMultipleElevatorsMultipleRequests() throws InterruptedException {
		System.out.println("\n----------IntegrationTest.testMultipleElevatorsMultipleRequests----------\n");

		numFloors = 4;
		numElevators = 2;
		inputFileName = "src/ElevatorSimulatorTest/TestFiles/elevator_test-2.csv";
		
		Thread schedulerThread = new Thread(new Scheduler(queue, false), "SCHEDULER THREAD");
		Thread elevatorControllerThread = new Thread(
				new ElevatorController(numElevators, numFloors, ConnectionType.LOCAL), "ELEVATOR CONTROLLER");
		Thread  floorThread = new Thread(new Floor(inputFileName, numFloors, ConnectionType.LOCAL), "FLOOR THREAD");
		
		schedulerThread.start();
		
		Thread.sleep(1000);
		
		elevatorControllerThread.start();
		
		Thread.sleep(1000);
		
		long startTime = System.currentTimeMillis();
		floorThread.start();
		
		floorThread.join();
		
		long endTime = System.currentTimeMillis();

		System.out.println("Time to complete 3 requests with 2 elevators: " + (endTime - startTime));

		floorServerRPC.join();
		elevatorServerRPC.join();
	}
	
	/**
	 * Measures how long it takes to complete 5 requests with 2 elevators.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testOverloadSystem() throws InterruptedException {
		System.out.println("\n----------IntegrationTest.testOverloadSystem----------\n");

		numFloors = 5;
		numElevators = 2;
		inputFileName = "src/ElevatorSimulatorTest/TestFiles/elevator_test-3.csv";
		
		Thread schedulerThread = new Thread(new Scheduler(queue, false), "SCHEDULER THREAD");
		Thread elevatorControllerThread = new Thread(
				new ElevatorController(numElevators, numFloors, ConnectionType.LOCAL), "ELEVATOR CONTROLLER");
		Thread  floorThread = new Thread(new Floor(inputFileName, numFloors, ConnectionType.LOCAL), "FLOOR THREAD");
		
		schedulerThread.start();
		
		Thread.sleep(1000);
		
		elevatorControllerThread.start();
		
		Thread.sleep(1000);
		
		long startTime = System.currentTimeMillis();
		floorThread.start();
		
		floorThread.join();
		
		long endTime = System.currentTimeMillis();

		System.out.println("Time to complete 5 requests with 2 elevators: " + (endTime - startTime));

		floorServerRPC.join();
		elevatorServerRPC.join();
	}
}

