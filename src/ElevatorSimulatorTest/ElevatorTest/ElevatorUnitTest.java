package ElevatorSimulatorTest.ElevatorTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import ElevatorSimulator.Simulator;
import ElevatorSimulator.Elevator.Elevator;
import ElevatorSimulator.Elevator.ElevatorInfo;
import ElevatorSimulator.Elevator.ElevatorState;
import ElevatorSimulator.Messages.*;
import ElevatorSimulator.Messaging.ConnectionType;
import ElevatorSimulator.Messaging.MessageQueue;
import ElevatorSimulator.Scheduler.Scheduler;
import ElevatorSimulatorTest.MockServerRPC;

/**
 * Unit Tests for the elevator.
 * Testing individual functionalites of the elevator.
 * 
 * @author Bardia Parmoun
 *
 */
public class ElevatorUnitTest {
	// The message queue used to keep track of the messages.
	private MessageQueue queue;

	// Keeps track of the elevator object.
	private Elevator elevator;

	// The server RPC thread running in the background to receive UDP messages.
	private Thread serverRPCThread;

	// The keeps track of the server RPC used to receive UDP messages.
	private MockServerRPC serverRPC;

	// Test constants
	public static int ELEVATOR_ID = 0;

	public static int NUM_FLOORS = 4;

	/**
	 * Initializes the server RPC thread in the background.
	 */
	@BeforeEach
	public void init() {
		Thread.currentThread().setName("ELEVATOR UNIT TEST THREAD");
		Simulator.DEBUG_MODE = false;

		queue = new MessageQueue();
		serverRPC = new MockServerRPC(queue, Scheduler.ELEVATOR_PORT);

		serverRPCThread = new Thread(serverRPC, "SERVER RPC THREAD");
		serverRPCThread.start();
		
		// Creates an elevator.
		elevator = new Elevator(ELEVATOR_ID, NUM_FLOORS, ConnectionType.LOCAL);

		// Adds the elevator to the queue.
		ElevatorInfo info = new ElevatorInfo(elevator.getDirection(), elevator.getParentState(), elevator.getState(),
				elevator.getFloorNumber(), elevator.getID(), elevator.getNumTrips());
		queue.addElevator(ELEVATOR_ID, info);
	}

