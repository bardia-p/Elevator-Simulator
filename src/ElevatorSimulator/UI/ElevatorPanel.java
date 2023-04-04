package ElevatorSimulator.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ElevatorSimulator.Simulator;
import ElevatorSimulator.Elevator.ElevatorState;
import ElevatorSimulator.Elevator.ElevatorTrip;
import ElevatorSimulator.Messages.DirectionType;

/**
 * each panel represents 1 elevator moving up and down the shaft
 * @author Guy Morgenshtern
 *
 */
@SuppressWarnings("serial")
public class ElevatorPanel extends JPanel {
	
	private JPanel headerJPanel;
	private JPanel eventLogJPanel;
	private Font titleFont;
	private Font headerFont;
	private Font logFont;
	private Font stateFont;
	private Font actionFont;
	
	
	private JTextArea log;
	private JTextArea stateArea;
	private JTextArea currArea;
	private JPanel midPanel;
	private JTextPane elevatorAction;
	private JPanel logPanel;
	
	private boolean elevatorLights[];
	
	public ElevatorPanel(int id) {
		super();
		
		elevatorLights = new boolean[Simulator.NUM_FLOORS];
		Arrays.fill(elevatorLights, false);
		titleFont = new Font("Helvetica", Font.PLAIN, 24);
		this.setFont(titleFont);
		headerFont = new Font("Helvetica", Font.PLAIN, 18);
		logFont = new Font("Helvetica", Font.PLAIN, 14);
		stateFont = new Font("Helvetica", Font.PLAIN, 18);
		actionFont = new Font("Helvetica", Font.PLAIN, 32);
				
        this.setLayout(new BorderLayout());
        setSize(1200/Simulator.NUM_ELEVATORS,1000);
        
        //Main elevator panel format
        headerJPanel = new JPanel();
        eventLogJPanel = new JPanel();
        eventLogJPanel.setLayout(new BorderLayout());
  
        
        this.add(headerJPanel, BorderLayout.NORTH);
        this.add(eventLogJPanel, BorderLayout.CENTER);
       
        //header content
        JLabel headerLabel = new JLabel("elevator0" + id);
        headerLabel.setFont(titleFont);
        headerJPanel.add(headerLabel);
        

        //event content
        log = new JTextArea();
        log.setEditable(false);
       
        log.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        midPanel = new JPanel(new GridLayout(2, 1));
        midPanel.setSize(this.getSize());
        
        logPanel = new JPanel(new BorderLayout());
        logPanel.add(log, BorderLayout.WEST);
        
        logPanel.setBackground(Color.WHITE);
 
        
        elevatorAction = new JTextPane();
        elevatorAction.setFont(actionFont);
        StyledDocument doc = elevatorAction.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        elevatorAction.setEditable(false);
        setElevatorAction(ElevatorState.CLOSE, DirectionType.UP);
        
        midPanel.add(elevatorAction);
        
        log.setFont(logFont);

      
        JScrollPane scrollPane = new JScrollPane(logPanel);  
        
        midPanel.add(scrollPane);
        
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setAutoscrolls(true);
        eventLogJPanel.add(midPanel, BorderLayout.CENTER);
        
        stateArea = new JTextArea("BOARDING");
        eventLogJPanel.add(stateArea, BorderLayout.SOUTH);
        stateArea.setFont(stateFont);
        
        currArea = new JTextArea("");
        eventLogJPanel.add(currArea, BorderLayout.NORTH);
        currArea.setFont(headerFont);
        
        
        currArea.setEditable(false);
        stateArea.setEditable(false);

        JPanel filler = new JPanel();
        this.add(filler, BorderLayout.SOUTH);
        filler.setPreferredSize(new Dimension(0, 0));
        setVisible(true);
	}
	
	/**
	 * Changes state and colour of elevator UI to reflect stuck state
	 */
	public void elevatorStuck() {
		setState(ElevatorState.ELEVATOR_STUCK);
		elevatorAction.setForeground(Color.RED);
	}
	
	/**
	 * adds event to the current event text box and refreshes the elevator light log
	 * @param message
	 */
	public void addEvent(String message) {
		log.setText(getLightStatus());
		currArea.setText(">" + message);

	}
	
	/**
	 * sets graphical state of elevator
	 * @param state
	 */
	public void setElevatorAction(ElevatorState state, DirectionType direction) {
		String elevatorRopeString = "";
		
		if (state == ElevatorState.CLOSE) {
			elevatorRopeString = (direction == DirectionType.DOWN) ? "\n|\n|\n|\n|\n|\n" : "\n|\n|\n|\n";
		} else {
			elevatorRopeString = "\n|\n|\n|\n|\n";
		}
		
		String elevatorTextString = "--------------\n";
		
		if (state == ElevatorState.OPEN) {
			elevatorTextString += "|     []  []     |\n|     []  []     |\n";
		} else if (state == ElevatorState.BOARDING) {
			elevatorTextString += "|    []    []    |\n|    []    []    |\n";
		} else if (state == ElevatorState.DOOR_INTERRUPT) {
			elevatorTextString += "|   []  |  []   |\n|   []  |  []   |\n";
		} else {
			elevatorTextString += "|      [][]      |\n|      [][]      |\n";
		}
		
		elevatorTextString += "--------------\n";
		
		elevatorAction.setText(elevatorRopeString + elevatorTextString);
	}
	
	/**
	 * changes state text area of elevator UI
	 * @param newState
	 */
	public void setState(ElevatorState newState) {
		stateArea.setText(newState.toString());
		if (newState == ElevatorState.DOOR_INTERRUPT) {
			elevatorAction.setForeground(Color.ORANGE);
		} else {
			elevatorAction.setForeground(Color.BLACK);
		}
	}
	
	/**
	 * determines elevator lamps that should be on based on current trips
	 * @param trips
	 */
	public void addTripsLights(ArrayList<ElevatorTrip> trips) {
		Arrays.fill(elevatorLights, false);
		for (ElevatorTrip trip : trips) {
			if (trip.isPickedUp()) {
				elevatorLights[trip.getDropoff()-1] = true;
			}
			
		}
	}
	
	/**
	 * returns a formatted string of all elevator lights with their statuses 
	 * @return
	 */
	private String getLightStatus() {
		String m = "";
		
		for (int i=0; i < elevatorLights.length; i++) {
			m += "f" + (i+1) + " ";
			m += elevatorLights[i] ? "ON\n" : "OFF\n";
		}
		return m;
	}
}
