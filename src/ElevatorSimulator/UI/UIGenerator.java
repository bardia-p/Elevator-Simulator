package ElevatorSimulator.UI;

/**
 * starts UI subsystem and connects GUI to it
 * @author guymorgenshtern
 *
 */
public class UIGenerator {
	
	public static void main(String[] args) {
		ElevatorUI ui = new ElevatorUI();
		Thread  uiClient = new Thread(new UIClient(ui));
		
		uiClient.start();
	}
}
