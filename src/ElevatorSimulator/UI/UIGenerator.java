package ElevatorSimulator.UI;

public class UIGenerator {
	
	public static void main(String[] args) {
		ElevatorUI ui = new ElevatorUI();
		Thread  uiClient = new Thread(new UIClient(ui));
		
		uiClient.start();
	}
}
