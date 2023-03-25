package ElevatorSimulatorTest.FloorTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ElevatorSimulator.Floor.Floor;
import ElevatorSimulator.Messages.DirectionType;
import ElevatorSimulator.Messages.DoorOpenedMessage;
import ElevatorSimulator.Messages.Message;
import ElevatorSimulator.Messages.MessageType;
import ElevatorSimulator.Messages.StartMessage;
import ElevatorSimulator.Messages.StopType;
import ElevatorSimulator.Messaging.MessageQueue;
import ElevatorSimulator.Messaging.ServerRPC;
import ElevatorSimulator.Scheduler.Scheduler;

/**
 * The unit tests for the floor subsystem.
 * 
 * @author Kyra Lothrop
 * @author Sarah Chow
 * @author Bardia Parmoun
 *
 */
public class FloorTest{		
	// The message queue used to keep track of the messages.
	private MessageQueue queue;
	
	// Keeps track of the floor object.
	private Floor floor;
	
	// The server RPC thread running in the background to receive UDP messages.
	private Thread serverRPCThread;
	
	// The keeps track of the server RPC used to receive UDP messages.
	private ServerRPC serverRPC;

	// Test constants.
	public static int NUM_FLOORS = 4;
	
	public static String FILEPATH = "src/ElevatorSimulatorTest/TestFiles/elevator_test-1.csv";
	
	private int PICKUP_FLOOR = 1;
	
	private int DESTINATION_FLOOR = 4;

	
	/**
	 * Initializes the server RPC thread in the background.
	 */
	@BeforeEach
	void init() {	
		Thread.currentThread().setName("FLOOR TEST THREAD");

		queue = new MessageQueue();
		serverRPC = new ServerRPC(queue, Scheduler.FLOOR_PORT);		
		
		// Creates a floor.
		floor = new Floor(FILEPATH, NUM_FLOORS);

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
	 * The unit test depicts the sending of a message from the
	 * floor to the scheduler and back from the scheduler to floor
	 * as a confirmation message. Sends kill message at the end 
	 * to terminate thread.
	 */
	@Test
	void testOneFloorRequest() {
		System.out.println("\n----------testOneFloorRequest----------\n");
		
		// Sends the start message to the floor.
		queue.replyToFloor(new StartMessage());

		// Starts the floor thread.
		
		Thread floorThread = new Thread(floor, "FLOOR");
		
		floorThread.start();		
		
		
		boolean shouldRun = true;
		
		// No dropoffs at first.
		assertEquals(0, floor.getDropoffsList().size());

		while(shouldRun) {
			Message newMessage = queue.pop();
			

			if (newMessage.getType() == MessageType.REQUEST) {
				assertEquals(0, floor.getElevatorRequestsList().size());

				DoorOpenedMessage pickupMessage = new DoorOpenedMessage(new Date(), PICKUP_FLOOR, StopType.PICKUP, DirectionType.UP, 1, 0);
				
				queue.replyToFloor(pickupMessage);
				
				assertEquals(0, floor.getElevatorRequestsList().size());
				
				DoorOpenedMessage dropoffMessage = new DoorOpenedMessage(new Date(), DESTINATION_FLOOR, StopType.DROPOFF, DirectionType.UP, 0, 1);

				queue.replyToFloor(dropoffMessage);
				
			} else if (newMessage.getType() == MessageType.KILL) {
				// Only killed when no more dropoffs.
				assertEquals(0, floor.getElevatorRequestsList().size());
				assertEquals(0, floor.getDropoffsList().size());
				
				shouldRun = false;
			}
		}
	}
}
