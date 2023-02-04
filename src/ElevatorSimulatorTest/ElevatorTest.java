package ElevatorSimulatorTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ElevatorSimulator.Elevator;
import ElevatorSimulator.Messages.*;

/**
 * The unit tests for the elevator subsystem.
 * 
 * @author Sarah Chow
 * @author Bardia Parmoun
 *
 */

class ElevatorTest {
	
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
	 * The unit test depicts the sending of a message from the
	 * scheduler to the elevator and the confirmation message
	 * from the elevator to the scheduler.
	 */
	@Test
	void testElevatorReceiveOneRequest() {
		
		Message message = new RequestElevatorMessage("timestamp", 4, DirectionType.DOWN, 1);

		scheduler.send(message);

		Elevator elevator = new Elevator(scheduler);
		
		elevator.run();
		
		assertNotNull(elevator);
		assertEquals(2, scheduler.getReceiveCount());
		assertEquals(2, scheduler.getSendCount());
				
	}

}
