package ElevatorSimulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Scanner;

import ElevatorSimulator.Messages.*;

/**
 * @author Guy Morgenshtern
 * @author Sarah Chow
 *
 */
public class Floor implements Runnable {
	// The scheduler used to get new messages and reply with updates.
	private Scheduler scheduler;
	
	// Used for keeping track of all the pressed buttons.
	private ArrayDeque<Message> elevatorRequests;
	
	/**
	 * ELevator constructor with a scheduler and a filename.
	 * 
	 * @param scheduler the shared scheduler instance.
	 * @param fileName the name of the input file.
	 */
	public Floor(Scheduler scheduler, String fileName){
		this.scheduler = scheduler;
		elevatorRequests = new ArrayDeque<Message>();
		readInElevatorRequests(fileName);
	}
	
	/**
	 * Gets a comma separated string and builds an ElevatorRequestMessage
	 * 
	 * @param line - comma separated string
	 * 
	 * @return - ElevatorRequestMessage
	 */
	private RequestElevatorMessage buildRequestFromCSV(String line) {
		String[] entry = line.split(",");
		
		
		int floor = Integer.parseInt(entry[1]);
		int destination = Integer.parseInt(entry[3]);
		
		DirectionType direction = DirectionType.valueOf(entry[2]);
		
		return new RequestElevatorMessage(entry[0], floor, direction, destination);
	}
	
	/**
	 * Given a file path, adds request to queue for each line.
	 * 
	 * @param fileName
	 */
	private void readInElevatorRequests(String fileName) {
		Scanner sc;
		try {
			sc = new Scanner(new File(fileName));
			
			//skip header line
			sc.nextLine();
			
			while (sc.hasNextLine()) {
				addRequest(buildRequestFromCSV(sc.nextLine()));
			}
			
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
			
	}
	
	/**
	 * Adds a request to the list of elevator requests.
	 * 
	 * @param request to add to the elevator.
	 */
	private void addRequest(RequestElevatorMessage request) {
		this.elevatorRequests.offer(request);
	}
	
	/**
	 * Sends the requestElevator to the scheduler.
	 */
	private void requestElevator() {
		Message request = elevatorRequests.poll();
		scheduler.send(request);	
	}
	
	/**
	 * Requests update from the scheduler.
	 * 
	 * @return the updated message.
	 */
	private Message requestUpdate() {
		return scheduler.receive(SenderType.FLOOR);
	}
	
	/**
	 * Kills the current running instance of the floor.
	 */
	private void kill() {
		scheduler.send(new KillMessage(SenderType.FLOOR, "No more floor requests remaining"));	
	}
	
	/**
	 * The run function used to logic of the floor.
	 */
	@Override
	public void run() {
		while (!elevatorRequests.isEmpty()) { // more conditions in the future to ensure all receive messages are accounted for
			requestElevator();

			requestUpdate();
		}
		
		kill();
	}

}
