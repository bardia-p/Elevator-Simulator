package ElevatorSimulator.UI;

import java.util.ArrayList;

import ElevatorSimulator.Messages.DoorOpenedMessage;
import ElevatorSimulator.Messages.ElevatorStuckMessage;
import ElevatorSimulator.Messages.Message;
import ElevatorSimulator.Messages.MessageType;
import ElevatorSimulator.Messages.RequestElevatorMessage;
import ElevatorSimulator.Messages.UpdateElevatorInfoMessage;
import ElevatorSimulator.Messaging.ClientRPC;

/**
 * responsible for getting UI update messages and processing them
 * @author guymorgenshtern andrehazim
 */

public class UIClient extends ClientRPC implements Runnable{

	private static final int port = 4445;
	private ElevatorUI ui;
	
	public UIClient(ElevatorUI ui) {
		super(port);
		this.ui = ui;
	}
	
	private void processUpdate(Message m) {
		
		//call methods within the actual UI class to update the view (Observer pattern)
		
		if (m.getType() == MessageType.REQUEST) {
			RequestElevatorMessage requestMessage = (RequestElevatorMessage) m;
			//ui.floorRequested(requestMessage.getFloor(), requestMessage.getDestination(), requestMessage.getDirection(), requestMessage.getTimestamp());
		} else if (m.getType() == MessageType.ELEVATOR_STUCK) {
			ElevatorStuckMessage stuckMessage = (ElevatorStuckMessage) m;
			//ui.elevatorStuck(stuckMessage.getElevatorId(), stuckMessage.getTimestamp());
		} else if (m.getType() == MessageType.DOORS_OPENED) {
			DoorOpenedMessage openMessage = (DoorOpenedMessage) m;
			//ui.doorOpened(openMessage.getArrivedFloor(), openMessage.getNumPickups(), openMessage.getNumDropoffs(), openMessage.getStopType(), openMessage.getTimestamp());
		} else if (m.getType() == MessageType.UPDATE_ELEVATOR_INFO) {
			UpdateElevatorInfoMessage updateElevatorMessage = (UpdateElevatorInfoMessage) m;
			//ui.updateElevatorInfo(updateElevatorMessage.getInfo(), updateElevatorMessage.getDirection(), updateElevatorMessage.getTimestamp());
		}
	}
	

	@Override
	public void run() {
		while(true) {
			Message update = getUIUpdate();
			
			if (update != null) {
				processUpdate(update);
			}
		}
		
	}
	
}
