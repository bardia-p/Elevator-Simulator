package ElevatorSimulator.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * represents a floor button lamp on an elevator
 * @author guymorgenshtern
 *
 */
@SuppressWarnings("serial")
public class ElevatorLamp extends JPanel {
	
	private Font boldFont;
	private Font font;
	private JTextField lampStatus;
	
	private static final Color ON_COLOUR = new Color(75, 149, 85);
	
	public ElevatorLamp(int floorNum) {
		
		this.setLayout(new GridLayout(1,2));
		boldFont = new Font("Helvetica",Font.BOLD, 14);
		font = new Font("Helvetica", Font.PLAIN, 14);
		JLabel label = new JLabel("f" + floorNum, SwingConstants.CENTER);

		
		label.setFont(boldFont);
		this.add(label);
		
		lampStatus = new JTextField();
		lampStatus.setHorizontalAlignment(JTextField.CENTER);
		updateLampStatus(false);
		
		this.add(lampStatus);
		
		lampStatus.setFont(font);
		
		this.setVisible(true);
		
		this.setVisible(true);
	}
	
	/**
	 * updates the status of the elevator lamp
	 * @param status
	 */
	public void updateLampStatus(boolean status) {
		lampStatus.setText(status ? "ON" : "OFF");
		lampStatus.setForeground(status ? ON_COLOUR : Color.BLACK);
		lampStatus.setFont(status ? boldFont : font);
	}
}
