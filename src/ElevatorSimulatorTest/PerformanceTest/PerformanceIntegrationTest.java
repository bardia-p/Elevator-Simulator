package ElevatorSimulatorTest.PerformanceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ElevatorSimulator.Simulator;
import ElevatorSimulator.Elevator.ElevatorController;
import ElevatorSimulator.Floor.Floor;
import ElevatorSimulator.Messaging.ConnectionType;
import ElevatorSimulator.Messaging.MessageQueue;
import ElevatorSimulator.Scheduler.Scheduler;
import ElevatorSimulatorTest.MockServerRPC;

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
		floorServerRPC = new Thread(new MockServerRPC(queue, Scheduler.FLOOR_PORT), "FLOOR MESSAGE THREAD");
		elevatorServerRPC = new Thread(new MockServerRPC(queue, Scheduler.ELEVATOR_PORT), "ELEVATOR MESSAGE THREAD");

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
		inputFileName = "src/ElevatorSimulatorTest/TestFiles/elevator_test-one_request.csv";
		
		startSimulator();
	}
	
	/**
	 * Measures how long it takes to run multiple requests with one elevator.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testOneElevatorMultipleRequests() throws InterruptedException {
		System.out.println("\n----------IntegrationTest.testOneElevatorMultipleRequests----------\n");

		numFloors = 5;
		numElevators = 1;
		inputFileName = "src/ElevatorSimulatorTest/TestFiles/elevator_test-multiple_requests.csv";
		
		startSimulator();
	}
	
	/**
	 * Measures how long it takes to run multiple requests with multiple elevators.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testMultipleElevatorsMultipleRequests() throws InterruptedException {
		System.out.println("\n----------IntegrationTest.testMultipleElevatorsMultipleRequests----------\n");

		numFloors = 5;
		numElevators = 2;
		inputFileName = "src/ElevatorSimulatorTest/TestFiles/elevator_test-multiple_requests.csv";
		
		startSimulator();
	}
	
	/**
	 * Measures how long it takes to run multiple requests with multiple elevators.
	 * With different error cases.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testMultipleElevatorsMultipleRequestsWithError() throws InterruptedException {
		System.out.println("\n----------IntegrationTest.testMultipleElevatorsMultipleRequestsWithError----------\n");

		numFloors = 5;
		numElevators = 2;
		inputFileName = "src/ElevatorSimulatorTest/TestFiles/elevator_test-multiple_requests_with_error.csv";
		
		startSimulator();
	}
	
	
	/**
	 * Measures how long it takes to complete 22 requests with 4 elevators.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testOverloadSystem() throws InterruptedException {
		System.out.println("\n----------IntegrationTest.testOverloadSystem----------\n");

		numFloors = 22;
		numElevators = 4;
		inputFileName = "src/ElevatorSimulatorTest/TestFiles/elevator_test-stress.csv";
		
		startSimulator();
	}
	
	/**
	 * Measures how long it takes to complete 22 requests with 4 elevators.
	 * With different error cases.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testOverloadSystemWithError() throws InterruptedException {
		System.out.println("\n----------IntegrationTest.testOverloadSystemWithError----------\n");

		numFloors = 22;
		numElevators = 4;
		inputFileName = "src/ElevatorSimulatorTest/TestFiles/elevator_test-stress_with_error.csv";
		
		startSimulator();
	}
	
	/**
	 * Starts the simulator and waits for it to finish.
	 * 
	 * @throws InterruptedException
	 */
	private void startSimulator() throws InterruptedException{
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

		System.out.println("Time to complete all the requests: " + (endTime - startTime));

		floorServerRPC.join();
		elevatorServerRPC.join();
	}
}

