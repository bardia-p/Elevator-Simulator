package ElevatorSimulatorTest.TestTiming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

import org.junit.jupiter.api.*;

import ElevatorSimulator.Elevator.*;
import ElevatorSimulator.Messages.*;
import ElevatorSimulator.Messaging.MessageQueue;
import ElevatorSimulator.Scheduler.Scheduler;
import ElevatorSimulatorTest.MockServerRPC;

/**
 * Test the elevator timing measurements.
 * 
 * @author Kyra Lothrop
 * @author Bardia Parmoun
 */

public class ElevatorTiming {
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

		queue = new MessageQueue();
		serverRPC = new MockServerRPC(queue, Scheduler.ELEVATOR_PORT);

		serverRPCThread = new Thread(serverRPC, "SERVER RPC THREAD");
		serverRPCThread.start();
		
		// Creates an elevator.
		elevator = new Elevator(ELEVATOR_ID, NUM_FLOORS);

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
		System.out.println("\n----------TestTiming.testElevatorMoving----------\n");

		assertNotNull(elevator);
		assertEquals(1, elevator.getFloorNumber());
		assertEquals(ElevatorState.POLL, elevator.getState());

		// Starts up the elevator thread.
		Thread elevatorThread = new Thread(elevator, "ELEVATOR THREAD");
		elevatorThread.start();

		// Assigns the request to the elevator.
		Message message = new RequestElevatorMessage(new Date(), 5, DirectionType.DOWN, 1, 0, null);
		queue.replyToElevator(message, ELEVATOR_ID);
		
		boolean shouldRun = true;

		while (shouldRun) {
			Message newMessage = serverRPC.getCurrentMessage();
			
			// Confirm the door open messages.
            if (newMessage.getType() == MessageType.DOORS_OPENED) {
                DoorOpenedMessage doorsOpenMessage = (DoorOpenedMessage) newMessage;
                
                if (doorsOpenMessage.getStopType() == StopType.DROPOFF) {                        
                	shouldRun = false;
                }
            }
			// Waits for the elevator to start moving
            else if (newMessage.getType() == MessageType.UPDATE_ELEVATOR_INFO) {
				ElevatorInfo elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();

                if (elevatorInfo.getState() == ElevatorState.MOVING) {
					long startTime = System.currentTimeMillis();
					
					// Wait to get the door interrupt message.
					newMessage = serverRPC.getCurrentMessage();
					assertEquals(MessageType.UPDATE_ELEVATOR_INFO, newMessage.getType());
					
					elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
					assertEquals(ElevatorState.ARRIVED, elevatorInfo.getState());

					long endTime = System.currentTimeMillis();

					// Confirm the elevator was moving for the proper amount.
					assertEquals(Elevator.MOVE_DELAY, endTime - startTime, 1000);
					
					System.out.println("Move time: " + (endTime - startTime));
				}
			}
		}

		// Kills the elevator thread.
		queue.replyToElevator(new KillMessage(SenderType.FLOOR, new Date()), ELEVATOR_ID);
		
