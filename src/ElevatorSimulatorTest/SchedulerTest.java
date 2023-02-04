package ElevatorSimulatorTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ElevatorSimulator.Scheduler;
import ElevatorSimulator.Messages.*;

/**
 * The unit tests for the scheduler subsystem.
 * 
 * @author Sarah Chow
 * @author Bardia Parmoun
 *
 */
public class SchedulerTest {
	
	private Scheduler scheduler;
	
	/**
	 * Creates an instance of the scheduler before each test.
	 * 
	 */
	@BeforeEach
	void init() {
		scheduler = new Scheduler();
	}

	/**
	 * This unit test depicts the transmission of a kill message from 
	 * the floor through the scheduler to the elevator. Ensures the 
	 * message sent from the floor and received by the elevator 
	 * contain equivalent data.
	 */
	@Test
	void testFloorToElevator() {
		
		Message message = new KillMessage(SenderType.FLOOR, "kill message from floor");
		
		scheduler.send(message);
		scheduler.run();
		
		Message result = scheduler.receive(SenderType.ELEVATOR);
		
		assertNotNull(result);
		assertEquals(result, message);
	}
	
	/**
	 * This unit test depicts the transmission of a kill message from
	 * the elevator through the scheduler to the floor. Ensures the
	 * message sent from the elevator and received by the floor 
	 * contain equivalent data.
	 */
	@Test
	void testElevatorToFloor() {
		Message message = new KillMessage(SenderType.ELEVATOR, "kill message from elevator");
		
		scheduler.send(message);
		scheduler.run();
		
		Message result = scheduler.receive(SenderType.FLOOR);
		
		assertNotNull(result);
		assertEquals(result, message);
	}

}
