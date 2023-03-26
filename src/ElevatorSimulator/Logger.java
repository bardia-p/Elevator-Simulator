package ElevatorSimulator;

import ElevatorSimulator.Messages.ArrivedElevatorMessage;
import ElevatorSimulator.Messages.DoorOpenedMessage;
import ElevatorSimulator.Messages.ElevatorStuckMessage;
import ElevatorSimulator.Messages.Message;
import ElevatorSimulator.Messages.MessageType;

/**
 * The logger class in charge of printing the messages.
 * 
 * @author Guy Morgenshtern
 * @author Sarah Chow
 *
 */
public class Logger {
	/**
	 * Print the message contents.
	 * 
	 * @param m, the message to print.
	 * @param type, the type status to print.
	 */
	public static void printMessage(Message m, String type) {
		String result = "";
				
		if (m != null && m.getType() != MessageType.EMPTY) {
			
			result += "\n" + new String(new char[40]).replace("\0", "-") + Thread.currentThread().getName() 
					+ new String(new char[40]).replace("\0", "-") + "\n";
			result += String.format("| %-30s | %-20s | %-10s | %-10s | %-5s |", "TIME", "REQUEST", "ACTION", "RECEIVED", "SENT");
			
			if (m.getType() == MessageType.ELEVATOR_STUCK) {
				result += String.format(" %-5s | %-12s |" , "ERROR", "ELEVATOR ID");
				result += "\n" + new String(new char[114]).replace("\0", "-");
			}
			else {
				result += "\n" + new String(new char[91]).replace("\0", "-");
			}
						
			result += String.format("\n| %-30s | ",  m.getTimestamp());
			
			if (m.getType() == MessageType.ARRIVE) {
				result += String.format("%-20s | %-10s | ",  m.getDescription(), ((ArrivedElevatorMessage)m).getArrivedFloor());
			} else if (m.getType() == MessageType.DOORS_OPENED) {
				result += String.format("%-20s | %-10s | ",  m.getDescription(), m.getDirection() + ", " + ((DoorOpenedMessage)m).getArrivedFloor());
			} else if (m.getType() == MessageType.ELEVATOR_STUCK) {
				result += String.format("%-20s | %-10s | ",  m.getDescription(), "");
			}else {
				result += String.format("%-20s | %-10s | ",  m.getDescription(), m.getDirection());
			}
			result += String.format("%-10s | %-5s |", type == "RECEIVED" ? "*" : " ", type == "RECEIVED" ? " " : "*");
			
			if (m.getType() == MessageType.ELEVATOR_STUCK) {
				result += String.format(" %-5s | %-12s |", ((ElevatorStuckMessage)m).getError(), ((ElevatorStuckMessage)m).getElevatorId());
			}
			
			System.out.println(result);
		}	
	}
		
	/**
	 * A display of the light status for floor.
	 */
	public static void printLightStatus(int[] upLights, int[] downLights) {
		String floorLightsDisplay = "\nFLOOR LIGHTS STATUS\n---------------------------------";
		for(int i = 0; i<upLights.length;i++) {			
			floorLightsDisplay += "\n| Floor " + (i + 1) + " | UP: " + (upLights[i] > 0? "on " : "off") + " | DOWN: " + (downLights[i] != 0 ? "on " : "off") + " |";
		}
		floorLightsDisplay += "\n---------------------------------\n";
		System.out.println(floorLightsDisplay);
	}
	
	/**
	 * A display of the light status for elevator.
	 */
	public static void printLightStatus(int elevatorNumber, boolean[] floorLights) {
		String elevatorLights = "\nELEVATOR " + (elevatorNumber + 1) + " LIGHTS STATUS\n----------------";
		for (int i = 0; i < floorLights.length; i++) {
			elevatorLights += "\n| Floor " + (i + 1) + ": " + (floorLights[i] ? "on " : "off") + " |";
		}
		elevatorLights += "\n----------------\n";
		System.out.println(elevatorLights);
	}

}
