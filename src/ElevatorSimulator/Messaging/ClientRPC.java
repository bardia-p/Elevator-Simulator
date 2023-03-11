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

import ElevatorSimulator.Messages.Message;

public class ClientRPC {
	// Used to hold the send and receive packets.
	private DatagramPacket sendPacket, receivePacket;
	
	// The socket used for sending and receiving packet.
	private DatagramSocket sendReceiveSocket;
	
	// The port used to send the packets to.
	private int sendPort;
	
	// The timeout used for closing the socket.
	public static final int TIMEOUT = 30000;
	
	public static final String RESPONSE_REQUEST = "REQUEST UPDATE";

	
	/**
	 * The constructor for the class.
	 * 
	 * @param sendPort, the port used to send the packets to.
	 */
	public ClientRPC(int sendPort) {
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
	private byte[] sendAndReceive(String message) {
		byte[] request = message.getBytes();
		
		// Tries to initialize the packet to send to the host.
		try {
			sendPacket = new DatagramPacket(request, request.length, InetAddress.getLocalHost(), sendPort);
		} catch (UnknownHostException e) {
			close();
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Sending packet:");
		
		//logger.displayPacket(sendPacket);

		// Send the datagram packet to the host via the send/receive socket.
		try {
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			close();
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Packet sent.\n");

		// Construct a DatagramPacket for receiving packets up to 100 bytes.
		byte data[] = new byte[100];
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
		
		System.out.println("Received Packet.\n");

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
		return deserializeMessage(sendAndReceive(RESPONSE_REQUEST));
	}
	
	
	public Message getElevatorUpdate(int elevatorNumber) {
		return deserializeMessage(sendAndReceive(RESPONSE_REQUEST + " " + elevatorNumber));
	}
	
	public void sendRequest(Message m) {
		String serializedRequest = serializeMessage(m);
		sendAndReceive(serializedRequest);
	}
	
	private String serializeMessage(Message m) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(outputStream);
			out.writeObject(m);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toString();
	}
	
	private Message deserializeMessage(byte[] content) {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(inputStream);
			return (Message)in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Closes the appropriate sockets.
	 */
	public void close() {
		sendReceiveSocket.close();
	}
}