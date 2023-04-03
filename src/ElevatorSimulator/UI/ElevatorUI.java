package ElevatorSimulator.UI;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.Border;

import ElevatorSimulator.Simulator;
import ElevatorSimulator.Elevator.ElevatorInfo;
import ElevatorSimulator.Elevator.ElevatorState;
import ElevatorSimulator.Elevator.ElevatorTrip;
import ElevatorSimulator.Messages.DirectionType;
import ElevatorSimulator.Messages.DoorInterruptMessage;
import ElevatorSimulator.Messages.StopType;

public class ElevatorUI {
	
	private JFrame frame;
	private JButton button;
	
	
	private BoxLayout boxlayout;
    private JPanel mainPanel;
    
    private JPanel elevator;
    private JPanel elevatorShaft;
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
	
	public void floorRequested(int floor, int destination, DirectionType direction, Date timestamp) {
		this.requestJPanel.addRequest(floor, direction);
	}
	
	public void elevatorStuck(int id, Date timestamp) {
		elevators.get(id).elevatorStuck();
	}
	
	public void doorOpened(int elevatorID, int arrivedFloor, int numPickups, int numDropoffs, StopType stopType, Date timestamp) {
		ElevatorPanel elevatorPanel = this.elevators.get(elevatorID);
		String message = "AT: " + arrivedFloor + " PICK: " + numPickups + " DROP: " + numDropoffs;
		
		elevatorPanel.addEvent(message);
	}
	
	public void pickupPerformed(int floor, DirectionType direction) {
		this.requestJPanel.removeRequest(floor, direction);
	}
	
	public void updateElevatorInfo(ElevatorInfo info, DirectionType direction, Date timestamp) {
		ElevatorPanel elevatorPanel = this.elevators.get(info.getElevatorId());
		elevatorPanel.setState(info.getState());
		elevatorPanel.addEvent("AT: " + info.getFloorNumber() + " DIR: " + info.getDirection());
		elevatorPanel.addTripsLights(info.getElevatorTrips());
		
		if (info.getState() == ElevatorState.OPEN || info.getState() == ElevatorState.BOARDING || info.getState() == ElevatorState.CLOSE) {
			elevatorPanel.setElevatorAction(info.getState());
		}
	}
	
	public void doorInterrupted(int id, Date timestamp) {
		elevators.get(id).setElevatorAction(ElevatorState.DOOR_INTERRUPT);
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

