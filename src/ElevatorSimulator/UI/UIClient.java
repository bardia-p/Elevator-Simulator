package ElevatorSimulator.UI;

import ElevatorSimulator.Messages.DoorInterruptMessage;
import ElevatorSimulator.Messages.DoorOpenedMessage;
import ElevatorSimulator.Messages.ElevatorStuckMessage;
import ElevatorSimulator.Messages.Message;
import ElevatorSimulator.Messages.MessageType;
import ElevatorSimulator.Messages.RequestElevatorMessage;
import ElevatorSimulator.Messages.StopType;
import ElevatorSimulator.Messages.UpdateElevatorInfoMessage;
import ElevatorSimulator.Messaging.ClientRPC;
import ElevatorSimulator.Messaging.ConnectionType;
import ElevatorSimulator.Scheduler.Scheduler;

/**
 * responsible for getting UI update messages and processing them
 * @author guymorgenshtern andrehazim
 */

public class UIClient extends ClientRPC implements Runnable{

	private ElevatorUI ui;
	
	public UIClient(ElevatorUI ui, ConnectionType connectionType) {
		super(Scheduler.UI_PORT, connectionType);
		this.ui = ui;
	}
	
	/**
	 * processes the update message received by calling specific functions within UI view
	 * @param m - Message 
	 */
	private void processUpdate(Message m) {
		
		//call methods within the actual UI class to update the view (Observer pattern)
		
		if (m.getType() == MessageType.REQUEST) {
			RequestElevatorMessage requestMessage = (RequestElevatorMessage) m;
			ui.floorRequested(requestMessage.getFloor(), requestMessage.getDestination(), requestMessage.getDirection(), requestMessage.getTimestamp());
		} else if (m.getType() == MessageType.ELEVATOR_STUCK) {
			ElevatorStuckMessage stuckMessage = (ElevatorStuckMessage) m;
			ui.elevatorStuck(stuckMessage.getElevatorId(), stuckMessage.getTimestamp());
		} else if (m.getType() == MessageType.DOORS_OPENED) {
			DoorOpenedMessage openMessage = (DoorOpenedMessage) m;
			ui.doorOpened(openMessage.getID(), openMessage.getArrivedFloor(), openMessage.getNumPickups(), openMessage.getNumDropoffs(), openMessage.getStopType(), openMessage.getTimestamp());
			if (openMessage.getStopType() == StopType.PICKUP || openMessage.getStopType() == StopType.PICKUP_AND_DROPOFF) {
				ui.pickupPerformed(openMessage.getArrivedFloor(), openMessage.getDirection());
			}
		} else if (m.getType() == MessageType.UPDATE_ELEVATOR_INFO) {
			UpdateElevatorInfoMessage updateElevatorMessage = (UpdateElevatorInfoMessage) m;
			ui.updateElevatorInfo(updateElevatorMessage.getInfo(), updateElevatorMessage.getDirection(), updateElevatorMessage.getTimestamp());
		} else if (m.getType() == MessageType.DOOR_INTERRUPT) {
			DoorInterruptMessage doorInterruptMessage = (DoorInterruptMessage) m;
			ui.doorInterrupted(doorInterruptMessage.getElevatorID(), doorInterruptMessage.getTimestamp(), doorInterruptMessage.getDirection());
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
