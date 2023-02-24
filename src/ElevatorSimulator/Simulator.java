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
	
	public static int NUM_ELEVATORS = 1;
	
	/**
	 * The main method and the starting point for the program.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MessageQueue queue = new MessageQueue();
		
		Thread schedulerThread, elevatorControlThread, floorThread;
				
		ElevatorController elevatorController = new ElevatorController(queue, NUM_ELEVATORS);
		
		schedulerThread = new Thread(new Scheduler(queue, elevatorController), "SCHEDULER");
		elevatorControlThread = new Thread(elevatorController, "ELEVATOR");
		floorThread = new Thread(new Floor(queue, INPUT), "FLOOR");
				
		schedulerThread.start();
		elevatorControlThread.start();
		floorThread.start();
	}

}
