package ElevatorSimulator.Floor;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Scanner;

import ElevatorSimulator.Simulator;
import ElevatorSimulator.Timer;
import ElevatorSimulator.Messages.*;
import ElevatorSimulator.Messaging.ClientRPC;
import ElevatorSimulator.Scheduler.Scheduler;

/**
 * @author Guy Morgenshtern
 * @author Sarah Chow
 * @author Kyra Lothrop
 * @author Bardia Parmoun
 *
 */
public class Floor extends ClientRPC implements Runnable {
	// Used for keeping track of all the pressed buttons.
	private ArrayDeque<Message> elevatorRequests;
		
	private boolean[] upLights; 
	
	private boolean[] downLights; 
		
	private boolean shouldRun;
		
	private boolean canStart;

	private Timer timer;
	
	private SimpleDateFormat dateFormat;
	
	private String filename;
	
	/**
	 * ELevator constructor with a scheduler and a filename.
	 * 
	 * @param queue, the message queue to communicate with the scheduler.
	 * @param fileName the name of the input file.
	 * @throws ParseException 
	 */
	public Floor(String fileName,int numFloors) throws ParseException{
		super(Scheduler.FLOOR_PORT);
		this.dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
		this.elevatorRequests = new ArrayDeque<Message>();
		this.upLights = new boolean[numFloors];
		this.downLights = new boolean[numFloors];
		this.shouldRun = true;
		this.canStart = false;
		this.filename = fileName;
		this.timer = new Timer();		
	}
	
	/**
	 * Gets a comma separated string and builds an ElevatorRequestMessage
	 * 
	 * @param line - comma separated string
	 * 
	 * @return - ElevatorRequestMessage
	 * @throws ParseException 
	 */
	private RequestElevatorMessage buildRequestFromCSV(String line) throws ParseException {
		String[] entry = line.split(",");
		
		int floor = Integer.parseInt(entry[1]);
		int destination = Integer.parseInt(entry[3]);
		
		DirectionType direction = DirectionType.valueOf(entry[2]);
		
		return new RequestElevatorMessage((Date) dateFormat.parse(entry[0]), floor, direction, destination);
	}
	
	/**
	 * Given a file path, adds request to queue for each line.
	 * 
	 * @param fileName
	 * @throws ParseException 
	 */
	private void readInElevatorRequests(String fileName) throws ParseException {
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
		
		timer.setTime(this.elevatorRequests.peek().getTimestamp());
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
		if (this.elevatorRequests.isEmpty() || (elevatorRequests.peek().getTimestamp().compareTo(timer.getTime()) > 0)) {
			return;
		}
		
		Message request = elevatorRequests.poll();
		updateLights(request);// turns light on
		sendRequest(request);		
	}
	
	/**
	 * Requests update from the scheduler.
	 * Updates Lights
	 * @return the updated message.
	 */
	private Message requestUpdate() {
		Message message = getFloorUpdate();
		
		if (message != null) {
			if (message.getType() == MessageType.EMPTY) {
				return null;
			}
			printMessage(message, "RECEIVED");

			if (message.getType() == MessageType.DOORS_OPENED) {
				updateLights(message); // turns light off
			}
			else if (message.getType() == MessageType.START) {
				canStart = true;
			}
		}
		
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
			
			floorNum = doorOpened.getArrivedFloor();

			direction = doorOpened.getDirection();
			if ( direction == DirectionType.UP) {
				this.upLights[floorNum-1] = false;
			} else {
				this.downLights[floorNum-1] = false;
			}
			
		} 
		else {
			RequestElevatorMessage request = (RequestElevatorMessage) message;
			floorNum = request.getFloor();
			direction = request.getDirection();			
			if (direction == DirectionType.UP) {
				this.upLights[floorNum-1] = true;
			} else {
				this.downLights[floorNum-1] = true;
			}
		}
		
		printLightStatus();
	}
	
	/**
	 * A display of the light status
	 */
	private void printLightStatus() {
		String floorLightsDisplay = "\nFLOOR LIGHTS STATUS\n-------------------------------------------";
		for(int i = 0; i<this.upLights.length;i++) {			
			floorLightsDisplay += "\n| Floor " + (i + 1) + " UP light: " + (this.upLights[i] ? "on " : "off") + " | DOWN light: " + (this.downLights[i] ? "on " : "off") + " |";
		}
		floorLightsDisplay += "\n-------------------------------------------\n";
		System.out.println(floorLightsDisplay);
	}
	
	/**
	 * The run function used to logic of the floor.
	 */
	@Override
	public void run() {
		try {
			readInElevatorRequests(this.filename);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		while (shouldRun) { // more conditions in the future to ensure all receive messages are accounted for
			
			if (canStart) {
				requestElevator();
			}
			
			requestUpdate();
			
			if (canStart) {
				timer.tick();
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void printMessage(Message m, String type) {
		
		String result = "";
		String addResult = "";
		String messageToPrint = "";
				
		if (m != null) {
			
			result += "\n---------------------" + Thread.currentThread().getName() +"-----------------------\n";
			result += String.format("| %-15s | %-10s | %-10s | %-3s |\n", "REQUEST", "ACTION", "RECEIVED", "SENT");
			result += new String(new char[52]).replace("\0", "-");
			
			addResult += String.format("\n| %-15s | %-10s | ",  m.getDescription(), m.getDirection());
			addResult += String.format(" %-10s | %-3s |", type == "RECEIVED" ? "*" : " ", type == "RECEIVED" ? " " : "*");
			
			System.out.println(messageToPrint + result + addResult);
		}
		
	}
	
	public static void main(String[] args) {
		try {
			Thread  floorThread = new Thread(new Floor(Simulator.INPUT, Simulator.NUM_FLOORS));
			floorThread.start();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
}
