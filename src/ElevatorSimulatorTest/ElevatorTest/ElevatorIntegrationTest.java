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
 * Tests the general flow for the elevator.
 * Confirms the various states that the elevator goes through under various circumstances.
 * 
 * @author Sarah Chow
 * @author Bardia Parmoun
 * @author Kyra Lothrop
 *
 */
public class ElevatorIntegrationTest {
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
		Thread.currentThread().setName("ELEVATOR INTEGRATION TEST THREAD");
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
	 * The unit test depicts the receiving of a message from the scheduler to the
	 * elevator to move from floor 4 to 1. Confirms the expected environments of
	 * each state (DOORS_OPENED, PICKUP, ARRIVE). Sends kill message at the end to
	 * terminate thread.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testOneElevatorRequest() throws InterruptedException {
		System.out.println("\n----------IntegrationTest.testOneElevatorRequest----------\n");

		assertNotNull(elevator);
		assertEquals(1, elevator.getFloorNumber());
		assertEquals(ElevatorState.POLL, elevator.getState());

		// Starts up the elevator thread.
		Thread elevatorThread = new Thread(elevator, "ELEVATOR THREAD");
		elevatorThread.start();

		// Assigns the request to the elevator.
		Message message = new RequestElevatorMessage(new Date(), 4, DirectionType.DOWN, 1, 0, null);
		queue.replyToElevator(message, ELEVATOR_ID);
		
		boolean shouldRun = true;
		boolean canKill = false;

		// Parameters used to validate to test elevator states.
		int[] expectedFloors = {1, 2, 3, 4, 3, 2, 1};
		int floorIterator = 0;
		int numDoorOpen = 0;
		int numDoorClosed = 0;
		int numBoarding = 0;

		while (shouldRun) {
			Message newMessage = serverRPC.getCurrentMessage();
			
			if (newMessage != null) {
				// Confirm the door open messages.
				if (newMessage.getType() == MessageType.DOORS_OPENED) {
					DoorOpenedMessage doorsOpenMessage = (DoorOpenedMessage) newMessage;
					
					assertEquals(DirectionType.DOWN, doorsOpenMessage.getDirection());
					assertEquals(expectedFloors[floorIterator], doorsOpenMessage.getArrivedFloor());
					
					if (doorsOpenMessage.getStopType() == StopType.DROPOFF) {						
						canKill = true;
					}
				} else if (newMessage.getType() == MessageType.UPDATE_ELEVATOR_INFO) {
					ElevatorInfo elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
					
					// The update caused the floor to change
					if (elevatorInfo.getFloorNumber() != expectedFloors[floorIterator]){
						floorIterator++;
					}
					
					// Confirm individual states.
					if (elevatorInfo.getState() == ElevatorState.ARRIVED) {
						assertEquals(expectedFloors[floorIterator], elevatorInfo.getFloorNumber());
					} else if (elevatorInfo.getState() == ElevatorState.OPEN) {
						numDoorOpen++;
					} else if (elevatorInfo.getState() == ElevatorState.BOARDING) {
						numBoarding++;
					} else if (elevatorInfo.getState() == ElevatorState.CLOSE) {
						numDoorClosed++;
						
						// dropped off the last request.
						if (canKill) {
							shouldRun = false;
						}
					}
				}
			}
		}
		// Kills the elevator thread.
		queue.replyToElevator(new KillMessage(SenderType.FLOOR, new Date()), ELEVATOR_ID);

		// Wait for the elevator thread to die.
		elevatorThread.join();
		
		// Check the number of states the elevator went through.
		assertEquals(2, numDoorOpen);
		assertEquals(2, numBoarding);
		assertEquals(2, numDoorClosed);
	}
	
