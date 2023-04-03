package ElevatorSimulator.UI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * each instance represents the state of the up and down buttons of the given floor
 * @author guymorgenshtern
 *
 */
public class FloorButtonPanel extends JPanel{
	private JTextField upLight;
	private JTextField downLight;
	private Font logFont;

	public FloorButtonPanel(int floor) {
		this.setLayout(new BorderLayout());
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1, 2));
		logFont = new Font("Helvetica", Font.PLAIN, 14);
		
		this.add(buttons, BorderLayout.CENTER);
		this.add(new JLabel("f" + floor), BorderLayout.WEST);
		this.setFont(logFont);
		
		this.setVisible(true);
		
		upLight = new JTextField("");
		downLight = new JTextField("");

		
		buttons.add(upLight);
		buttons.add(downLight);
		this.setVisible(true);
	}
	
	/**
	 * sets value of the up lamp
	 * @param value
	 */
	public void setUpLight(boolean value) {
		if (value) {
			upLight.setText("UP");
		} else {
			upLight.setText("");
		}
	}
	
	/**
	 * sets value of the down lamp
	 * @param value
	 */
	public void setDownLight(boolean value) {
		if (value) {
			downLight.setText("DOWN");
		} else {
			downLight.setText("");
		}
	}
}
