package ElevatorSimulatorTest.FloorTest;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ElevatorSimulator.Floor.Floor;
import ElevatorSimulator.Messages.DirectionType;
import ElevatorSimulator.Messages.ErrorType;
import ElevatorSimulator.Messages.Message;
import ElevatorSimulator.Messages.RequestElevatorMessage;
import ElevatorSimulator.Messaging.ConnectionType;

/**
 * The unit tests for the floor subsystem.
 * 
 * @author Kyra Lothrop
 * @author Sarah Chow
 * @author Bardia Parmoun
 *
 */
public class FloorParseTest{		
	
	// Keeps track of the floor object.
	private Floor floor;

	// Test constants.
	public static int NUM_FLOORS = 4;
	
	public static String STUCK_FILEPATH = "src/ElevatorSimulatorTest/TestFiles/elevator_test-stuck.csv";
	public static String INTERRUPT_FILEPATH = "src/ElevatorSimulatorTest/TestFiles/elevator_test-interrupt.csv";
	
	@BeforeEach
	void init() {
		floor = new Floor(STUCK_FILEPATH, NUM_FLOORS, ConnectionType.LOCAL);
	}
	/**
	 * This tests the Floor's functionality for parsing a request CSV that has elevator stuck faults within it
	 *
	 */
	@Test
	void testParseRequestsWithStuckFaults() {
		System.out.println("\n----------testParseRequestsWithStuckFault----------\n");

		Thread.currentThread().setName("FLOOR TEST THREAD");
		
		floor.readInElevatorRequests(STUCK_FILEPATH);


		RequestElevatorMessage msgWithFault = null;
		for(Message message : floor.getElevatorRequestsList()) {
			if (((RequestElevatorMessage)message).getError() == ErrorType.ELEVATOR_STUCK) {
					msgWithFault = (RequestElevatorMessage)message;
				msgWithFault = (RequestElevatorMessage)message;

			}
		}
		
		assertNotNull(msgWithFault);
		assertEquals(msgWithFault.getError(), ErrorType.ELEVATOR_STUCK);
		assertEquals(msgWithFault.getErrorTime(), 3000);
		assertEquals(msgWithFault.getDirection(), DirectionType.DOWN);
		assertEquals(msgWithFault.getFloor(), 2);
		assertEquals(msgWithFault.getDestination(), 1);	
	}
	
	/**
	 * This tests the Floor's functionality for parsing a request CSV that has elevator door interrupt faults within it
	 *
	 */
	@Test
	void testParseRequestsWithInterruptFaults() {
		System.out.println("\n----------testParseRequestsWithInterruptFaults----------\n");

		Thread.currentThread().setName("FLOOR TEST THREAD");
		

		floor.readInElevatorRequests(INTERRUPT_FILEPATH);

		RequestElevatorMessage msgWithFault = null;
		for(Message message : floor.getElevatorRequestsList()) {
			if (((RequestElevatorMessage)message).getError() == ErrorType.DOOR_INTERRUPT) {
				msgWithFault = (RequestElevatorMessage)message;
			}
		}
		
		assertNotNull(msgWithFault);
		assertEquals(msgWithFault.getError(), ErrorType.DOOR_INTERRUPT);
		assertEquals(msgWithFault.getErrorTime(), 3000);
		assertEquals(msgWithFault.getDirection(), DirectionType.DOWN);
		assertEquals(msgWithFault.getFloor(), 2);
		assertEquals(msgWithFault.getDestination(), 1);
		
	}
	
}
