package ElevatorSimulator.Floor;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

import ElevatorSimulator.Logger;
import ElevatorSimulator.Simulator;
import ElevatorSimulator.Timer;
import ElevatorSimulator.Elevator.ElevatorTrip;
import ElevatorSimulator.Messages.*;
import ElevatorSimulator.Messaging.ClientRPC;
import ElevatorSimulator.Scheduler.Scheduler;

/**
 * The class in charge of the floor subsystem.
 * 
 * @author Guy Morgenshtern
 * @author Sarah Chow
 * @author Kyra Lothrop
 * @author Bardia Parmoun
 *
 */
public class Floor extends ClientRPC implements Runnable {
	// Used for keeping track of all the pressed buttons.
	private ArrayDeque<Message> elevatorRequests;
  
	// Used for keeping track of all the dropoff locations for the floor.
	private ArrayList<Integer> dropoffs;
  
	/**
	 * Floor lights.
	 */
	private int[] upLights; 
	private int[] downLights; 
	
	// The flag used to check if the floor subsystem should keep running.
	private boolean shouldRun;
		
	// Checks to see if the floor subsystem can start sending requests.
	private boolean canStart;
	
	// The date format used for parsing the messages.
	private SimpleDateFormat dateFormat;
	
	// The filename used for reading the requests.
	private String filename;
	
	// Checks to see if the floor subsystem can die.
	private boolean canKill;
	
	// Logger object
	private Logger logger;
		
	/**
	 * ELevator constructor with a scheduler and a filename.
	 * 
	 * @param queue, the message queue to communicate with the scheduler.
	 * @param fileName the name of the input file.
	 * @throws ParseException 
	 */
	public Floor(String fileName,int numFloors) {
		super(Scheduler.FLOOR_PORT);
		this.dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
		this.elevatorRequests = new ArrayDeque<Message>();
		this.upLights = new int[numFloors];
		this.downLights = new int[numFloors];
		this.shouldRun = true;
		this.canStart = false;
		this.filename = fileName;
		this.dropoffs = new ArrayList<>();
		this.logger = new Logger();
		
		Arrays.fill(upLights, 0);
		Arrays.fill(downLights, 0);
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
		int timeError = Integer.parseInt(entry[4]);
		
		DirectionType direction = DirectionType.valueOf(entry[2]);
		ErrorType error = entry[5].equals("null") ? null : ErrorType.valueOf(entry[5]);
		
		return new RequestElevatorMessage((Date) dateFormat.parse(entry[0]), floor, direction, destination, timeError, error);
	}
	
	/**
	 * Given a file path, adds request to queue for each line.
	 * 
	 * @param fileName
	 * @throws ParseException 
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
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Timer.setTime(this.elevatorRequests.peek().getTimestamp());
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
	 * Resolves when an elevator is stuck.
	 * @param currentTrips trips currently held by the stuck elevator.
	 */
	private void resolveStuckPassengers(ArrayList<ElevatorTrip> currentTrips) {
		for (ElevatorTrip trip : currentTrips) {
			dropoffs.remove((Integer)trip.getDropoff());
		}
	}
	
	/**
	 * Sends the requestElevator to the scheduler.
	 */
	private void requestElevator() {
		Message request = elevatorRequests.poll();
		updateLights(request);// turns light on
		sendRequest(request);
		Logger.printMessage(request, "SENT");
		
		dropoffs.add(((RequestElevatorMessage)request).getDestination());
		if (this.elevatorRequests.isEmpty()) {
			this.canKill = true;
		}
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
			
			Logger.printMessage(message, "RECEIVED");
			if (message.getType() == MessageType.DOORS_OPENED) {
      
				DoorOpenedMessage openDoorMessage = (DoorOpenedMessage)message;

				updateLights(message); // turns light off

				if (openDoorMessage.getStopType() != StopType.PICKUP) {
					for (int i = 0; i < openDoorMessage.getNumDropoffs(); i++) {
						dropoffs.remove((Integer)openDoorMessage.getArrivedFloor());
					}
				}				
 
			}else if (message.getType() == MessageType.START) {
				this.canStart = true;
			}else if (message.getType() == MessageType.ELEVATOR_STUCK) {
				ElevatorStuckMessage elevatorStuckMessage = (ElevatorStuckMessage)message;
				this.resolveStuckPassengers(elevatorStuckMessage.getCurrentTrips());
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
				this.upLights[floorNum-1] -= doorOpened.getNumPickups();
			} else {
				this.downLights[floorNum-1] -= doorOpened.getNumPickups();
			}
			
		} 
		else {
			RequestElevatorMessage request = (RequestElevatorMessage) message;
			floorNum = request.getFloor();
			direction = request.getDirection();			
			if (direction == DirectionType.UP) {
				this.upLights[floorNum-1]++;
			} else {
				this.downLights[floorNum-1]++;
			}
		}
		
		logger.printLightStatus(this.upLights, this.downLights);
	}
	

	
	/**
	 * Kills the current running instance of the floor.
	 */
	private void kill() {
		this.shouldRun = false;
		KillMessage killMessage = new KillMessage(SenderType.FLOOR, Timer.getTime());
		sendRequest(killMessage);
		Logger.printMessage(killMessage, "SENT");
	}
	
	/**
	 * Returns all the request elevator messages in the floor.
	 * Used for testing.
	 * 
	 * @return
	 */
	public ArrayDeque<Message> getElevatorRequestsList(){
		return elevatorRequests;
	}
	
	/**
	 * Returns the list of all the floor dropoffs.
	 * Used for testing.
	 * 
	 * @return
	 */
	public ArrayList<Integer> getDropoffsList(){
		return dropoffs;
	}
	
	/**
	 * The run function used to logic of the floor.
	 */
	@Override
	public void run() {
		readInElevatorRequests(this.filename);

		while (shouldRun) { // more conditions in the future to ensure all receive messages are accounted for
			if (canStart) {
				while (!this.elevatorRequests.isEmpty() && (elevatorRequests.peek().getTimestamp().compareTo(Timer.getTime()) <= 0)) {
					requestElevator();
				}
			}
			
			requestUpdate();
			
			if (dropoffs.isEmpty() && canKill) {
				kill();
			}
			
			if (canStart) {
				Timer.tick();
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		close();
	}
	
	/**
	 * The starting point for the floor subsystem.
	 * @param args
	 */
	public static void main(String[] args) {
		Thread  floorThread = new Thread(new Floor(Simulator.INPUT, Simulator.NUM_FLOORS), "FLOOR THREAD");
		floorThread.start();
	}
}
