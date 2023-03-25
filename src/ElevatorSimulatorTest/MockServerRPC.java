package ElevatorSimulatorTest;

import ElevatorSimulator.Logger;
import ElevatorSimulator.Serializer;
import ElevatorSimulator.Messages.ACKMessage;
import ElevatorSimulator.Messages.GetUpdateMessage;
import ElevatorSimulator.Messages.Message;
import ElevatorSimulator.Messages.MessageType;
import ElevatorSimulator.Messages.ReadyMessage;
import ElevatorSimulator.Messages.SenderType;
import ElevatorSimulator.Messages.UpdateElevatorInfoMessage;
import ElevatorSimulator.Messaging.MessageQueue;
import ElevatorSimulator.Messaging.ServerRPC;

/**
 * The ServerRPC class in charge of receiving elevator/floor requests and forwarding them.
 * 
 * @author Bardia Parmoun
 *
 */
public class MockServerRPC extends ServerRPC {
	// The current message in the server.
	private Message currentMessage;
	
	
	/**
	 * The constructor for the class.
	 * 
	 * @param sendPort, the port used to send the packets to.
	 */
	public MockServerRPC(MessageQueue queue, int port) {
		super(queue, port);
		currentMessage = null;
	}
	
	/**
	 * Process the received back and prepare the proper reply.
	 * 
	 * @return the proper reply for the packet.
	 */
	@Override
	protected Message processPacket() {
		Message receiveMessage = Serializer.deserializeMessage(receivePacket.getData());
				
		if (receiveMessage == null) {
			return null;
		}
		
		if (receiveMessage.getType() != MessageType.GET_UPDATE) {
			updateCurrentMessage(receiveMessage);
		}
						

		Message replyMessage;
		if (receiveMessage.getType() == MessageType.GET_UPDATE) { // for request update packets check the receiving buffer.
			GetUpdateMessage updateMessage = (GetUpdateMessage) receiveMessage;
			if (updateMessage.getSender() == SenderType.FLOOR) {
				replyMessage = queue.receiveFromFloor();
			} else {
				replyMessage = queue.receiveFromElevator(updateMessage.getElevatorNumber());
			}
		} else if (receiveMessage.getType() == MessageType.READY) {
			ReadyMessage readyMessage = (ReadyMessage) receiveMessage;
			Logger.printMessage(readyMessage, "RECEIVED");
			queue.addElevator(readyMessage.getElevatorInfo().getElevatorId(),readyMessage.getElevatorInfo());
			replyMessage = new ACKMessage();
		} else if (receiveMessage.getType() == MessageType.UPDATE_ELEVATOR_INFO) {
			UpdateElevatorInfoMessage message = (UpdateElevatorInfoMessage) receiveMessage;
			queue.updateInfo(message.getInfo().getElevatorId(), message.getInfo());
			replyMessage = new ACKMessage();
		} else if (receiveMessage.getType() == MessageType.START) {
			queue.replyToFloor(receiveMessage);
			replyMessage = new ACKMessage();
		} else if (receiveMessage.getType() == MessageType.ELEVATOR_STUCK) {
			queue.replyToFloor(receiveMessage); // forward to floor
			queue.send(receiveMessage); // also forward to the scheduler.
			replyMessage = new ACKMessage();
		} else if (receiveMessage.getType() == MessageType.DOORS_OPENED) {
			queue.replyToFloor(receiveMessage);
			replyMessage = new ACKMessage();
		} else { // for data packets, send an acknowledgement message and update the send buffer.
			queue.send(receiveMessage);
			replyMessage = new ACKMessage();
		}
		
		return replyMessage;
	}
	
	/**
	 * Returns the current message in the server.
	 * @return
	 */
	public synchronized Message getCurrentMessage() {
		while(currentMessage == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return currentMessage;
	}
	
	private synchronized void updateCurrentMessage(Message newMessage) {
		this.currentMessage = newMessage;
		notifyAll();
	}
	
	public synchronized void clearCurrentMessage() {
		currentMessage = null;
	}
}
