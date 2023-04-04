package ElevatorSimulator.UI;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.*;

import ElevatorSimulator.Simulator;
import ElevatorSimulator.Elevator.ElevatorInfo;
import ElevatorSimulator.Elevator.ElevatorState;
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
    

	public ElevatorUI() {
		frame = new JFrame("Console System");//creating instance of JFrame  
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 1000);


		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1, Simulator.NUM_ELEVATORS + 1));
		
		elevators = new ArrayList<>();
		
		frame.add(mainPanel);
		for (int i = 0; i < Simulator.NUM_ELEVATORS; i++) {
			ElevatorPanel e = new ElevatorPanel(i + 1);
			elevators.add(e);
			mainPanel.add(e);
		}
		
		
		requestJPanel = new FloorRequestPanel();
		mainPanel.add(requestJPanel);
        
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
	public void doorOpened(int elevatorID, int arrivedFloor, int numPickups, int numDropoffs, StopType stopType, Date timestamp) {
		ElevatorPanel elevatorPanel = this.elevators.get(elevatorID);
		String message = "AT: " + arrivedFloor + " PICK: " + numPickups + " DROP: " + numDropoffs;
		
		elevatorPanel.addEvent(message);
	}
	
	/**
	 * edits floor request panel when passenger is picked up in order to turn off request light
	 * @param floor
	 * @param direction
	 */
	public void pickupPerformed(int floor, DirectionType direction) {
		this.requestJPanel.removeRequest(floor, direction);
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
		elevatorPanel.addEvent("AT: " + info.getFloorNumber() + "\n DIR: " + info.getDirection());
		elevatorPanel.addTripsLights(info.getElevatorTrips());
		
		if (info.getState() == ElevatorState.OPEN || info.getState() == ElevatorState.BOARDING || info.getState() == ElevatorState.CLOSE) {
			elevatorPanel.setElevatorAction(info.getState(), info.getDirection());
		}
	}
	
	/**
	 * changes graphical state to doors interrupted 
	 * @param id
	 * @param timestamp
	 */
	public void doorInterrupted(int id, Date timestamp, DirectionType directionType) {
		elevators.get(id).setElevatorAction(ElevatorState.DOOR_INTERRUPT, directionType);
	}
	
	
	public static void main(String args[]) {
		ElevatorUI ui = new ElevatorUI();
		
		for (int i = 0; i < 100; i++) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ui.elevators.get(i % (Simulator.NUM_ELEVATORS)).addEvent("Elevator at " + i);
		}
		
	}
	
}
