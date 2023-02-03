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
	private static final String INPUT = "elevator_input.csv";
	public static void main(String[] args) {
		Scheduler scheduler;
		Thread schedulerThread, elevatorThread, floorThread;
		
		scheduler = new Scheduler();
		
		schedulerThread = new Thread(scheduler, "SCHEDULER");
		elevatorThread = new Thread(new Elevator(scheduler), "ELEVATOR");
		floorThread = new Thread( new Floor(scheduler, INPUT), "FLOOR");
		
		
		
		schedulerThread.start();
		elevatorThread.start();
		floorThread.start();
		
	}

}
