package ElevatorSimulator.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.Border;
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
	
	private ElevatorLamp elevatorLamps[];
	
	public ElevatorPanel(int id) {
		super();
	
		
		titleFont = new Font("Helvetica", Font.PLAIN, 24);
		this.setFont(titleFont);
		headerFont = new Font("Helvetica", Font.PLAIN, 18);
		logFont = new Font("Helvetica", Font.PLAIN, 14);
		stateFont = new Font("Helvetica", Font.PLAIN, 18);
		actionFont = new Font("Helvetica", Font.PLAIN, 32);
		Border border =  BorderFactory.createLineBorder(Color.GRAY);
				
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
        
        logPanel = new JPanel();
        logPanel.setLayout(new GridLayout((int) Math.ceil(Simulator.NUM_FLOORS/2.0), 2));
        
		elevatorLamps = new ElevatorLamp[Simulator.NUM_FLOORS];
		initElevatorLamps();
       
        
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
        
        stateArea.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(0, 10, 10, 10)));
        eventLogJPanel.add(stateArea, BorderLayout.SOUTH);
        stateArea.setFont(stateFont);
        
        currArea = new JTextArea("");
        currArea.setBorder(border);
        eventLogJPanel.add(currArea, BorderLayout.NORTH);
        currArea.setFont(headerFont);
        
        
        currArea.setEditable(false);
        stateArea.setEditable(false);

        JPanel filler = new JPanel();
        this.add(filler, BorderLayout.SOUTH);
        filler.setPreferredSize(new Dimension(0, 0));
        setVisible(true);
	}
	
	private void initElevatorLamps() {
		for (int i = 0; i < elevatorLamps.length; i++) {
			elevatorLamps[i] = new ElevatorLamp(i + 1);
			logPanel.add(elevatorLamps[i]);
		}
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
		for (ElevatorTrip trip : trips) {
			if (trip.isPickedUp()) {
				elevatorLamps[trip.getDropoff()-1].updateLampStatus(true);
			}
			
		}
	}
	
	public void dropoffAtFloor(int floor) {
		elevatorLamps[floor-1].updateLampStatus(false);
	}

}
