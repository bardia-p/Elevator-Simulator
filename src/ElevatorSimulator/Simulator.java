package ElevatorSimulator;

/**
 * The class in charge of starting up the subsystems.
 * 
 * @author Guy Morgenshtern
 * @author Sarah Chow
 *
 */
public class Simulator {
	// Keeps track of the input file name for the simulator.
	public static String INPUT = "src/ElevatorSimulator/Resources/elevator_input.csv";
	
	/**
	 * The main method and the starting point for the program.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MessageQueue queue = new MessageQueue();
		
		Thread schedulerThread, elevatorThread, floorThread;
				
		schedulerThread = new Thread(new Scheduler(queue), "SCHEDULER");
		elevatorThread = new Thread(new Elevator(queue), "ELEVATOR");
		floorThread = new Thread(new Floor(queue, INPUT), "FLOOR");
				
		schedulerThread.start();
		elevatorThread.start();
		floorThread.start();
	}

}
