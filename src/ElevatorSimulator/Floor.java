/**
 * 
 */
package ElevatorSimulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * @author Guy Morgenshtern
 * @author Sarah Chow
 *
 */
public class Floor implements Runnable {
	private Scheduler scheduler;
	private Deque<Message> elevatorRequests;
	
	Floor(Scheduler scheduler, String fileName){
		this.scheduler = scheduler;
		elevatorRequests = new LinkedList<Message>();
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
				elevatorRequests.offer(buildRequestFromCSV(sc.nextLine()));
			}
			
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	@Override
	public void run() {
		
		while (!elevatorRequests.isEmpty()) { // more conditions in the future to ensure all receive messages are accounted for
			
			Message request = elevatorRequests.poll();
			System.out.println("IN FLOOR SENT: \n" + request.getDescription());
			scheduler.send(request);
			
//			Message receive = scheduler.receive();
//			System.out.println("FLOOR RECEIVED: \n" + receive.getDescription());
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
