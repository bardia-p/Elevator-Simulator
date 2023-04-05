package ElevatorSimulator.UI;


import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.JPanel;

import ElevatorSimulator.Simulator;
import ElevatorSimulator.Messages.DirectionType;

@SuppressWarnings("serial")

/**
 * Represents UP DOWN elevator request buttons on a floor 
 * @author Guy Morgenshtern
 *
 */
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
		
		Arrays.fill(upRequests, 0);
		Arrays.fill(downRequests, 0);
        
        setVisible(true);
	}
	
	/**
	 * adds request to outstanding requests, helps keep light on until every request is picked up
	 * @param floor
	 * @param direction
	 */
	
	public void addRequest(int floor, DirectionType direction) {
		if (direction == DirectionType.UP) {
			upRequests[floor-1]++;
		} else {
			downRequests[floor-1]++;
		}	
		
		updateLights();
	}
	
	/**
	 * removes request from outstanding requests, helps keep light on until every request is picked up
	 * @param floor
	 * @param direction
	 * @param numPickups
	 */
	public void removeRequest(int floor, DirectionType direction, int numPickups) {
		if (direction == DirectionType.UP) {
			upRequests[floor-1] -= numPickups;
		} else {
			downRequests[floor-1] -= numPickups;
		}	
		
		updateLights();
	}
	
	/**
	 * updates floor request lamps
	 */
	private void updateLights() {
		
		for (int i = 0; i < Simulator.NUM_FLOORS; i++) {
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
