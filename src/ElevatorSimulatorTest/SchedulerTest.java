package ElevatorSimulatorTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import ElevatorSimulator.Elevator.ElevatorInfo;
import ElevatorSimulator.Elevator.ElevatorState;
import ElevatorSimulator.Messages.*;
import ElevatorSimulator.Scheduler.Scheduler;

/**
 * The unit tests for the scheduler subsystem using the
 * Mock ElevatorController class.
 * 
 * @author Sarah Chow
 * @author Kyra Lothrop
 *
 */
public class SchedulerTest {
	
	private Scheduler scheduler;
		
	private RequestElevatorMessage message1;
	private RequestElevatorMessage message2;
	private RequestElevatorMessage message3;
	
	
	/**
	 * Test to check the behaviour of the system when there is at least
	 * one available elevator in the ArrayList. Should return the ID of
	 * the available elevator.
	 */
	@Test
	void testAvailElevator() {
		System.out.println("\n----------testAvailElevator----------\n");

		message1 = new RequestElevatorMessage(new Date(1000), 4, DirectionType.DOWN, 1);
		message2 = new RequestElevatorMessage(new Date(1000), 2, DirectionType.UP, 5);
		message3 = new RequestElevatorMessage(new Date(1000), 3, DirectionType.DOWN, 1);
		
		ElevatorInfo e1 = new ElevatorInfo(DirectionType.UP, ElevatorState.POLL, 1, 0, 0);
		ElevatorInfo e2 = new ElevatorInfo(DirectionType.UP, ElevatorState.POLL, 1, 1, 0);
		
		scheduler = new Scheduler();

		scheduler.addToQueue(e1);
		scheduler.addToQueue(e2);
		
		int id1 = scheduler.getClosestElevator(message1);
		assertEquals(0, id1); // Elevator 0 service
		e1.setState(ElevatorState.MOVING);
		
		int id2 = scheduler.getClosestElevator(message2);
		assertEquals(1, id2); // Elevator 1 service
		e2.setState(ElevatorState.MOVING);
		
		e1.setState(ElevatorState.POLL);
		int id3 = scheduler.getClosestElevator(message3);
		
		assertEquals(0, id3); // Elevator 0 service
		
	}

}
