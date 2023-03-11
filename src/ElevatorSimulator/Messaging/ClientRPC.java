package ElevatorSimulator.Messaging;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import ElevatorSimulator.Serializer;
import ElevatorSimulator.Messages.GetUpdateMessage;
import ElevatorSimulator.Messages.Message;
import ElevatorSimulator.Messages.MessageType;
import ElevatorSimulator.Messages.SenderType;

public class ClientRPC {
	// Used to hold the send and receive packets.
	private DatagramPacket sendPacket, receivePacket;
	
	// The socket used for sending and receiving packet.
	private DatagramSocket sendReceiveSocket;
	
	// The port used to send the packets to.
	private int sendPort;
		
	// The timeout used for closing the socket.
	public static final int TIMEOUT = 30000;
	
	
	/**
	 * The constructor for the class.
	 * 
	 * @param sendPort, the port used to send the packets to.
	 */
	public ClientRPC( int sendPort) {
		try {
			// Construct a datagram socket and bind it to any available port on the local host machine.
			// This socket will be used to send and receive UDP Datagram packets.
			sendReceiveSocket = new DatagramSocket();
			
			// Set a timeout for the socket.
			sendReceiveSocket.setSoTimeout(TIMEOUT);
			
			//logger = new Logger();
			
			this.sendPort = sendPort;
			
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Sends a UDP packet and obtains the reply.
	 * 
	 * @param request, the request body to include in the packet.
	 */
	private byte[] sendAndReceive(byte[] request) {		
		// Tries to initialize the packet to send to the host.
		try {
			sendPacket = new DatagramPacket(request, request.length, InetAddress.getLocalHost(), sendPort);
		} catch (UnknownHostException e) {
			close();
			e.printStackTrace();
			System.exit(1);
		}

		//System.out.println("Sending packet:");
		
		//logger.displayPacket(sendPacket);

		// Send the datagram packet to the host via the send/receive socket.
		try {
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			close();
			e.printStackTrace();
			System.exit(1);
		}

		//System.out.println("Packet sent.\n");

		// Construct a DatagramPacket for receiving packets up to 100 bytes.
		byte data[] = new byte[100000];
		receivePacket = new DatagramPacket(data, data.length);

		// Try to receive the response packet.
		try {
			// Block until a datagram is received via sendReceiveSocket.
			sendReceiveSocket.receive(receivePacket);
		} catch (IOException e) {
			close();
			e.printStackTrace();
			System.exit(1);
		}
		
		//System.out.println("Received Packet.\n");

		// Process the received datagram packet.
		//logger.displayPacket(receivePacket);
		
		return receivePacket.getData();
	}
	
	/**
	 * A specific form of the sendAndReceive request.
	 * Sends a "REQUEST UPDATE" request and obtains the reply.
	 * 
	 * @return the update received from the host.
	 */
	public Message getFloorUpdate() {
		return Serializer.deserializeMessage(sendAndReceive(Serializer.serializeMessage(new GetUpdateMessage(null, SenderType.FLOOR))));
	}
	
	
	public Message getElevatorUpdate(int elevatorNumber) {
		return Serializer.deserializeMessage(sendAndReceive(Serializer.serializeMessage(new GetUpdateMessage(null, SenderType.ELEVATOR, elevatorNumber))));
	}
	
	public void sendRequest(Message m) {
		printMessage(m, "SEND");
		sendAndReceive(Serializer.serializeMessage(m));
	}
	
	/**
	 * Closes the appropriate sockets.
	 */
	public void close() {
		sendReceiveSocket.close();
	}
	
	private void printMessage(Message m, String type) {
		
		String result = "";
		String addResult = "";
		String messageToPrint = "";
				
		if (m != null) {
			
			result += "\n---------------------" + Thread.currentThread().getName() +"-----------------------\n";
			result += String.format("| %-15s | %-10s | %-10s | %-3s |\n", "REQUEST", "ACTION", "RECEIVED", "SENT");
			result += new String(new char[52]).replace("\0", "-");
			
			addResult += String.format("\n| %-15s | %-10s | ", (m.getType() == MessageType.KILL ? "KILL" : m.getDescription()), m.getDirection());
			addResult += String.format(" %-10s | %-3s |", type == "RECEIVED" ? "*" : " ", type == "RECEIVED" ? " " : "*");
			
			System.out.println(messageToPrint + result + addResult);
		}
		
	}
}