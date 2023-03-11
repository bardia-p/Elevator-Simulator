package ElevatorSimulator.Messaging;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import ElevatorSimulator.Messages.ACKMessage;
import ElevatorSimulator.Messages.GetUpdateMessage;
import ElevatorSimulator.Messages.Message;
import ElevatorSimulator.Messages.MessageType;
import ElevatorSimulator.Messages.ReadyMessage;
import ElevatorSimulator.Messages.SenderType;
import ElevatorSimulator.Messages.UpdateElevatorInfoMessage;
import ElevatorSimulator.Scheduler.Scheduler;

public class SchedulerHelper implements Runnable {
	// Used to hold the send and receive packets.
	private DatagramPacket sendPacket, receivePacket;
	
	// The socket used for sending and receiving packet.
	private DatagramSocket sendReceiveSocket;
	
	// The port used to send the packets to.
	private int sourcePort;
	
	// The timeout used for closing the socket.
	public static final int TIMEOUT = 30000;
	
	private MessageQueue queue;
	
	private Scheduler scheduler;
	
	/**
	 * The constructor for the class.
	 * 
	 * @param sendPort, the port used to send the packets to.
	 */
	public SchedulerHelper(Scheduler sch, MessageQueue queue, int port) {
		try {
			// Construct a datagram socket and bind it to any available port on the local host machine.
			// This socket will be used to send and receive UDP Datagram packets.
			sendReceiveSocket = new DatagramSocket(port);
			
			// Set a timeout for the socket.
			sendReceiveSocket.setSoTimeout(TIMEOUT);
			
			this.queue = queue;
			this.scheduler = sch;
			//logger = new Logger();
						
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Receives a packet and from a source (either client or host)
	 */
	private void receive() {
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
			e.printStackTrace();
			System.exit(1);
		}

		// Process the received datagram.
		//System.out.println(Thread.currentThread().getName() + ": Packet received:\n");
		
		sourcePort = receivePacket.getPort();
	}
	
	/**
	 * Process the received back and prepare the proper reply.
	 * 
	 * @return the proper reply for the packet.
	 */
	private Message processPacket() {
		Message receiveMessage = deserializeMessage(receivePacket.getData());
		
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
			queue.addElevator(readyMessage.getElevatorInfo().getElevatorId());
			queue.send(receiveMessage);
			replyMessage = new ACKMessage();
		} else if (receiveMessage.getType() == MessageType.UPDATE_ELEVATOR_INFO) {
			UpdateElevatorInfoMessage message = (UpdateElevatorInfoMessage) receiveMessage;
			scheduler.updateInfo(message.getInfo().getElevatorId(), message.getInfo());
			System.out.println("GOT UPDATE FROM ELEVATOR " + message.getInfo().getElevatorId());
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
	private void send(Message replyMessage) {
		byte[] content = serializeMessage(replyMessage);
		
		// Create a new datagram packet to reply to the soruce.
		sendPacket = new DatagramPacket(content, content.length, receivePacket.getAddress(), sourcePort);

		//System.out.println(Thread.currentThread().getName() + ": Sending packet:\n");

		// Try to send the packet to the source.
		try {
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			close();
			e.printStackTrace();
			System.exit(1);
		}

		//System.out.println(Thread.currentThread().getName() + ": packet sent.\n");
	}
	
	
	private byte[] serializeMessage(Message m) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream out;
		byte[] result = new byte[100];
		try {
			out = new ObjectOutputStream(outputStream);
			out.writeObject(m);
			out.flush();

			result = outputStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	private Message deserializeMessage(byte[] content) {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
		ObjectInputStream in;
		Message result = null;
		try {
			in = new ObjectInputStream(inputStream);
			result = (Message)in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Closes the appropriate sockets.
	 */
	public void close() {
		sendReceiveSocket.close();
	}

	@Override
	public void run() {
		while(true) {
			receive();
			Message replyMessage = processPacket();
			send(replyMessage);
		}
	}

}