		// Wait for the elevator thread to die.
		elevatorThread.join();
	}
	
	/**
	 * Test the time to open the door.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testOpenDoor() throws InterruptedException {
		System.out.println("\n----------TestTiming.testOpenDoor----------\n");

		assertNotNull(elevator);
		assertEquals(1, elevator.getFloorNumber());
		assertEquals(ElevatorState.POLL, elevator.getState());

		// Starts up the elevator thread.
		Thread elevatorThread = new Thread(elevator, "ELEVATOR THREAD");
		elevatorThread.start();

		// Assigns the request to the elevator.
		Message message = new RequestElevatorMessage(new Date(), 5, DirectionType.DOWN, 1, 0, null);
		queue.replyToElevator(message, ELEVATOR_ID);
		
		boolean shouldRun = true;

		while (shouldRun) {
			Message newMessage = serverRPC.getCurrentMessage();
			
			// Confirm the door open messages.
            if (newMessage.getType() == MessageType.DOORS_OPENED) {
                DoorOpenedMessage doorsOpenMessage = (DoorOpenedMessage) newMessage;
                
                long startTime = System.currentTimeMillis();
				
				// Wait to get the door interrupt message.
				newMessage = serverRPC.getCurrentMessage();
				assertEquals(MessageType.UPDATE_ELEVATOR_INFO, newMessage.getType());
				
				ElevatorInfo elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
				assertEquals(ElevatorState.BOARDING, elevatorInfo.getState());

				long endTime = System.currentTimeMillis();

				// Confirm the elevator was moving for the proper amount.
				System.out.println("Door open time: " + (endTime - startTime));
				assertEquals(Elevator.DOOR_DELAY, endTime - startTime, 1000);
				
                if (doorsOpenMessage.getStopType() == StopType.DROPOFF) {                        
                	shouldRun = false;
                }
            }
		}

		// Kills the elevator thread.
		queue.replyToElevator(new KillMessage(SenderType.FLOOR, new Date()), ELEVATOR_ID);
		
		// Wait for the elevator thread to die.
		elevatorThread.join();
	}
	
	/**
	 * Test the time to close the doors.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testCloseDoor() throws InterruptedException {
		System.out.println("\n----------TestTiming.testCloseDoor----------\n");

		assertNotNull(elevator);
		assertEquals(1, elevator.getFloorNumber());
		assertEquals(ElevatorState.POLL, elevator.getState());

		// Starts up the elevator thread.
		Thread elevatorThread = new Thread(elevator, "ELEVATOR THREAD");
		elevatorThread.start();

		// Assigns the request to the elevator.
		Message message = new RequestElevatorMessage(new Date(), 5, DirectionType.DOWN, 1, 0, null);
		queue.replyToElevator(message, ELEVATOR_ID);
		
		boolean shouldRun = true;

		while (shouldRun) {
			Message newMessage = serverRPC.getCurrentMessage();
			
			// Confirm the door open messages.
            if (newMessage.getType() == MessageType.DOORS_OPENED) {
                DoorOpenedMessage doorsOpenMessage = (DoorOpenedMessage) newMessage;
                
                if (doorsOpenMessage.getStopType() == StopType.DROPOFF) {                        
                	shouldRun = false;
                }
            }
			// Waits for the elevator to start moving
            else if (newMessage.getType() == MessageType.UPDATE_ELEVATOR_INFO) {
				ElevatorInfo elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
                if (elevatorInfo.getState() == ElevatorState.CLOSE) {
					long startTime = System.currentTimeMillis();
					
					// Wait to get the door interrupt message.
					newMessage = serverRPC.getCurrentMessage();
					assertEquals(MessageType.UPDATE_ELEVATOR_INFO, newMessage.getType());
					
					elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
					assertEquals(ElevatorState.POLL, elevatorInfo.getState());

					long endTime = System.currentTimeMillis();

					// Confirm the elevator was moving for the proper amount.
					assertEquals(Elevator.DOOR_DELAY, endTime - startTime, 1000);
					
					System.out.println("Close door time: " + (endTime - startTime));
				}
			}
		}

		// Kills the elevator thread.
		queue.replyToElevator(new KillMessage(SenderType.FLOOR, new Date()), ELEVATOR_ID);
		
		// Wait for the elevator thread to die.
		elevatorThread.join();
	}
	
	/**
	 * Test the boarding time.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testBoarding() throws InterruptedException {
		System.out.println("\n----------TestTiming.testBoarding----------\n");

		assertNotNull(elevator);
		assertEquals(1, elevator.getFloorNumber());
		assertEquals(ElevatorState.POLL, elevator.getState());

		// Starts up the elevator thread.
		Thread elevatorThread = new Thread(elevator, "ELEVATOR THREAD");
		elevatorThread.start();

		// Assigns the request to the elevator.
		Message message = new RequestElevatorMessage(new Date(), 5, DirectionType.DOWN, 1, 0, null);
		queue.replyToElevator(message, ELEVATOR_ID);
		
		boolean shouldRun = true;

		while (shouldRun) {
			Message newMessage = serverRPC.getCurrentMessage();
			
			// Confirm the door open messages.
            if (newMessage.getType() == MessageType.DOORS_OPENED) {
                DoorOpenedMessage doorsOpenMessage = (DoorOpenedMessage) newMessage;
                
                if (doorsOpenMessage.getStopType() == StopType.DROPOFF) {                        
                	shouldRun = false;
                }
            }
			// Waits for the elevator to start moving
            else if (newMessage.getType() == MessageType.UPDATE_ELEVATOR_INFO) {
				ElevatorInfo elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
                if (elevatorInfo.getState() == ElevatorState.BOARDING) {
					long startTime = System.currentTimeMillis();
					
					// Wait to get the door interrupt message.
					newMessage = serverRPC.getCurrentMessage();
					assertEquals(MessageType.UPDATE_ELEVATOR_INFO, newMessage.getType());
					
					elevatorInfo = ((UpdateElevatorInfoMessage) newMessage).getInfo();
					assertEquals(ElevatorState.CLOSE, elevatorInfo.getState());

					long endTime = System.currentTimeMillis();

					// Confirm the elevator was moving for the proper amount.
					assertEquals(Elevator.BOARDING_DELAY, endTime - startTime, 1000);
					
					System.out.println("Time boarding delay: " + (endTime - startTime));
				}
			}
		}

		// Kills the elevator thread.
		queue.replyToElevator(new KillMessage(SenderType.FLOOR, new Date()), ELEVATOR_ID);
		
		// Wait for the elevator thread to die.
		elevatorThread.join();
	}
}