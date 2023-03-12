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
	 * Creating a new instance of the queue and shouldRun 
	 * before each test.
	 */
	@BeforeEach
	void init() {
		shouldRun = true;
		
		DoorOpenedMessage pickupMessage = new DoorOpenedMessage(new Date(1000), DESTINATION_FLOOR, StopType.PICKUP, DirectionType.UP, 0);
		DoorOpenedMessage dropoffMessage = new DoorOpenedMessage(new Date(1100), DESTINATION_FLOOR, StopType.DROPOFF, DirectionType.UP, 1);

		messages = new ArrayList<>();	
		messages.add(pickupMessage);
		messages.add(dropoffMessage);
	}
	

	public Message getFloorUpdate() {
		DoorOpenedMessage temp = messages.get(0);
		messages.remove(0);
		return temp;
	}
	
	/**
	 * The unit test depicts the sending of a message from the
	 * floor to the scheduler and back from the scheduler to floor
	 * as a confirmation message. Sends kill message at the end 
	 * to terminate thread.
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
