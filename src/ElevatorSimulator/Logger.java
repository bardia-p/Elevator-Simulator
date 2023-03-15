package ElevatorSimulator;

import ElevatorSimulator.Messages.ArrivedElevatorMessage;
import ElevatorSimulator.Messages.DoorOpenedMessage;
import ElevatorSimulator.Messages.Message;
import ElevatorSimulator.Messages.MessageType;

/**
 * The logger class in charge of printing the messages.
 * 
 * @author Guy Morgenshtern
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
		String addResult = "";
		String messageToPrint = "";
				
		if (m != null && m.getType() != MessageType.EMPTY) {
			
			result += "\n---------------------" + Thread.currentThread().getName() +"-----------------------\n";
			result += String.format("| %-30s | %-15s | %-10s | %-10s | %-3s |\n", "TIME", "REQUEST", "ACTION", "RECEIVED", "SENT");
			result += new String(new char[52]).replace("\0", "-");
			
			addResult += String.format("\n| %-30s | ",  m.getTimestamp());
			
			if (m.getType() == MessageType.ARRIVE) {
				addResult += String.format("%-15s | %-10s | ",  m.getDescription(), ((ArrivedElevatorMessage)m).getArrivedFloor());
			} else if (m.getType() == MessageType.DOORS_OPENED) {
				addResult += String.format("%-15s | %-10s | ",  m.getDescription(), m.getDirection() + ", " + ((DoorOpenedMessage)m).getArrivedFloor());
			} else {
				addResult += String.format("%-15s | %-10s | ",  m.getDescription(), m.getDirection());
			}
			addResult += String.format(" %-10s | %-3s |", type == "RECEIVED" ? "*" : " ", type == "RECEIVED" ? " " : "*");
			
			System.out.println(messageToPrint + result + addResult);
		}	
	}

}
