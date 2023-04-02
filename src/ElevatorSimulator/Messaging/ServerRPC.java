package ElevatorSimulator.Messaging;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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

/**
 * The ServerRPC class in charge of receiving elevator/floor requests and forwarding them.
 * 
 * @author Bardia Parmoun
 *
 */
public class ServerRPC implements Runnable {
	// Used to hold the send and receive packets.
	private DatagramPacket sendPacket;
	protected DatagramPacket receivePacket;
	
	// The socket used for sending and receiving packet.
	protected DatagramSocket sendReceiveSocket;
	
	// The port used to send the packets to.
	private int sourcePort;
	
	// Keeps track of the message queue for the shared data.
	protected MessageQueue queue;
	
	// Ensures the server RPC can keep running.
	private boolean shouldRun;
	
	// The timeout used for closing the socket.
	public static final int TIMEOUT = 30000;
	
	// The public ip for the server.
	public static final String PUBLIC_IP = "xx.xx.xx.xx";

	/**
	 * The constructor for the class.
	 * 
	 * @param sendPort, the port used to send the packets to.
	 */
	public ServerRPC(MessageQueue queue, int port) {
		try {
			// Construct a datagram socket and bind it to any available port on the local host machine.
			// This socket will be used to send and receive UDP Datagram packets.
			sendReceiveSocket = new DatagramSocket(port);
			
			// Set a timeout for the socket.
			sendReceiveSocket.setSoTimeout(TIMEOUT);		
			this.queue = queue;
			this.shouldRun = true;						
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Receives a packet and from a source (either client or host)
	 */
	protected void receive() {
		// Construct a DatagramPacket for receiving packets up
		// to 100 bytes long (the length of the byte array).
		byte data[] = new byte[100000];
		receivePacket = new DatagramPacket(data, data.length);
		//System.out.println(Thread.currentThread().getName() + ": Waiting for Packet.\n");

		// Try to receive a packet from the source.
		try {
			sendReceiveSocket.receive(receivePacket);
		} catch (IOException e) {
			close();
			shouldRun = false;
			return;
		}
		
		sourcePort = receivePacket.getPort();
	}
	
	/**
	 * Process the received back and prepare the proper reply.
	 * 
	 * @return the proper reply for the packet.
	 */
	protected synchronized Message processPacket() {
		Message receiveMessage = Serializer.deserializeMessage(receivePacket.getData());
				
		if (receiveMessage == null) {
			return null;
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
	 * Sends a packet back to the source.
	 * 
	 * @param content, the contents of the packet to send back.
	 */
	protected void send(Message replyMessage) {
		byte[] content = Serializer.serializeMessage(replyMessage);
		
		// Create a new datagram packet to reply to the source.
		sendPacket = new DatagramPacket(content, content.length, receivePacket.getAddress(), sourcePort);

		// Try to send the packet to the source.
		try {
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			close();
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Closes the appropriate sockets.
	 */
	protected void close() {
		sendReceiveSocket.close();
	}

	/**
	 * Kills the server RPC.
	 */
	public void kill() {
		// Decrease the timeout.
		try {
			sendReceiveSocket.setSoTimeout(1000);
		} catch (SocketException e) {
			e.printStackTrace();
		}	
		shouldRun = false;
	}
	
	/**
	 * The run function for the server thread.
	 */
	@Override
	public void run() {
		while(shouldRun) {
			receive();
			
			Message replyMessage = processPacket();
			
			if (replyMessage != null) {
				send(replyMessage);
			}
		}
		
		close();		
	}

}