	/**
	 * The unit test depicts the elevator picking up multiple requests at once.
	 * It confirms that the elevator will travel in the most efficient flow.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testMultipleElevatorRequest() throws InterruptedException {
		System.out.println("\n----------IntegrationTest.testMultipleElevatorRequest----------\n");

		assertNotNull(elevator);
		assertEquals(1, elevator.getFloorNumber());
		assertEquals(ElevatorState.POLL, elevator.getState());

		// Starts up the elevator thread.
		Thread elevatorThread = new Thread(elevator, "ELEVATOR THREAD");
		elevatorThread.start();

		// Assigns the requests to the elevator.
		// The elevator should have the following flow
		// Pickup 4 -> Pickup and Dropoff at 2 -> Dropoff at 1.
		Message message = new RequestElevatorMessage(new Date(), 4, DirectionType.DOWN, 2, 0, null);
		queue.replyToElevator(message, ELEVATOR_ID);
		
		message = new RequestElevatorMessage(new Date(), 2, DirectionType.DOWN, 1, 0, null);
		queue.replyToElevator(message, ELEVATOR_ID);
		
		boolean shouldRun = true;
		boolean canKill = false;

		// Parameters used to validate to test elevator states.
		int[] expectedFloors = {1, 2, 3, 4, 3, 2, 1};
		int floorIterator = 0;
		int numDoorOpen = 0;
		int numDoorClosed = 0;
		int numBoarding = 0;

		while (shouldRun) {
			Message newMessage = serverRPC.getCurrentMessage();
			
			if (newMessage != null) {
				// Confirm the door open messages.
				if (newMessage.getType() == MessageType.DOORS_OPENED) {
					DoorOpenedMessage doorsOpenMessage = (DoorOpenedMessage) newMessage;
					
					assertEquals(DirectionType.DOWN, doorsOpenMessage.getDirection());
					assertEquals(expectedFloors[floorIterator], doorsOpenMessage.getArrivedFloor());
					
					if (doorsOpenMessage.getStopType() == StopType.DROPOFF) {						
						canKill = true;
					}
				} else if (newMessage.getType() == MessageType.UPDATE_ELEVATOR_INFO) {
					ElevatorInfo elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
					
					// The update caused the floor to change
					if (elevatorInfo.getFloorNumber() != expectedFloors[floorIterator]){
						floorIterator++;
					}
					
					// Confirm individual states.
					if (elevatorInfo.getState() == ElevatorState.ARRIVED) {
						assertEquals(expectedFloors[floorIterator], elevatorInfo.getFloorNumber());
					} else if (elevatorInfo.getState() == ElevatorState.OPEN) {
						numDoorOpen++;
					} else if (elevatorInfo.getState() == ElevatorState.BOARDING) {
						numBoarding++;
					} else if (elevatorInfo.getState() == ElevatorState.CLOSE) {
						numDoorClosed++;
						
						// dropped off the last request.
						if (canKill) {
							shouldRun = false;
						}
					}
				}
			}
		}
		// Kills the elevator thread.
		queue.replyToElevator(new KillMessage(SenderType.FLOOR, new Date()), ELEVATOR_ID);

		// Wait for the elevator thread to die.
		elevatorThread.join();
		
		// Check the number of states the elevator went through.
		assertEquals(3, numDoorOpen);
		assertEquals(3, numBoarding);
		assertEquals(3, numDoorClosed);
	}
	
	/**
	 * The unit test depicts the elevator picking up a request with the same pickup and destination.
	 * It confirms that the elevator will proceed as expected.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testElevatorRequestSameFloor() throws InterruptedException {
		System.out.println("\n----------IntegrationTest.testElevatorRequestSameFloor----------\n");

		assertNotNull(elevator);
		assertEquals(1, elevator.getFloorNumber());
		assertEquals(ElevatorState.POLL, elevator.getState());

		// Starts up the elevator thread.
		Thread elevatorThread = new Thread(elevator, "ELEVATOR THREAD");
		elevatorThread.start();

		// Assigns the requests to the elevator.
		Message message = new RequestElevatorMessage(new Date(), 1, DirectionType.DOWN, 1, 0, null);
		queue.replyToElevator(message, ELEVATOR_ID);
		
		boolean shouldRun = true;
		boolean canKill = false;

		// Parameters used to validate to test elevator states.
		int[] expectedFloors = {1,1};
		int floorIterator = 0;
		int numDoorOpen = 0;
		int numDoorClosed = 0;
		int numBoarding = 0;

		while (shouldRun) {
			Message newMessage = serverRPC.getCurrentMessage();
			
			if (newMessage != null) {
				// Confirm the door open messages.
				if (newMessage.getType() == MessageType.DOORS_OPENED) {
					DoorOpenedMessage doorsOpenMessage = (DoorOpenedMessage) newMessage;
					
					assertEquals(DirectionType.DOWN, doorsOpenMessage.getDirection());
					assertEquals(expectedFloors[floorIterator], doorsOpenMessage.getArrivedFloor());
					
					if (doorsOpenMessage.getStopType() == StopType.PICKUP_AND_DROPOFF) {						
						canKill = true;
					}
				} else if (newMessage.getType() == MessageType.UPDATE_ELEVATOR_INFO) {
					ElevatorInfo elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
					
					// The update caused the floor to change
					if (elevatorInfo.getFloorNumber() != expectedFloors[floorIterator]){
						floorIterator++;
					}
					
					// Confirm individual states.
					if (elevatorInfo.getState() == ElevatorState.ARRIVED) {
						assertEquals(expectedFloors[floorIterator], elevatorInfo.getFloorNumber());
					} else if (elevatorInfo.getState() == ElevatorState.OPEN) {
						numDoorOpen++;
					} else if (elevatorInfo.getState() == ElevatorState.BOARDING) {
						numBoarding++;
					} else if (elevatorInfo.getState() == ElevatorState.CLOSE) {
						numDoorClosed++;
						
						// dropped off the last request.
						if (canKill) {
							shouldRun = false;
						}
					}
				}
			}
		}
		// Kills the elevator thread.
		queue.replyToElevator(new KillMessage(SenderType.FLOOR, new Date()), ELEVATOR_ID);

		// Wait for the elevator thread to die.
		elevatorThread.join();
		
		// Check the number of states the elevator went through.
		assertEquals(1, numDoorOpen);
		assertEquals(1, numBoarding);
		assertEquals(1, numDoorClosed);
	}
	
	/**
	 * Tests the elevator in the presence of a transient fault.
	 * Confirms the elevator still goes through its request as expect.
	 * Confirms the elevator did an extra boarding state.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testElevatorTransientFault() throws InterruptedException {
		System.out.println("\n----------IntegrationTest.testElevatorTransientFault----------\n");

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
		boolean canKill = false;

		// Parameters used to validate to test elevator states.
		int[] expectedFloors = {1, 2};
		int floorIterator = 0;
		int numDoorOpen = 0;
		int numDoorClosed = 0;
		int numBoarding = 0;
		int numDoorInterrupt = 0;

		while (shouldRun) {
			Message newMessage = serverRPC.getCurrentMessage();
			
			if (newMessage != null) {
				// Confirm the door open messages.
				if (newMessage.getType() == MessageType.DOORS_OPENED) {
					DoorOpenedMessage doorsOpenMessage = (DoorOpenedMessage) newMessage;
					
					assertEquals(DirectionType.DOWN, doorsOpenMessage.getDirection());
					assertEquals(expectedFloors[floorIterator], doorsOpenMessage.getArrivedFloor());
					
					if (doorsOpenMessage.getStopType() == StopType.DROPOFF) {						
						canKill = true;
					}
				} else if (newMessage.getType() == MessageType.UPDATE_ELEVATOR_INFO) {
					ElevatorInfo elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
					
					// The update caused the floor to change
					if (elevatorInfo.getFloorNumber() != expectedFloors[floorIterator]){
						floorIterator++;
					}
					
					// Confirm individual states.
					if (elevatorInfo.getState() == ElevatorState.ARRIVED) {
						assertEquals(expectedFloors[floorIterator], elevatorInfo.getFloorNumber());
					} else if (elevatorInfo.getState() == ElevatorState.OPEN) {
						numDoorOpen++;
					} else if (elevatorInfo.getState() == ElevatorState.BOARDING) {
						numBoarding++;
					} else if (elevatorInfo.getState() == ElevatorState.DOOR_INTERRUPT) {
						numDoorInterrupt++; 
					} else if (elevatorInfo.getState() == ElevatorState.CLOSE) {
						numDoorClosed++;
						
						// dropped off the last request.
						if (canKill) {
							shouldRun = false;
						}
					}
				}
			}
		}
		// Kills the elevator thread.
		queue.replyToElevator(new KillMessage(SenderType.FLOOR, new Date()), ELEVATOR_ID);

		// Wait for the elevator thread to die.
		elevatorThread.join();
				
		// Check the number of states the elevator went through.
		assertEquals(2, numDoorOpen);
		assertEquals(3, numBoarding); // one more boarding state due to the transient fault.
		assertEquals(1, numDoorInterrupt);
		assertEquals(2, numDoorClosed);
	}
	
	/**
	 * Tests the elevator in the presence of a system fault.
	 * Confirms the elevator cannot complete the request.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testElevatorSystemFault() throws InterruptedException {
		System.out.println("\n----------IntegrationTest.testElevatorSystemFault----------\n");

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
		int[] expectedFloors = {1, 2};
		int floorIterator = 0;

		while (shouldRun) {
			Message newMessage = serverRPC.getCurrentMessage();
			
			if (newMessage != null) {
				// Confirm the door open messages.
				if (newMessage.getType() == MessageType.DOORS_OPENED) {
					DoorOpenedMessage doorsOpenMessage = (DoorOpenedMessage) newMessage;
					
					// Picked up the request.
					if (doorsOpenMessage.getStopType() == StopType.PICKUP) {						
						assertEquals(DirectionType.DOWN, doorsOpenMessage.getDirection());
						assertEquals(expectedFloors[floorIterator], doorsOpenMessage.getArrivedFloor());
					}
				} else if (newMessage.getType() == MessageType.UPDATE_ELEVATOR_INFO) {
					ElevatorInfo elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
					
					// The update caused the floor to change
					if (elevatorInfo.getFloorNumber() != expectedFloors[floorIterator]){
						floorIterator++;
					}
					
					// Confirm individual states.
					if (elevatorInfo.getState() == ElevatorState.ARRIVED) {
						assertEquals(expectedFloors[floorIterator], elevatorInfo.getFloorNumber());
					}
				} else if (newMessage.getType() == MessageType.ELEVATOR_STUCK) {
					ElevatorStuckMessage stuckMessage = (ElevatorStuckMessage) newMessage;
					
					// The current trip was sent to the scheduler.
					assertEquals(1, stuckMessage.getCurrentTrips().size());
					assertEquals(1, stuckMessage.getCurrentTrips().get(0).getPickup());
					assertEquals(2, stuckMessage.getCurrentTrips().get(0).getDropoff());
					
					// No more remaining trips.
					assertEquals(0, stuckMessage.getRemainingTrips().size());
					
					shouldRun = false;
				}
			} 
		}

		// Wait for the elevator thread to die.
		elevatorThread.join();
		
		// Assert the elevator's parent state is no longer operational.
		assertTrue(elevator.getParentState() == ElevatorState.ELEVATOR_STUCK);
	}
}
