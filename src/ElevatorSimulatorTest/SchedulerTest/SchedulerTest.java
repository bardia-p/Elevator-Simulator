package ElevatorSimulatorTest.SchedulerTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ElevatorSimulator.Simulator;
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
	private Scheduler scheduler;
	
	// The message queue object for the scheduler.
	private MessageQueue queue;
	
	/**
	 * Initializes the scheduler objects.
	 */
	@BeforeEach
	void init() {
		Thread.currentThread().setName("SCHEDULER THREAD");
		Simulator.DEBUG_MODE = false;
		
		queue = new MessageQueue();
		scheduler = new Scheduler(queue, false);

		// Creates 2 elevators for the queue.
		queue.addElevator(0, new ElevatorInfo(DirectionType.UP, ElevatorState.OPERATIONAL, ElevatorState.POLL, 1, 0, 0, new ArrayList<>()));
		queue.addElevator(1, new ElevatorInfo(DirectionType.UP,ElevatorState.OPERATIONAL, ElevatorState.POLL, 1, 1, 0, new ArrayList<>()));
	}
	
	/**
	 * Test to check the behaviour of the system when there are two elevators
	 * and confirms the servicing of each request per elevator is as expected.
	 */
	@Test
	public void testClosestElevator() {
		System.out.println("\n----------testClosestElevator----------\n");

		// Sample requests.
		RequestElevatorMessage message1 = new RequestElevatorMessage(new Date(1000), 4, DirectionType.DOWN, 1, 0, null);
		RequestElevatorMessage message2 = new RequestElevatorMessage(new Date(1000), 2, DirectionType.UP, 5, 0, null);
		RequestElevatorMessage message3 = new RequestElevatorMessage(new Date(1000), 3, DirectionType.DOWN, 1, 0, null);
				
		int id1 = scheduler.getClosestElevator(message1);
		
		// Elevator 1 was selected.
		assertEquals(0, id1); 
		
		// Set elevator 1 to MOVING.
		queue.updateInfo(0, new ElevatorInfo(DirectionType.UP, ElevatorState.OPERATIONAL, ElevatorState.MOVING, 1, 0, 1, new ArrayList<>()));
		
		int id2 = scheduler.getClosestElevator(message2);
		
		// Elevator 2 was selected.
		assertEquals(1, id2); 
		
		// Set elevator 2 to MOVING.
		queue.updateInfo(1, new ElevatorInfo(DirectionType.UP,ElevatorState.OPERATIONAL, ElevatorState.MOVING, 1, 1, 1, new ArrayList<>()));
		
		int id3 = scheduler.getClosestElevator(message3);
		
		// No available elevators.
		assertEquals(-1, id3); 
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
		queue.updateInfo(0, new ElevatorInfo(DirectionType.UP, ElevatorState.OPERATIONAL, ElevatorState.MOVING, 1, 0, 1, new ArrayList<>()));
		
		// Both elevators are available, neither is stuck.
		assertEquals(2, scheduler.getAvailableElevators().size());
		assertEquals(0, scheduler.getAvailableElevators().get(0).getElevatorId());
		
		// Set elevator 2 to MOVING.
		queue.updateInfo(1, new ElevatorInfo(DirectionType.UP, ElevatorState.OPERATIONAL, ElevatorState.MOVING, 1, 1, 1, new ArrayList<>()));
		
		// Both elevators are available, neither is stuck.
		assertEquals(2, scheduler.getAvailableElevators().size());
		
		// Set elevator 1 back to POLL.
		queue.updateInfo(0, new ElevatorInfo(DirectionType.UP, ElevatorState.OPERATIONAL, ElevatorState.POLL, 1, 0, 1, new ArrayList<>()));
		
		// Both elevators are available, neither is stuck.
		assertEquals(2, scheduler.getAvailableElevators().size());
		assertEquals(0, scheduler.getAvailableElevators().get(0).getElevatorId());
		
		// Set elevator 1 to STUCK.
		queue.updateInfo(0, new ElevatorInfo(DirectionType.UP, ElevatorState.ELEVATOR_STUCK, ElevatorState.POLL, 1, 0, 1, new ArrayList<>()));
		
		// Only one elevator is available.
		assertEquals(1, scheduler.getAvailableElevators().size());
		assertEquals(1, scheduler.getAvailableElevators().get(0).getElevatorId());
	}
	
	/**
	 * Test re-routing the request in an elevator stuck event.
	 */
	@Test
	public void testReroutingElevator() {
		// Elevator request.
		RequestElevatorMessage message1 = new RequestElevatorMessage(new Date(1000), 4, DirectionType.DOWN, 1, 0, null);
			
		// Two available elevators at the beginning.
		assertEquals(2, scheduler.getAvailableElevators().size());	
		
		int id1 = scheduler.getClosestElevator(message1);
		
		// Elevator 1 was selected.
		assertEquals(0, id1); 
		
		// Set elevator 1 to STUCK.
		queue.updateInfo(0, new ElevatorInfo(DirectionType.UP, ElevatorState.ELEVATOR_STUCK, ElevatorState.POLL, 1, 0, 1, new ArrayList<>()));
		
		// Only one available elevator.
		assertEquals(1, scheduler.getAvailableElevators().size());	
		
		int id2 = scheduler.getClosestElevator(message1);
		
		// Elevator 2 was selected.
		assertEquals(1, id2); 
	}
}
