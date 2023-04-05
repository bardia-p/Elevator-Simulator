package ElevatorSimulator.UI;


import java.awt.GridLayout;

import javax.swing.JPanel;

import ElevatorSimulator.Simulator;
import ElevatorSimulator.Messages.DirectionType;

@SuppressWarnings("serial")
public class FloorRequestPanel extends JPanel {
	
	private FloorButtonPanel[] floors;
	private int upRequests[];
	private int downRequests[];
	
	public FloorRequestPanel() {
		super();
		
		this.setLayout(new GridLayout(Simulator.NUM_FLOORS, 1));
        
		upRequests = new int[Simulator.NUM_FLOORS];
		downRequests = new int[Simulator.NUM_FLOORS];
		floors = new FloorButtonPanel[Simulator.NUM_FLOORS];
		
		for (int i = 0; i < floors.length; i++) {
			floors[i] = new FloorButtonPanel(i+1);
			this.add(floors[i]);
		}
		
        
        setVisible(true);
	}
	
	public void addRequest(int floor, DirectionType direction) {
		if (direction == DirectionType.UP) {
			upRequests[floor-1]++;
		} else {
			downRequests[floor-1]++;
		}	
		
		updateLights();
	}
	
	public void removeRequest(int floor, DirectionType direction) {
		if (direction == DirectionType.UP) {
			upRequests[floor-1]--;
		} else {
			downRequests[floor-1]--;
		}	
		
		updateLights();
	}
	
	private void updateLights() {
		
		for (int i = 0; i < Simulator.NUM_ELEVATORS; i++) {
			if (upRequests[i] > 0) {
				floors[i].setUpLight(true);
			} else {
				floors[i].setUpLight(false);
			}
			
			if (downRequests[i] > 0) {
				floors[i].setDownLight(true);
			} else {
				floors[i].setDownLight(false);
			}
		}
	}

}
