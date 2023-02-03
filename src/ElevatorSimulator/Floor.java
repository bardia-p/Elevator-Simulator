/**
 * 
 */
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
	private Scheduler scheduler;
	private ArrayDeque<Message> elevatorRequests;
	
	public Floor(Scheduler scheduler, String fileName){
		this.scheduler = scheduler;
		elevatorRequests = new ArrayDeque<Message>();
		readInElevatorRequests(fileName);
		
	}
	
	private RequestElevatorMessage buildRequestFromCSV(String line) {
		String[] entry = line.split(",");
		
		
		int floor = Integer.parseInt(entry[1]);
		int destination = Integer.parseInt(entry[3]);
		
		DirectionType direction = DirectionType.valueOf(entry[2]);
		
		return new RequestElevatorMessage(entry[0], floor, direction, destination);
	}
	
	//possibly refactor to readLine from csv then call method to build message
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	private void addRequest(RequestElevatorMessage request) {
		this.elevatorRequests.offer(request);
	}
	
	private void requestElevator() {
		Message request = elevatorRequests.poll();
		scheduler.send(request);	
	}
	
	private Message requestUpdate() {
		return scheduler.receive(SenderType.FLOOR);
	}
	
	public void kill() {
		scheduler.send(new KillMessage(SenderType.FLOOR, "No more floor requests remaining"));	
	}
	
	@Override
	public void run() {
		while (!elevatorRequests.isEmpty()) { // more conditions in the future to ensure all receive messages are accounted for
			requestElevator();

			requestUpdate();
			
			/*try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			
		}
		
		kill();
	}

}
