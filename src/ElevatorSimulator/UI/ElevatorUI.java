package ElevatorSimulator.UI;

import java.util.Date;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.Border;

import ElevatorSimulator.Elevator.ElevatorInfo;
import ElevatorSimulator.Messages.DirectionType;
import ElevatorSimulator.Messages.StopType;

public class ElevatorUI {
	
	private JFrame frame;
	private JButton button;
	
	private HashMap<JPanel, JPanel[]> elevators;
	
	
	
	private BoxLayout boxlayout;
    private JPanel mainPanel;
    
    private JPanel elevator;
    private JPanel elevatorShaft;
    

	public ElevatorUI() {
		frame = new JFrame("Console System");//creating instance of JFrame  
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 1000);
        
		button = new JButton("click");//creating instance of JButton  
		//this.buttonAction();

		button.addActionListener(new ActionListener(){  
		public void actionPerformed(ActionEvent e){  
			
			Timer timer = new Timer(500, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent ae) {  
                	
                	elevator.setLocation(elevator.getLocation().x, elevator.getLocation().y - 10);

                }
            });
            timer.setRepeats(true);
            timer.setInitialDelay(0);
            timer.start(); 
			
			

		        }  
		});  
		
		mainPanel = new JPanel();
				
        boxlayout = new BoxLayout(mainPanel, BoxLayout.X_AXIS);

        
		this.elevators = new HashMap<>();
		this.elevator = new JPanel();

		this.makeConsole();
	}
	
	
	private void makeConsole() {
		
		//mainPanel.setLayout(boxlayout);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,100,10));
        
		mainPanel.setBackground(Color.gray);
        
		mainPanel.add(button);
		frame.add(mainPanel);
		          

		this.update();
		
		//this.createElevatorEntry();
		this.createElevatorShaft();
		

		

	}
	
	private void buttonAction() {
		button.addActionListener(new ActionListener(){  
		public void actionPerformed(ActionEvent e){  
			
			Timer timer = new Timer(500, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent ae) {  
                	
                	elevator.setLocation(elevator.getLocation().x, elevator.getLocation().y - 10);

                }
            });
            timer.setRepeats(true);
            timer.setInitialDelay(0);
            timer.start(); 
			
			

		        }  
		});  
	}
	
	private void createElevatorShaft() {
		
		elevatorShaft = new JPanel();
		
	        elevatorShaft.setPreferredSize(new Dimension(200, 700));
			
	        elevatorShaft.setBackground(Color.white);
			
			
			
			elevator.setPreferredSize(new Dimension(150, 200));
			elevator.setBackground(Color.black);
			
			elevatorShaft.add(elevator);
			
			
			mainPanel.add(elevatorShaft);

		
	}
	
//	private void createElevatorEntry() {
//		
//		for (int i = 0; i < 3; i++) {
//			JPanel e = new JPanel();
//			
//			JPanel doorLeft = new JPanel();
//			JPanel doorRight = new JPanel();
//			
//			JPanel[] doorArray = new JPanel[]{doorLeft, doorRight};
//			
//	        BoxLayout eBL = new BoxLayout(e, BoxLayout.X_AXIS);
//	        
//	        e.setLayout(eBL);
//
//			doorLeft.setBackground(Color.white);
//			doorRight.setBackground(Color.white);
//
//			
//		    Border blackline = BorderFactory.createLineBorder(Color.black);
//
//	        doorLeft.setBorder(blackline);
//	        doorRight.setBorder(blackline);
//	        
//	        doorLeft.setPreferredSize(new Dimension(100, 50));
//	        doorRight.setPreferredSize(new Dimension(100, 50));
//
//			e.add(doorLeft);
//			e.add(doorRight);
//			
//			
//						
//			this.elevators.put(e, doorArray);
//
//	        
//	        if (i == 0) {
//	        	e.setBackground(Color.black);
//	        }
//	        else if (i == 1) {
//	        	e.setBackground(Color.red);
//
//	        }
//	        else {
//	        	e.setBackground(Color.yellow);
//
//	        }
//	        
//	        mainPanel.add(e);
//
//		}
//
//		this.update();
//		
//	}
	
	private void update() {

		frame.setVisible(true);//making the frame visible  
	}
	
	public void floorRequested(int floor, int destination, DirectionType direction, Date timestamp) {
		
	}
	
	public void elevatorStuck(int id, Date timestamp) {
		
	}
	
	public void doorOpened(int arrivedFloor, int numPickups, int numDropoffs, StopType stopType, Date timestamp) {
		
	}	
	
	public void updateElevatorInfo(ElevatorInfo info, DirectionType direction, Date timestamp) {
		
	}
	
	
	public static void main(String args[]) {
		new ElevatorUI();
	}
	
}

