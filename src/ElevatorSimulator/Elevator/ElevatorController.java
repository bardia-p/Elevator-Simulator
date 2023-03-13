package ElevatorSimulator.Elevator;

import java.util.ArrayList;

import ElevatorSimulator.Logger;
import ElevatorSimulator.Simulator;
import ElevatorSimulator.Messages.MessageType;
import ElevatorSimulator.Messages.ReadyMessage;
import ElevatorSimulator.Messages.StartMessage;
import ElevatorSimulator.Messaging.ClientRPC;
import ElevatorSimulator.Scheduler.Scheduler;

/**
 * The Elevator controller class responsible for controlling the multiple
 * elevators
 * 
 * @author Andre Hazim,
 * @author Bardia Parmoun
 */
public class ElevatorController extends ClientRPC implements Runnable {
	// Keeps track of all the elevators.
	private ArrayList<Elevator> elevators;

	// Keeps track of the number of elevators.
	private int numElevators;

	// Keeps track of the number of floors.
	private int numFloors;

	/**
	 * The constructor of the Elevator Controller
	 * 
	 * @param queue        The message queue of the system
	 * @param numElevators The number of Elevators
	 * @param numFloors    The number of Floors
	 */
	public ElevatorController(int numElevators, int numFloors) {
		super(Scheduler.ELEVATOR_PORT);
		this.elevators = new ArrayList<>();
		this.numElevators = numElevators;
		this.numFloors = numFloors;
	}

	/**
	 * Starts the number of elevator threads specified in the constructor
	 */
	private void initializeElevators() {
		for (int i = 0; i < numElevators; i++) {
			Elevator elevator = new Elevator(i, this.numFloors);
			ReadyMessage readyMessage = new ReadyMessage(MessageType.READY, new ElevatorInfo(elevator.getDirection(),
					elevator.getState(), elevator.getFloorNumber(), elevator.getID(), elevator.getNumTrips()));
			sendRequest(readyMessage);
			Logger.printMessage(readyMessage, "SENT");
			elevators.add(elevator);
		}

		StartMessage startMessage = new StartMessage();

		sendRequest(startMessage);
		Logger.printMessage(startMessage, "SENT");

		for (int i = 0; i < numElevators; i++) {
			Thread elevatorThread = new Thread(elevators.get(i), "ELEVATOR " + (i + 1));
			elevatorThread.start();
		}
	}

	@Override
	/**
	 * Starts all the elevator threads
	 */
	public void run() {
		initializeElevators();
		close();
	}

	/**
	 * The main program for the elevator controller.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Thread elevatorControllerThread = new Thread(
				new ElevatorController(Simulator.NUM_ELEVATORS, Simulator.NUM_FLOORS), "ELEVATOR CONTROLLER");
		elevatorControllerThread.start();
	}
}