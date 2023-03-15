package ElevatorSimulatorTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import ElevatorSimulator.Elevator.Elevator;
import ElevatorSimulator.Elevator.ElevatorInfo;
import ElevatorSimulator.Elevator.ElevatorState;
import ElevatorSimulator.Messages.*;
import ElevatorSimulator.Messaging.MessageQueue;
import ElevatorSimulator.Messaging.ServerRPC;
import ElevatorSimulator.Scheduler.Scheduler;

/**
 * The unit tests for the elevator subsystem.
 * 
 * @author Sarah Chow
 * @author Bardia Parmoun
 * @author Kyra Lothrop
 *
 */
public class ElevatorTest {	
	// The message queue used to keep track of the messages.
	private MessageQueue queue;
	
	// Keeps track of the elevator object.
	private Elevator elevator;
	
	// The server RPC thread running in the background to receive UDP messages.
	private Thread serverRPCThread;
	
	// The keeps track of the server RPC used to receive UDP messages.
	private ServerRPC serverRPC;
	
	// Test constants
	public static int ELEVATOR_ID = 0;
	
	public static int NUM_FLOORS = 4;
	
	/**
	 * Initializes the server RPC thread in the background.
	 */
	@BeforeEach
	public void init() {
		Thread.currentThread().setName("ELEVATOR TEST THREAD");

		queue = new MessageQueue();
		serverRPC = new ServerRPC(queue, Scheduler.ELEVATOR_PORT);		
		
		// Creates an elevator.
		elevator = new Elevator(ELEVATOR_ID, NUM_FLOORS);
		
		
		serverRPCThread = new Thread(serverRPC, "SERVER RPC THREAD");
		serverRPCThread.start();
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
	 * The unit test depicts the receiving of a message from the
	 * scheduler to the elevator to move from floor 4 to 1.
	 * Confirms the expected environments of each 
	 * state (DOORS_OPENED, PICKUP, ARRIVE). Sends kill message
	 * at the end to terminate thread.
	 * @throws InterruptedException 
	 */
	@Test
	public void testOneElevatorRequest() throws InterruptedException {
		System.out.println("\n----------testOneElevatorRequest----------\n");
		
		// Adds the elevator to the queue.
		queue.addElevator(ELEVATOR_ID, new ElevatorInfo(elevator.getDirection(),
				elevator.getState(), elevator.getFloorNumber(), elevator.getID(), elevator.getNumTrips()));
		
		// Assigns the request to the elevator.
		Message message = new RequestElevatorMessage(new Date(), 4, DirectionType.DOWN, 1);
		queue.replyToElevator(message, ELEVATOR_ID);
		
		assertNotNull(elevator);
		assertEquals(1, elevator.getFloorNumber());
		assertEquals(ElevatorState.POLL, elevator.getState());
		
		// Starts up the elevator thread.
		Thread elevatorThread = new Thread(elevator, "ELEVATOR THREAD");
		elevatorThread.start();
		
		boolean shouldRun = true;
				
		int expectedFloor = 1;
		
		while(shouldRun) {
			Message newMessage = queue.pop();
			
			if (newMessage.getType() == MessageType.DOORS_OPENED) {
				DoorOpenedMessage doorsOpenMessage = (DoorOpenedMessage) newMessage;
				
				if (doorsOpenMessage.getStopType() == StopType.PICKUP) {
					assertEquals(ElevatorState.OPEN, elevator.getState());
					assertEquals(DirectionType.DOWN, doorsOpenMessage.getDirection());
					assertEquals(4, doorsOpenMessage.getArrivedFloor());
				} else {
					assertEquals(ElevatorState.OPEN, elevator.getState());
					assertEquals(DirectionType.DOWN, doorsOpenMessage.getDirection());
					assertEquals(1, doorsOpenMessage.getArrivedFloor());
					
					shouldRun = false;
				}
			} else if (newMessage.getType() == MessageType.ARRIVE) {
				if (elevator.getDirection() == DirectionType.UP) {
					expectedFloor++;
				} else {
					expectedFloor--;
				}
				assertEquals(expectedFloor, ((ArrivedElevatorMessage)newMessage).getArrivedFloor());
			}
		}
		
		// Kills the elevator thread.
		queue.replyToElevator(new KillMessage(SenderType.FLOOR, new Date()), ELEVATOR_ID);	
		
		elevatorThread.join();

	}
}
