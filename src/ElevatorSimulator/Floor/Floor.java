package ElevatorSimulator.Floor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Scanner;

import ElevatorSimulator.Messages.*;
import ElevatorSimulator.Messaging.MessageQueue;

/**
 * @author Guy Morgenshtern
 * @author Sarah Chow
 *
 */
public class Floor implements Runnable {
	// The message queue.
	private MessageQueue queue;
	
	// Used for keeping track of all the pressed buttons.
	private ArrayDeque<Message> elevatorRequests;
	
	private boolean[] upLights; 
	
	private boolean[] downLights; 
	
	private boolean canSendRequest;
	
	private boolean shouldRun;
	
	/**
	 * ELevator constructor with a scheduler and a filename.
	 * 
	 * @param queue, the message queue to communicate with the scheduler.
	 * @param fileName the name of the input file.
	 */
	public Floor(MessageQueue queue, String fileName,int numFloors){
		this.queue = queue;
		elevatorRequests = new ArrayDeque<Message>();
		readInElevatorRequests(fileName);
		this.upLights = new boolean[numFloors];
		this.downLights = new boolean[numFloors];
		this.canSendRequest = true;
		this.shouldRun = true;
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
	 * Updates Lights
	 * @param request to add to the elevator.
	 */
	private void addRequest(RequestElevatorMessage request) {
		this.elevatorRequests.offer(request);
	}
	
	/**
	 * Sends the requestElevator to the scheduler.
	 */
	private void requestElevator() {
		
		if (!this.canSendRequest || this.elevatorRequests.isEmpty()) {
			return;
		}
		
		Message request = elevatorRequests.poll();
		updateLights(request);// turns light on
		this.canSendRequest = false;
		queue.send(request);	
	}
	
	/**
	 * Requests update from the scheduler.
	 * Updates Lights
	 * @return the updated message.
	 */
	private Message requestUpdate() {
	
		Message message = queue.receiveFromFloor();
		
		updateLights(message);// turns light off
		
		return message;
		
	}
	/**
	 * Updates the lights based on message type
	 * @param message The message that came in
	 */
	private void updateLights(Message message) {
		int floorNum;
		DirectionType direction;
		
		if(message.getType().equals(MessageType.DOORS_OPENED)) {
			DoorOpenedMessage doorOpened = (DoorOpenedMessage) message;
			
			if (doorOpened.getStopType() == StopType.DROPOFF) {
				this.canSendRequest = true;
			}
			
			floorNum = doorOpened.getArrivedFloor();

			direction = doorOpened.getDirection();
			if ( direction == DirectionType.UP) {
				this.upLights[floorNum-1] = false;
			} else {
				this.downLights[floorNum-1] = false;
			}
			
		} else {
			RequestElevatorMessage request = (RequestElevatorMessage) message;
			floorNum = request.getFloor();
			direction = request.getDirection();			
			if (direction == DirectionType.UP) {
				this.upLights[floorNum-1] = true;
			} else {
				this.downLights[floorNum-1] = true;
			}
		}
	}
	
	/**
	 * A display of the light status
	 */
	private void printLightStatus() {
		String floorLightsDisplay = "\nFLOOR LIGHTS STATUS\n---------------------------------------";
		for(int i = 0; i<this.upLights.length;i++) {
			floorLightsDisplay += "\n| Floor " + (i+1) + " up light on :" + this.upLights[i] + " ";
			floorLightsDisplay += " down light on :" + this.downLights[i] + " |";
		}
		floorLightsDisplay += "\n---------------------------------------\n";
		System.out.println(floorLightsDisplay);
	}
	/**
	 * Kills the current running instance of the floor.
	 */
	private void kill() {
		if (!this.canSendRequest) {
			return;
		}
		this.shouldRun = false;
		queue.send(new KillMessage(SenderType.FLOOR, "No more floor requests remaining"));	
	}
	
	public boolean getCanSendRequest() {
		return this.canSendRequest;
	}
	
	/**
	 * The run function used to logic of the floor.
	 */
	@Override
	public void run() {
		while (shouldRun) { // more conditions in the future to ensure all receive messages are accounted for
			requestElevator();
			
			printLightStatus();
			
			requestUpdate();
			
			if (this.elevatorRequests.isEmpty()) {
				kill();
			}
		}
		
	}

	

}
