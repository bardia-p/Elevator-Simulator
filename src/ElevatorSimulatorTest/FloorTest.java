package ElevatorSimulatorTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ElevatorSimulator.Floor.Floor;
import ElevatorSimulator.Messages.DirectionType;
import ElevatorSimulator.Messages.DoorOpenedMessage;
import ElevatorSimulator.Messages.Message;
import ElevatorSimulator.Messages.MessageType;
import ElevatorSimulator.Messages.StopType;
import ElevatorSimulator.Messaging.MessageQueue;

/**
 * The unit tests for the floor subsystem.
 * 
 * @author Kyra Lothrop
 * @author Sarah Chow
 *
 */
public class FloorTest {
	
	MessageQueue queue;
	
	public static int NUM_FLOORS = 4;
	
	public static String FILEPATH = "src/ElevatorSimulatorTest/TestFiles/elevator_test-1.csv";
	
	private boolean shouldRun;
	
	private int DESTINATION_FLOOR = 4;

	
	/**
	 * Creating a new instance of the mock scheduler before
	 * each new test.
	 */
	@BeforeEach
	void init() {
		queue = new MessageQueue();
		shouldRun = true;
	}
	
	/**
	 * The unit test depicts the sending of a message and a 
	 * kill message from the floor to the scheduler.
	 */
	@Test
	void testOneFloorRequest() {
		
		System.out.println("\n----------testOneFloorRequest----------\n");

		
		Floor floor = new Floor(queue, FILEPATH, NUM_FLOORS);
		
		assertTrue(floor.getCanSendRequest());

		Thread floorThread = new Thread(floor, "FLOOR");
		
		floorThread.start();		
		
			
		while(this.shouldRun) {
			Message newMessage = queue.pop();
			
			if (newMessage.getType() == MessageType.REQUEST) {
				DoorOpenedMessage pickupMessage = new DoorOpenedMessage("timestamp", DESTINATION_FLOOR, StopType.PICKUP, DirectionType.UP);
				
				queue.replyToFloor(pickupMessage);
				
				assertFalse(floor.getCanSendRequest());

				DoorOpenedMessage dropoffMessage = new DoorOpenedMessage("timestamp", DESTINATION_FLOOR, StopType.DROPOFF, DirectionType.UP);

				queue.replyToFloor(dropoffMessage);
			} else if (newMessage.getType() == MessageType.KILL) {
				shouldRun = false;
			}
		}
	}
}
