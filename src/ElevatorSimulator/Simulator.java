/**
 * 
 */
package ElevatorSimulator;

/**
 * @author Andre Hazim
 * @author Bardia Parmoun
 * @author Guy Morgenshtern
 * @author Kyra Lothrop
 * @author Sarah Chow
 *
 */
public class Simulator {
	public static void main(String[] args) {
		Scheduler scheduler;
		Thread schedulerThread, elevatorThread, floorThread;
		
		scheduler = new Scheduler();
		
		schedulerThread = new Thread(scheduler, "Scheduler");
		elevatorThread = new Thread(new Elevator(scheduler), "ELEVATOR");
		floorThread = new Thread( new Floor(scheduler, "src/ElevatorSimulator/resources/elevator_input.csv"), "FLOOR");
		
		
		
		schedulerThread.start();
		elevatorThread.start();
		floorThread.start();
		
	}

}
