package ElevatorSimulatorTest;

import java.net.SocketException;

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
 * A mock version of the ServerRPC class with a work queue in the background which holds the latest request.
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
		try {
			this.sendReceiveSocket.setSoTimeout(10000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Overriding the processPacket() function of the ServerRPC to add custom functionality.
	 * 
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
		
		// We don't need the GET_UPDATE messages.
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
	 * Returns the current message in ServerRPC.
	 * 
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
		
		Message toReturn = currentMessage;
		currentMessage = null;
		return toReturn;
	}
	
	/**
	 * Updates the latest message in the ServerRPC.
	 * 
	 * @param newMessage
	 */
	private synchronized void updateCurrentMessage(Message newMessage) {
		this.currentMessage = newMessage;
		notifyAll();
	}
}
