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
 * @author Kyra Lothrop
 *
 */

class ElevatorTest {
	
	MessageQueue queue;
	
	public static int ELEVATOR_ID = 0;
	
	public static int NUM_FLOORS = 4;
	
	public boolean shouldRun;
	
	/**
	 * Creating a new instance of the MessageQueue
	 * before each test.
	 */
	@BeforeEach
	void init() {
		queue = new MessageQueue();
	}

	/**
	 * The unit test depicts the receiving of a message from the
	 * scheduler to the elevator to move from floor 4 to 1.
	 * Confirms the expected environments of each 
	 * state (DOORS_OPENED, PICKUP, ARRIVE). Sends kill message
	 * at the end to terminate thread.
	 * @throws InterruptedException 
	 */
	@Test
	void testOneElevatorRequest() throws InterruptedException {
		
		System.out.println("\n----------testOneElevatorRequest----------\n");

		
		Elevator elevator = new Elevator(queue, ELEVATOR_ID, NUM_FLOORS);
		Thread elevatorThread = new Thread(elevator, "ELEVATOR");
		queue.addElevator();
		
		Message message = new RequestElevatorMessage("timestamp", 4, DirectionType.DOWN, 1);
		queue.replyToElevator(message, ELEVATOR_ID);
		
		assertNotNull(elevator);
		assertEquals(1, elevator.getFloorNumber());
		assertEquals(ElevatorState.POLL, elevator.getState());
		
		elevatorThread.start();
		
		shouldRun = true;
		
		assertTrue(queue.elevatorHasRequest(ELEVATOR_ID));
		
		int expectedFloor = 1;
		
		while(this.shouldRun) {
			Message newMessage = queue.pop();
			
			if (newMessage.getType() == MessageType.DOORS_OPENED) {
				DoorOpenedMessage doorsOpenMessage = (DoorOpenedMessage) newMessage;
				
				if (doorsOpenMessage.getStopType() == StopType.PICKUP) {
					assertEquals(ElevatorState.OPEN, elevator.getState());
					assertEquals(DirectionType.UP, doorsOpenMessage.getDirection());
					assertEquals(4, doorsOpenMessage.getArrivedFloor());
				} else {
					assertEquals(ElevatorState.OPEN, elevator.getState());
					assertEquals(DirectionType.DOWN, doorsOpenMessage.getDirection());
					assertEquals(1, doorsOpenMessage.getArrivedFloor());
					
					shouldRun = false;
				}
			} else if (newMessage.getType() == MessageType.ARRIVE) {
				if (elevator.getDirection() == DirectionType.UP) {
					expectedFloor++;
				} else {
					expectedFloor--;
				}
				assertEquals(expectedFloor, ((ArrivedElevatorMessage)newMessage).getArrivedFloor());
			}
		}
		
		queue.replyToElevator(new KillMessage(SenderType.FLOOR, "No more floor requests remaining"), ELEVATOR_ID);	
		
		elevatorThread.join();
	}
}
