/**
 * 
 */
package ElevatorSimulatorTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ElevatorSimulator.Floor;

/**
 * The unit tests for the floor subsystem.
 * 
 * @author Bardia Parmoun
 * @author Sarah Chow
 *
 */
public class FloorTest {
	
	// Mock scheduler object.
	MockScheduler scheduler;
	
	/**
	 * Creating a new instance of the mock scheduler before
	 * each new test.
	 */
	@BeforeEach
	void init() {
		scheduler = new MockScheduler();
	}
	
	/**
	 * The unit test depicts the sending of a message and a 
	 * kill message from the floor to the scheduler.
	 */
	@Test
	void testFloorSendOneRequest() {
		
		Floor floor = new Floor(scheduler, "src/ElevatorSimulatorTest/TestFiles/elevator_test-1.csv");
		
		floor.run();
		
		assertNotNull(floor);
		assertEquals(2, scheduler.getSendCount());
		assertEquals(1, scheduler.getReceiveCount());
	}
	

}
