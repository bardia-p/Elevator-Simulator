package ElevatorSimulator.UI;

import java.util.ArrayList;
import java.util.Date;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.*;

import ElevatorSimulator.Simulator;
import ElevatorSimulator.Elevator.ElevatorInfo;
import ElevatorSimulator.Elevator.ElevatorState;
import ElevatorSimulator.Elevator.ElevatorTrip;
import ElevatorSimulator.Messages.DirectionType;
import ElevatorSimulator.Messages.StopType;

/**
 * main graphical class for the Elevator Simulator UI, made up of more components
 * @author Guy Morgenshtern
 *
 */
public class ElevatorUI {
	
	private JFrame frame;
	
	
    private JPanel mainPanel;
    
    private FloorRequestPanel requestJPanel;
    
    private ArrayList<ElevatorPanel> elevators;
    private ElevatorMessageLogPanel[] elevatorLogs;

    

	public ElevatorUI() {
		frame = new JFrame("Console System");//creating instance of JFrame  
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(Toolkit.getDefaultToolkit(). getScreenSize());

		elevatorLogs = new ElevatorMessageLogPanel[Simulator.NUM_ELEVATORS];
		
		JPanel messageLogPanel = new JPanel(new GridLayout(Simulator.NUM_ELEVATORS, 1));
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1, Simulator.NUM_ELEVATORS + 2));
		
		elevators = new ArrayList<>();
		
		frame.add(mainPanel);
		for (int i = 0; i < Simulator.NUM_ELEVATORS; i++) {
			ElevatorPanel e = new ElevatorPanel(i + 1);
			elevators.add(e);
			mainPanel.add(e);
			
			elevatorLogs[i] = new ElevatorMessageLogPanel(i+1);
			messageLogPanel.add(elevatorLogs[i]);
		}
		
		
		requestJPanel = new FloorRequestPanel();
		mainPanel.add(requestJPanel);
		
		mainPanel.add(messageLogPanel);
        
		mainPanel.setBackground(Color.gray);

		frame.setVisible(true);

	}
	
	/**
	 * edits floor request panel based on request passed
	 * @param floor
	 * @param destination
	 * @param direction
	 * @param timestamp
	 */
	public void floorRequested(int floor, int destination, DirectionType direction, Date timestamp) {
		this.requestJPanel.addRequest(floor, direction);
	}
	
	/**
	 * calls elevator stuck on the ElevatorPanel representing that elevator
	 * @param id
	 * @param timestamp
	 */
	public void elevatorStuck(int id, Date timestamp) {
		elevators.get(id).elevatorStuck();
		elevatorLogs[id].addEvent(timestamp, "ELEVATOR STUCK");
	}
	
	/**
	 * Adds event to ElevatorPanel based DoorsOpened message
	 * @param elevatorID
	 * @param arrivedFloor
	 * @param numPickups
	 * @param numDropoffs
	 * @param stopType
	 * @param timestamp
	 */
	public void doorOpened(int elevatorID, DirectionType direction, int arrivedFloor, int numPickups, int numDropoffs, StopType stopType, Date timestamp) {
		ElevatorPanel elevatorPanel = this.elevators.get(elevatorID);
		String message = "AT: " + arrivedFloor + " PICK: " + numPickups + " DROP: " + numDropoffs;
		
		elevatorPanel.addEvent(message);
		
		if (stopType == StopType.DROPOFF || stopType == StopType.PICKUP_AND_DROPOFF) {
			elevatorPanel.dropoffAtFloor(arrivedFloor);
			dropoffPerformed(elevatorID, arrivedFloor, direction, numDropoffs, timestamp);
		}
		
		if (stopType == StopType.PICKUP || stopType == StopType.PICKUP_AND_DROPOFF) {
			pickupPerformed(elevatorID, arrivedFloor, direction,  numPickups, timestamp);
		}

	}
	
	/**
	 * edits floor request panel when passenger is picked up in order to turn off request light
	 * @param floor
	 * @param direction
	 * @param numPickups
	 */
	private void pickupPerformed(int id, int floor, DirectionType direction, int numPickups, Date timestamp) {
		this.elevatorLogs[id].addEvent(timestamp, numPickups + " PICKUP(s) at: " + floor + " " + direction);
		this.requestJPanel.removeRequest(floor, direction, numPickups);
	}
	
	private void dropoffPerformed(int id, int floor, DirectionType direction, int numDropoffs, Date timestamp) {
		this.elevatorLogs[id].addEvent(timestamp, numDropoffs + " DROPOFF(s) at: " + floor + " " + direction);
	}
	
	/**
	 * used to update the state and elevator lamps of a given elevator
	 * @param info
	 * @param direction
	 * @param timestamp
	 */
	public void updateElevatorInfo(ElevatorInfo info, DirectionType direction, Date timestamp) {
		ElevatorPanel elevatorPanel = this.elevators.get(info.getElevatorId());
		elevatorPanel.setState(info.getState());
		elevatorPanel.addEvent("AT: " + info.getFloorNumber() + " DIR: " + info.getDirection());
		elevatorPanel.addTripsLights(info.getElevatorTrips());
		elevatorPanel.setElevatorAction(info.getState(), info.getDirection());
		
		for (ElevatorTrip trip : info.getElevatorTrips()) {
			elevatorLogs[info.getElevatorId()].addEvent(timestamp, "REQUEST at " + trip.getPickup() + " to " + trip.getDropoff());
		}
		
	}
	
	/**
	 * changes graphical state to doors interrupted 
	 * @param id
	 * @param timestamp
	 */
	public void doorInterrupted(int id, Date timestamp, DirectionType directionType) {
		elevators.get(id).setElevatorAction(ElevatorState.DOOR_INTERRUPT, directionType);
		elevatorLogs[id].addEvent(timestamp, "DOOR INTERRUPT");
	}
	
	
	public static void main(String args[]) {
		ElevatorUI ui = new ElevatorUI();
		
		for (int i = 0; i < 100; i++) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			ui.elevators.get(i % (Simulator.NUM_ELEVATORS)).addEvent("Elevator at " + i);
		}
		
	}
	
}

