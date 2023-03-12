package ElevatorSimulatorTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ElevatorSimulator.Floor.Floor;
import ElevatorSimulator.Messages.DirectionType;
import ElevatorSimulator.Messages.DoorOpenedMessage;
import ElevatorSimulator.Messages.Message;
import ElevatorSimulator.Messages.StopType;

/**
 * The unit tests for the floor subsystem.
 * 
 * @author Kyra Lothrop
 * @author Sarah Chow
 * @author Bardia Parmoun
 *
 */
public class FloorTest{	
	public static int NUM_FLOORS = 4;
	
	public static String FILEPATH = "src/ElevatorSimulatorTest/TestFiles/elevator_test-1.csv";
	
	private boolean shouldRun;
	
	private int DESTINATION_FLOOR = 4;
	private ArrayList<DoorOpenedMessage> messages;

	
	/**
	 * Creating the messages and adding to ArrayList
	 * before each test.
	 */
	@BeforeEach
	void init() {
		shouldRun = true;
		
		DoorOpenedMessage pickupMessage = new DoorOpenedMessage(new Date(1000), DESTINATION_FLOOR, StopType.PICKUP, DirectionType.UP, 1, 0);
		DoorOpenedMessage dropoffMessage = new DoorOpenedMessage(new Date(1100), DESTINATION_FLOOR, StopType.DROPOFF, DirectionType.UP, 0, 1);

		messages = new ArrayList<>();	
		messages.add(pickupMessage);
		messages.add(dropoffMessage);
	}
	

	/**
	 * Method to retrieve the latest message in the arraylist.
	 * @return latest message, Message
	 */
	public Message getFloorUpdate() {
		DoorOpenedMessage temp = messages.get(0);
		messages.remove(0);
		return temp;
	}
	
	/**
	 * The unit test depicts the receiving of a message to the
	 * floor. Confirms the time delay between the two messages
	 * are as expected.
	 */
	@Test
	void testOneFloorRequest() throws Exception{
		
		System.out.println("\n----------testOneFloorRequest----------\n");

		Floor floor = new Floor(FILEPATH, NUM_FLOORS);
		
		assertNotNull(floor);
				
		int count = 0;
		
		Date d1 = new Date();
		Date d2 = new Date();
			
		while(this.shouldRun) {
			
			if (count == 0) {
				Message newMessage = this.getFloorUpdate();

				d1 = newMessage.getTimestamp();
			}
			else if (count == 1) {
				Message newMessage = this.getFloorUpdate();

				d2 = newMessage.getTimestamp();
			}
			else {
				this.shouldRun = false;
			}
			
			count++;
		}
		
		assertEquals(d2.getTime() - d1.getTime(), 100); 
	}
}
