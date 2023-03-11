package ElevatorSimulator;

import ElevatorSimulator.Messages.ArrivedElevatorMessage;
import ElevatorSimulator.Messages.DoorOpenedMessage;
import ElevatorSimulator.Messages.Message;
import ElevatorSimulator.Messages.MessageType;

public class Logger {
	
	public static void printMessage(Message m, String type) {
		String result = "";
		String addResult = "";
		String messageToPrint = "";
				
		if (m != null && m.getType() != MessageType.EMPTY) {
			
			result += "\n---------------------" + Thread.currentThread().getName() +"-----------------------\n";
			result += String.format("| %-15s | %-10s | %-10s | %-3s |\n", "REQUEST", "ACTION", "RECEIVED", "SENT");
			result += new String(new char[52]).replace("\0", "-");
			
			if (m.getType() == MessageType.ARRIVE) {
				addResult += String.format("\n| %-15s | %-10s | ",  m.getDescription(), ((ArrivedElevatorMessage)m).getArrivedFloor());
			} else if (m.getType() == MessageType.DOORS_OPENED) {
				addResult += String.format("\n| %-15s | %-10s | ",  m.getDescription(), m.getDirection() + ", " + ((DoorOpenedMessage)m).getArrivedFloor());
			} else {
				addResult += String.format("\n| %-15s | %-10s | ",  m.getDescription(), m.getDirection());
			}
			addResult += String.format(" %-10s | %-3s |", type == "RECEIVED" ? "*" : " ", type == "RECEIVED" ? " " : "*");
			
			System.out.println(messageToPrint + result + addResult);
		}	
	}

}
