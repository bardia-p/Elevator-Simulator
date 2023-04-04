package ElevatorSimulator.UI;

import ElevatorSimulator.Messaging.ConnectionType;

/**
 * starts UI subsystem and connects GUI to it
 * @author Guy Morgenshtern
 *
 */
public class UIGenerator {
	
	public static void main(String[] args) {
		ElevatorUI ui = new ElevatorUI();
		Thread  uiClient = new Thread(new UIClient(ui, ConnectionType.LOCAL));
		
		uiClient.start();
	}
}
