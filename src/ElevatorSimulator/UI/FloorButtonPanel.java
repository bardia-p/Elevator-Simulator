package ElevatorSimulator.UI;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FloorButtonPanel extends JPanel{
	private JCheckBox upLight;
	private JCheckBox downLight;

	public FloorButtonPanel(int floor) {
		this.setLayout(new BorderLayout());
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1, 2));
		
		this.add(buttons, BorderLayout.CENTER);
		this.add(new JLabel("f" + floor), BorderLayout.WEST);
		
		this.setVisible(true);
		
		upLight = new JCheckBox("up");
		downLight = new JCheckBox("down");
		upLight.setEnabled(false);
		downLight.setEnabled(false);
		
		buttons.add(upLight);
		buttons.add(downLight);
		this.setVisible(true);
	}
	
	public void setUpLight(boolean value) {
		upLight.setSelected(value);
	}
	
	public void setDownLight(boolean value) {
		upLight.setSelected(value);
	}
}
