/**
 * 
 */
package ElevatorSimulatorTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ElevatorSimulator.Floor;

/**
 * @author Bardia Parmoun
 * @author Sarah Chow
 *
 */
public class FloorTest {
	@Test
	void testFloorSendOneRequest() {
		MockScheduler scheduler = new MockScheduler();
		Floor floor = new Floor(scheduler, "src/ElevatorSimulatorTest/TestFiles/elevator_test-1.csv");
		
		floor.run();
		
		assertEquals(2, scheduler.getSendCount());
		assertEquals(1, scheduler.getReceiveCount());
	}
}
