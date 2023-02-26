package ElevatorSimulatorTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ElevatorSimulator.Messages.*;
import ElevatorSimulator.Messaging.MessageQueue;
import ElevatorSimulator.Scheduler.Scheduler;

/**
 * The unit tests for the scheduler subsystem.
 * 
 * @author Sarah Chow
 * @author Kyra Lothrop
 *
 */

class SchedulerTest {
	
	private Scheduler scheduler;
	
	private MockElevatorController mockEC;
	
	private MessageQueue queue;
	
	RequestElevatorMessage message;
	
	@BeforeEach
	void init() {
		queue = new MessageQueue();
		message = new RequestElevatorMessage("timestamp", 4, DirectionType.DOWN, 1);

	}
	
	@Test
	void testAvailElevator() {
		System.out.println("\n----------testAvailElevator----------\n");

		mockEC = new MockElevatorController(queue, 1);
		scheduler = new Scheduler(queue, mockEC);
						
		queue.send(message);	
		
		assertEquals(0, scheduler.getClosestElevator(message));
	}
	
	@Test
	void testZeroElevator() {
		System.out.println("\n----------testZeroElevator----------\n");

		mockEC = new MockElevatorController(queue, 0);
		scheduler = new Scheduler(queue, mockEC);
						
		queue.send(message);	
		
		assertEquals(-1, scheduler.getClosestElevator(message));
	}

}
