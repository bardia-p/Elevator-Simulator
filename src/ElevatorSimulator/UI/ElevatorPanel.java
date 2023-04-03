package ElevatorSimulator.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import ElevatorSimulator.Simulator;
import ElevatorSimulator.Elevator.ElevatorState;
import ElevatorSimulator.Elevator.ElevatorTrip;

public class ElevatorPanel extends JPanel {
	
	JPanel headerJPanel;
	JPanel eventLogJPanel;
	Font titleFont;
	Font headerFont;
	Font logFont;
	Font stateFont;
	Font actionFont;
	
	
	JTextArea log;
	JTextArea stateArea;
	JTextField currArea;
	JPanel midPanel;
	JTextField elevatorAction;
	JPanel logPanel;
	
	private boolean elevatorLights[];
	
	private int id;
	public ElevatorPanel(int id) {
		super();
		
		elevatorLights = new boolean[Simulator.NUM_FLOORS];
		Arrays.fill(elevatorLights, false);
		titleFont = new Font("Helvetica", Font.PLAIN, 24);
		this.setFont(titleFont);
		headerFont = new Font("Helvetica", Font.PLAIN, 18);
		logFont = new Font("Helvetica", Font.PLAIN, 14);
		stateFont = new Font("Helvetica", Font.PLAIN, 18);
		actionFont = new Font("Helvetica", Font.PLAIN, 48);
		
		this.id = id;
		
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
        log.setAlignmentX(0.0f);
        
        midPanel = new JPanel(new GridLayout(2, 1));
        midPanel.setSize(this.getSize());
        
        logPanel = new JPanel();
        logPanel.add(log);
        logPanel.setBackground(Color.WHITE);
 
        
        elevatorAction = new JTextField();
        elevatorAction.setFont(actionFont);
        elevatorAction.setHorizontalAlignment(JTextField.CENTER);
        setElevatorAction(ElevatorState.CLOSE);
        
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
        
        currArea = new JTextField("");
        eventLogJPanel.add(currArea, BorderLayout.NORTH);
        currArea.setFont(headerFont);
        
        
        currArea.setEditable(false);
        stateArea.setEditable(false);

        JPanel filler = new JPanel();
        this.add(filler, BorderLayout.SOUTH);
        filler.setPreferredSize(new Dimension(0, 0));
        setVisible(true);
	}
	
	public void elevatorStuck() {
		elevatorAction.setForeground(Color.RED);
	}
	
	public void addEvent(String message) {
		log.setText(getLightStatus());
		currArea.setText(">" + message);

	}
	
	public void setElevatorAction(ElevatorState state) {
		if (state == ElevatorState.OPEN) {
			elevatorAction.setText("|] [|");
		} else if (state == ElevatorState.BOARDING) {
			elevatorAction.setText("|]  [|");
		} else if (state == ElevatorState.DOOR_INTERRUPT) {
			elevatorAction.setText("|] | [|");
		} else {
			elevatorAction.setText("|][|");
		}
	}
	
	public void setState(ElevatorState newState) {
		stateArea.setText(newState.toString());
	}
	
	public void addTripsLights(ArrayList<ElevatorTrip> trips) {
		Arrays.fill(elevatorLights, false);
		for (ElevatorTrip trip : trips) {
			if (trip.isPickedUp()) {
				elevatorLights[trip.getDropoff()] = true;
			}
			
		}
	}
	
	private String getLightStatus() {
		String m = "";
		
		for (int i=0; i < elevatorLights.length; i++) {
			m += "f" + (i+1) + " ";
			m += elevatorLights[i] ? "ON\n" : "OFF\n";
		}
		return m;
	}
}