	/**
	 * Terminates the server RPC thread and closes the socket.
	 */
	@AfterEach
	public void cleanup() {
		try {
			serverRPC.kill();
			serverRPCThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests to see if the elevator stays moving for the proper time.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testElevatorMoving() throws InterruptedException {
		System.out.println("\n----------UnitTest.testElevatorMoving----------\n");

		assertNotNull(elevator);
		assertEquals(1, elevator.getFloorNumber());
		assertEquals(ElevatorState.POLL, elevator.getState());

		// Starts up the elevator thread.
		Thread elevatorThread = new Thread(elevator, "ELEVATOR THREAD");
		elevatorThread.start();

		// Assigns the request to the elevator.
		Message message = new RequestElevatorMessage(new Date(), 1, DirectionType.UP, 2, 0, null);
		queue.replyToElevator(message, ELEVATOR_ID);
		
		boolean shouldRun = true;

		while (shouldRun) {
			Message newMessage = serverRPC.getCurrentMessage();
			
			// Waits for the elevator to start moving
			if (newMessage.getType() == MessageType.UPDATE_ELEVATOR_INFO) {
				ElevatorInfo elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
				
				if (elevatorInfo.getState() == ElevatorState.MOVING) {
					shouldRun = false; // it is starting to move
				}
			}
		}

		long startTime = System.currentTimeMillis();
		
		// Wait to get the door interrupt message.
		Message newMessage = serverRPC.getCurrentMessage();
		assertEquals(MessageType.UPDATE_ELEVATOR_INFO, newMessage.getType());
		
		ElevatorInfo elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
		assertEquals(ElevatorState.ARRIVED, elevatorInfo.getState());

		long endTime = System.currentTimeMillis();

		// Confirm the elevator was moving for the proper amount.
		assertEquals(Elevator.MOVE_DELAY, endTime - startTime, 1000);
		
		// Kills the elevator thread.
		queue.replyToElevator(new KillMessage(SenderType.FLOOR, new Date()), ELEVATOR_ID);
		
		// Wait for the elevator thread to die.
		elevatorThread.join();
	}

		
	/**
	 * Tests to see if the elevator opens its doors after a specified time.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testElevatorOpenDoor() throws InterruptedException {
		System.out.println("\n----------UnitTest.testElevatorOpenDoor----------\n");

		assertNotNull(elevator);
		assertEquals(1, elevator.getFloorNumber());
		assertEquals(ElevatorState.POLL, elevator.getState());

		// Starts up the elevator thread.
		Thread elevatorThread = new Thread(elevator, "ELEVATOR THREAD");
		elevatorThread.start();

		// Assigns the request to the elevator.
		Message message = new RequestElevatorMessage(new Date(), 1, DirectionType.DOWN, 1, 0, null);
		queue.replyToElevator(message, ELEVATOR_ID);
		
		boolean shouldRun = true;

		while (shouldRun) {
			Message newMessage = serverRPC.getCurrentMessage();
			
			if (newMessage != null) {
				// Waits for the elevator to open its doors.
				if (newMessage.getType() == MessageType.DOORS_OPENED) {
					shouldRun = false; // it is starting to open doors.
				}
			}
		}

		long startTime = System.currentTimeMillis();
		
		// Wait to get the door interrupt message.
		Message newMessage = serverRPC.getCurrentMessage();
		assertEquals(MessageType.UPDATE_ELEVATOR_INFO, newMessage.getType());
		
		ElevatorInfo elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
		assertEquals(ElevatorState.BOARDING, elevatorInfo.getState());

		long endTime = System.currentTimeMillis();

		// Confirm the door was held open for the proper amount.
		assertEquals(Elevator.DOOR_DELAY, endTime - startTime, 1000);
		
		// Kills the elevator thread.
		queue.replyToElevator(new KillMessage(SenderType.FLOOR, new Date()), ELEVATOR_ID);
		
		// Wait for the elevator thread to die.
		elevatorThread.join();
	}

	/**
	 * Tests to see if the elevator closes its doors after a specified time.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testElevatorCloseDoor() throws InterruptedException {
		System.out.println("\n----------UnitTest.testElevatorCloseDoor----------\n");

		assertNotNull(elevator);
		assertEquals(1, elevator.getFloorNumber());
		assertEquals(ElevatorState.POLL, elevator.getState());

		// Starts up the elevator thread.
		Thread elevatorThread = new Thread(elevator, "ELEVATOR THREAD");
		elevatorThread.start();

		// Assigns the request to the elevator.
		Message message = new RequestElevatorMessage(new Date(), 1, DirectionType.DOWN, 1, 0, null);
		queue.replyToElevator(message, ELEVATOR_ID);
		
		boolean shouldRun = true;

		while (shouldRun) {
			Message newMessage = serverRPC.getCurrentMessage();
			
			if (newMessage != null) {
				// Waits for the elevator to arrive at the floor.
				if (newMessage.getType() == MessageType.UPDATE_ELEVATOR_INFO) {
					ElevatorInfo elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
					
					if (elevatorInfo.getState() == ElevatorState.CLOSE) {
						shouldRun = false; // it is starting to close doors.
					}
				}
			}
		}

		long startTime = System.currentTimeMillis();
		
		// Wait to get the door interrupt message.
		Message newMessage = serverRPC.getCurrentMessage();
		assertEquals(MessageType.UPDATE_ELEVATOR_INFO, newMessage.getType());
		
		ElevatorInfo elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
		assertEquals(ElevatorState.POLL, elevatorInfo.getState());

		long endTime = System.currentTimeMillis();

		// Confirm the door was held open for the proper amount.
		assertEquals(Elevator.DOOR_DELAY, endTime - startTime, 1000);
		
		// Kills the elevator thread.
		queue.replyToElevator(new KillMessage(SenderType.FLOOR, new Date()), ELEVATOR_ID);
		
		// Wait for the elevator thread to die.
		elevatorThread.join();
	}
	
	/**
	 * Tests to see if the elevator boards the passengers within a specified time.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testElevatorBoarding() throws InterruptedException {
		System.out.println("\n----------UnitTest.testElevatorBoarding----------\n");

		assertNotNull(elevator);
		assertEquals(1, elevator.getFloorNumber());
		assertEquals(ElevatorState.POLL, elevator.getState());

		// Starts up the elevator thread.
		Thread elevatorThread = new Thread(elevator, "ELEVATOR THREAD");
		elevatorThread.start();

		// Assigns the request to the elevator.
		Message message = new RequestElevatorMessage(new Date(), 1, DirectionType.DOWN, 1, 0, null);
		queue.replyToElevator(message, ELEVATOR_ID);
		
		boolean shouldRun = true;

		while (shouldRun) {
			Message newMessage = serverRPC.getCurrentMessage();
			
			if (newMessage != null) {
				// Waits for the elevator to arrive at the floor.
				if (newMessage.getType() == MessageType.UPDATE_ELEVATOR_INFO) {
					ElevatorInfo elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
					
					if (elevatorInfo.getState() == ElevatorState.BOARDING) {
						shouldRun = false; // it is starting to board passengers.
					}
				}
			}
		}

		long startTime = System.currentTimeMillis();
		
		// Wait to get the door interrupt message.
		Message newMessage = serverRPC.getCurrentMessage();
		assertEquals(MessageType.UPDATE_ELEVATOR_INFO, newMessage.getType());
		
		ElevatorInfo elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
		assertEquals(ElevatorState.CLOSE, elevatorInfo.getState());

		long endTime = System.currentTimeMillis();

		// Confirm the door was held open for the proper amount.
		assertEquals(Elevator.BOARDING_DELAY, endTime - startTime, 1000);
		
		// Kills the elevator thread.
		queue.replyToElevator(new KillMessage(SenderType.FLOOR, new Date()), ELEVATOR_ID);
		
		// Wait for the elevator thread to die.
		elevatorThread.join();
	}
	
	/**
	 * Confirms the elevator goes to DOOR_INTERRUPT within the specified time.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testElevatorTransientFault() throws InterruptedException {
		System.out.println("\n----------UnitTest.testElevatorTransientFault----------\n");

		assertNotNull(elevator);
		assertEquals(1, elevator.getFloorNumber());
		assertEquals(ElevatorState.POLL, elevator.getState());

		// Starts up the elevator thread.
		Thread elevatorThread = new Thread(elevator, "ELEVATOR THREAD");
		elevatorThread.start();

		// Assigns the request to the elevator.
		Message message = new RequestElevatorMessage(new Date(), 1, DirectionType.DOWN, 2, 1000, ErrorType.DOOR_INTERRUPT);
		queue.replyToElevator(message, ELEVATOR_ID);
		
		boolean shouldRun = true;

		// Parameters used to validate to test elevator states.
		int[] expectedFloors = {1};
		int floorIterator = 0;

		while (shouldRun) {
			Message newMessage = serverRPC.getCurrentMessage();
			
			if (newMessage != null) {
				// Confirm the door open messages.
				if (newMessage.getType() == MessageType.DOORS_OPENED) {
					DoorOpenedMessage doorsOpenMessage = (DoorOpenedMessage) newMessage;
					
					if (doorsOpenMessage.getStopType() == StopType.PICKUP) {						
						assertEquals(DirectionType.DOWN, doorsOpenMessage.getDirection());
						assertEquals(expectedFloors[floorIterator], doorsOpenMessage.getArrivedFloor());
					}
				} else if (newMessage.getType() == MessageType.UPDATE_ELEVATOR_INFO) {
					ElevatorInfo elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
					
					if (elevatorInfo.getState() == ElevatorState.BOARDING) {
						shouldRun = false; // Boarded the faulty request.
					}
				}
			}
		}

		long startTime = System.currentTimeMillis();
		
		// Wait to get the door interrupt message.
		Message newMessage = serverRPC.getCurrentMessage();
		assertEquals(MessageType.UPDATE_ELEVATOR_INFO, newMessage.getType());
		
		ElevatorInfo elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
		assertEquals(ElevatorState.DOOR_INTERRUPT, elevatorInfo.getState());

		long endTime = System.currentTimeMillis();

		// Confirm the door was interrupted within 1 second of picking up the faulty request.
		assertEquals(1000, endTime - startTime, 1000);
		
		startTime = System.currentTimeMillis();
		
		// Confirm the elevator went back to boarding.
		newMessage = serverRPC.getCurrentMessage();
		assertEquals(MessageType.UPDATE_ELEVATOR_INFO, newMessage.getType());
		
		elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
		assertEquals(ElevatorState.BOARDING, elevatorInfo.getState());
		
		endTime = System.currentTimeMillis();
		
		// Confirm the door was interrupted within the interrupt delay of picking up the faulty request.
		assertEquals(Elevator.DOOR_INTERRUPT_DELAY, endTime - startTime, 1000);
		
		// Kills the elevator thread.
		queue.replyToElevator(new KillMessage(SenderType.FLOOR, new Date()), ELEVATOR_ID);
		
		// Wait for the elevator thread to die.
		elevatorThread.join();

	}
	
	/**
	 * Confirms the elevator crashes within a specified time after picking up a faulty request.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testElevatorSystemFault() throws InterruptedException {
		System.out.println("\n----------UnitTest.testElevatorSystemFault----------\n");

		assertNotNull(elevator);
		assertEquals(1, elevator.getFloorNumber());
		assertEquals(ElevatorState.POLL, elevator.getState());

		// Starts up the elevator thread.
		Thread elevatorThread = new Thread(elevator, "ELEVATOR THREAD");
		elevatorThread.start();

		// Assigns the request to the elevator.
		Message message = new RequestElevatorMessage(new Date(), 1, DirectionType.DOWN, 2, 1000, ErrorType.ELEVATOR_STUCK);
		queue.replyToElevator(message, ELEVATOR_ID);
		
		boolean shouldRun = true;

		// Parameters used to validate to test elevator states.
		int[] expectedFloors = {1};
		int floorIterator = 0;

		while (shouldRun) {
			Message newMessage = serverRPC.getCurrentMessage();
			
			if (newMessage != null) {
				// Confirm the door open messages.
				if (newMessage.getType() == MessageType.DOORS_OPENED) {
					DoorOpenedMessage doorsOpenMessage = (DoorOpenedMessage) newMessage;
					
					if (doorsOpenMessage.getStopType() == StopType.PICKUP) {						
						assertEquals(DirectionType.DOWN, doorsOpenMessage.getDirection());
						assertEquals(expectedFloors[floorIterator], doorsOpenMessage.getArrivedFloor());
						
						shouldRun = false; // Picked up the faulty request.
					}
				} 
			}
		}

		long startTime = System.currentTimeMillis();
		
		// Wait for the elevator thread to die.
		elevatorThread.join();
		
		long endTime = System.currentTimeMillis();

		// Confirm the elevator was killed within 1 second of picking up the faulty request.
		assertEquals(1000, endTime - startTime, 1000);
	}
}
