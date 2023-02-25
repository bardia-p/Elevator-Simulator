package ElevatorSimulatorTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ElevatorSimulator.Elevator.Elevator;
import ElevatorSimulator.Elevator.ElevatorState;
import ElevatorSimulator.Messages.*;
import ElevatorSimulator.Messaging.MessageQueue;

/**
 * The unit tests for the elevator subsystem.
 * 
 * @author Sarah Chow
 * @author Bardia Parmoun
 *
 */

class ElevatorTest {
	
	// Mock scheduler object.
	//MockScheduler scheduler;
	
	MessageQueue queue;
	
	public static int ELEVATOR_ID = 0;
	
	public static int NUM_FLOORS = 4;
	
	public boolean shouldRun;
	
	/**
	 * Creating a new instance of the mock scheduler before
	 * each new test.
	 */
	@BeforeEach
	void init() {
		//scheduler = new MockScheduler();
		queue = new MessageQueue();
	}

	/**
	 * The unit test depicts the sending of a message from the
	 * scheduler to the elevator and the confirmation message
	 * from the elevator to the scheduler.
	 * @throws InterruptedException 
	 */
	@Test
	void testElevatorReceiveOneRequest() throws InterruptedException {
		Elevator elevator = new Elevator(queue, ELEVATOR_ID, NUM_FLOORS);
		Thread elevatorThread = new Thread(elevator);
		queue.addElevator();
		
		Message message = new RequestElevatorMessage("timestamp", 4, DirectionType.DOWN, 1);
		//queue.send(message);
		queue.replyToElevator(message, ELEVATOR_ID);
		
		elevatorThread.start();
		elevatorThread.join();
		
		shouldRun = true;
		
		assertNotNull(elevator);
		assertEquals(1, elevator.getFloorNumber());
		assertEquals(ElevatorState.POLL, elevator.getState());
		
		System.out.println(queue.elevatorHasRequest(ELEVATOR_ID));
		
		while(this.shouldRun) {
			//queue.pop();
			
			System.out.println(elevator.getFloorNumber() + " " + elevator.getState() + "ABC");
			
			if(elevator.getFloorNumber() != 4 && elevator.getState() != ElevatorState.POLL) {
				System.out.println("here1");
				assertEquals(elevator.getState(), ElevatorState.MOVING);
			}
			else if(elevator.getFloorNumber() != 4 && elevator.getState() != ElevatorState.MOVING){
				System.out.println("here2");
				assertEquals(elevator.getState(), ElevatorState.POLL);
			}
			else {
				System.out.println("here3");
				assertEquals(elevator.getState(), ElevatorState.ARRIVED);
				this.shouldRun = false;
			}
		}
	}
}
