package ElevatorSimulator.Messaging;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import ElevatorSimulator.Serializer;
import ElevatorSimulator.Messages.GetUpdateMessage;
import ElevatorSimulator.Messages.Message;
import ElevatorSimulator.Messages.SenderType;

/**
 * The class in charge of the UDP responsibilities.
 * 
 * @author Bardia Parmoun
 * @author Andre Hazim
 * @author Guy Morgenshtern
 *
 */
public class ClientRPC {
	// Used to hold the send and receive packets.
	private DatagramPacket sendPacket;
	private DatagramPacket receivePacket;
	
	// The socket used for sending and receiving packet.
	private DatagramSocket sendReceiveSocket;
	
	// The port used to send the packets to.
	private int sendPort;
	
	private ConnectionType connectionType;
		
	// The timeout used for closing the socket.
	public static final int TIMEOUT = 30000;
	
	
	/**
	 * The constructor for the class.
	 * 
	 * @param sendPort, the port used to send the packets to.
	 */
	public ClientRPC(int sendPort) {
		this(sendPort, ConnectionType.LOCAL);
	}
	
	/**
	 * The constructor for the ClientRPC class with a connection type.
	 * 
	 * @param sendPort, the port to send to the server.
	 * @param connectionType, the connection type to create local or remote connections.
	 */
	public ClientRPC(int sendPort, ConnectionType connectionType) {
		try {
			// Construct a datagram socket and bind it to any available port on the local host machine.
			// This socket will be used to send and receive UDP Datagram packets.
			sendReceiveSocket = new DatagramSocket();
			
			// Set a timeout for the socket.
			sendReceiveSocket.setSoTimeout(TIMEOUT);
			
			//logger = new Logger();
			
			this.sendPort = sendPort;
			
			this.connectionType = connectionType;
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
			InetAddress receiveingAddress;
			if (connectionType == ConnectionType.LOCAL) {
				receiveingAddress = InetAddress.getLocalHost();
			} else {
				receiveingAddress = InetAddress.getByName(ServerRPC.PUBLIC_IP);
			}
			sendPacket = new DatagramPacket(request, request.length, receiveingAddress, sendPort);
		} catch (UnknownHostException e) {
			close();
			e.printStackTrace();
			System.exit(1);
		}


		// Send the datagram packet to the host via the send/receive socket.
		try {
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			close();
			e.printStackTrace();
			System.exit(1);
		}

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
		
		return receivePacket.getData();
	}
	
	/**
	 * A specific form of the sendAndReceive request.
	 * Sends a "REQUEST UPDATE" request and obtains the reply.
	 * 
	 * Used by the floor.
	 * 
	 * @return the update received from the host.
	 */
	public Message getFloorUpdate() {
		return Serializer.deserializeMessage(sendAndReceive(Serializer.serializeMessage(new GetUpdateMessage(null, SenderType.FLOOR))));
	}
	
	/**
	 * A specific form of the sendAndReceive request.
	 * Sends a "REQUEST UPDATE" request and obtains the reply.
	 * 
	 * Used by the elevator.
	 * 
	 * @return the update received from the host.
	 */
	public Message getElevatorUpdate(int elevatorNumber) {
		return Serializer.deserializeMessage(sendAndReceive(Serializer.serializeMessage(new GetUpdateMessage(null, SenderType.ELEVATOR, elevatorNumber))));
	}
	
	/**
	 * Send a message request.
	 * 
	 * @param m, the message to send over UDP.
	 */
	public void sendRequest(Message m) {
		sendAndReceive(Serializer.serializeMessage(m));
	}
	
	/**
	 * Closes the appropriate sockets.
	 */
	public void close() {
		sendReceiveSocket.close();
	}

}