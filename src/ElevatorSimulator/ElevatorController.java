package ElevatorSimulator;

import java.util.ArrayList;

import ElevatorSimulator.Messages.KillMessage;
import ElevatorSimulator.Messages.RequestElevatorMessage;

public class ElevatorController implements Runnable {
	
	private ArrayList<Elevator> elevators;
	private int numElevators;
	private int numFloors;
	private MessageQueue queue;
	
	public ElevatorController(MessageQueue queue, int numElevators, int numFloors) {
		this.elevators = new ArrayList<>();
		this.numElevators = numElevators;
		this.queue = queue;		
		this.numFloors = numFloors;
	}
	
	private void initializeElevators() {
		for (int i = 0; i < numElevators; i++) {
			Elevator elevator = new Elevator(queue, i, this.numFloors);
			elevators.add(elevator);
			queue.addElevator();
			Thread elevatorThread = new Thread(elevator, "ELEVATOR " + (i+1));
			elevatorThread.start();
		}
	}
	
	private boolean checkElevatorValid(Elevator elevator) {
		
		if (elevator.getState() == ElevatorState.POLL) {
			return true;
		}
		
		return false;
		
	}
	
	public ArrayList<Elevator> getAvailableElevators(RequestElevatorMessage message){
		ArrayList<Elevator> availableElevators = new ArrayList<>();

		for (Elevator e : elevators) {
			if (checkElevatorValid(e)) {
				availableElevators.add(e);
			}
			
		}
		return availableElevators;
	}
	
	public void kill(KillMessage message) {
		for (int i =0; i < elevators.size(); i++) {
			queue.replyToElevator(message, i);
		}
	}

	@Override
	public void run() {
		initializeElevators();
	}
}