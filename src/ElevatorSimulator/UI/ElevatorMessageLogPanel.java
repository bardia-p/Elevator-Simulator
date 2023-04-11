package ElevatorSimulator.UI;

import java.awt.BorderLayout;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * The elevator log panel for displaying the elevator messages. 
 * 
 * @author Guy Morgenshtern
 *
 */
@SuppressWarnings("serial")
public class ElevatorMessageLogPanel extends JPanel{
	
	private JTextPane log;
	private ArrayList<String> messages;
	
	public ElevatorMessageLogPanel(int id) {
		this.setLayout(new BorderLayout());
		log = new JTextPane();
		this.add(new JLabel("Elevator0" + id), BorderLayout.NORTH);
		messages = new ArrayList<>();
		
		JScrollPane scrollPane = new JScrollPane(log);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setAutoscrolls(true);
		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	/**
	 * Displays a given event for the elevator.
	 * 
	 * @param timestamp
	 * @param message
	 */
	public void addEvent(Date timestamp, String message) {
		String m = "";
		if (timestamp != null) {
			Timestamp ts= new Timestamp(timestamp.getTime());  
	        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");  
	        m = formatter.format(ts) + ": ";
		}
        
        m += message;
        
        if(!messages.contains(m)) {
        	this.log.setText(m + "\n"  + log.getText());
        	this.messages.add(m);
        }
		
		
		
	}
}
