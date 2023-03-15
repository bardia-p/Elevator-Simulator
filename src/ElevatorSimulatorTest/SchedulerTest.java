package ElevatorSimulatorTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ElevatorSimulator.Elevator.ElevatorInfo;
import ElevatorSimulator.Elevator.ElevatorState;
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
public class SchedulerTest {
	// Keeps track of the scheduler object.
	private Scheduler scheduler = new Scheduler();;
	
	// The message queue object for the scheduler.
	private MessageQueue queue;
	
	/**
	 * Initializes the scheduler objects.
	 */
	@BeforeEach
	void init() {
		Thread.currentThread().setName("SCHEDULER THREAD");

		queue = new MessageQueue();
		scheduler.updateQueue(queue);
		
		// Creates 2 elevators for the queue.
		queue.addElevator(0, new ElevatorInfo(DirectionType.UP, ElevatorState.POLL, 1, 0, 0));
		queue.addElevator(1, new ElevatorInfo(DirectionType.UP, ElevatorState.POLL, 1, 1, 0));
	}
	
	/**
	 * Test to check the behaviour of the system when there are two elevators
	 * and confirms the servicing of each request per elevator is as expected.
	 */
	@Test
	public void testClosestElevator() {
		System.out.println("\n----------testClosestElevator----------\n");

		// Sample requests.
		RequestElevatorMessage message1 = new RequestElevatorMessage(new Date(1000), 4, DirectionType.DOWN, 1);
		RequestElevatorMessage message2 = new RequestElevatorMessage(new Date(1000), 2, DirectionType.UP, 5);
		RequestElevatorMessage message3 = new RequestElevatorMessage(new Date(1000), 3, DirectionType.DOWN, 1);
				
		int id1 = scheduler.getClosestElevator(message1);
		
		// Elevator 1 was selected.
		assertEquals(0, id1); 
		
		// Set elevator 1 to MOVING.
		queue.updateInfo(0, new ElevatorInfo(DirectionType.UP, ElevatorState.MOVING, 1, 0, 1));
		
		int id2 = scheduler.getClosestElevator(message2);
		
		// Elevator 2 was selected.
		assertEquals(1, id2); 
		
		// Set elevator 2 to MOVING.
		queue.updateInfo(1, new ElevatorInfo(DirectionType.UP, ElevatorState.MOVING, 1, 1, 1));
		
		int id3 = scheduler.getClosestElevator(message3);
		
		// No available elevators.
		assertEquals(-1, id3); 
		
		// Set elevator 1 to POLL.
		queue.updateInfo(0, new ElevatorInfo(DirectionType.UP, ElevatorState.POLL, 2, 0, 1));
		
		int id4 = scheduler.getClosestElevator(message3);
		
		// Elevator 1 was selected again.
		assertEquals(0, id4);
	}
	
	/**
	 * Tests the get available elevators function in the scheduler.
	 */
	@Test
	public void testAvailableElevator() {
		System.out.println("\n----------testAvailableElevator----------\n");
	
		// Two available elevators at the beginning.
		assertEquals(2, scheduler.getAvailableElevators().size());

		// Set elevator 1 to MOVING.
		queue.updateInfo(0, new ElevatorInfo(DirectionType.UP, ElevatorState.MOVING, 1, 0, 1));
		
		// Only second elevator is available now.
		assertEquals(1, scheduler.getAvailableElevators().size());
		assertEquals(1, scheduler.getAvailableElevators().get(0).getElevatorId());
		
		// Set elevator 2 to MOVING.
		queue.updateInfo(1, new ElevatorInfo(DirectionType.UP, ElevatorState.MOVING, 1, 1, 1));
		
		// No available elevators.
		assertEquals(0, scheduler.getAvailableElevators().size());
		
		// Set elevator 1 back to POLL.
		queue.updateInfo(0, new ElevatorInfo(DirectionType.UP, ElevatorState.POLL, 1, 0, 1));
		
		// Only first elevator is available now.
		assertEquals(1, scheduler.getAvailableElevators().size());
		assertEquals(0, scheduler.getAvailableElevators().get(0).getElevatorId());
		
	}

}
