package ElevatorSimulator.UI;


import java.awt.GridLayout;

import javax.swing.JPanel;

import ElevatorSimulator.Simulator;
import ElevatorSimulator.Messages.DirectionType;

@SuppressWarnings("serial")
public class FloorRequestPanel extends JPanel {
	
	private FloorButtonPanel[] floors;
	
	public FloorRequestPanel() {
		super();
		
		this.setLayout(new GridLayout(Simulator.NUM_FLOORS, 1));
        
		floors = new FloorButtonPanel[Simulator.NUM_FLOORS];
		
		for (int i = 0; i < floors.length; i++) {
			floors[i] = new FloorButtonPanel(i+1);
			this.add(floors[i]);
		}
		
        
        setVisible(true);
	}
	
	public void addRequest(int floor, DirectionType direction) {
		if (direction == DirectionType.UP) {
			floors[floor-1].setUpLight(true);
		} else {
			floors[floor-1].setDownLight(true);
		}	
	}
	
	public void removeRequest(int floor, DirectionType direction) {
		if (direction == DirectionType.UP) {
			floors[floor-1].setUpLight(false);
		} else {
			floors[floor-1].setDownLight(false);
		}	
	}

}
