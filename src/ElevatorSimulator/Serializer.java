package ElevatorSimulator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ElevatorSimulator.Messages.Message;

/**
 * The class in charge of serializing and deserializing the message contents.
 * 
 * @author Andre Hazim
 *
 */
public class Serializer {
	/**
	 * Serializing the message content to a byte array to transfer over UDP.
	 * 
	 * @param m, the message to serialize.
	 * 
	 * @return the byte array contents of the message.
	 */
	public static synchronized byte[] serializeMessage(Message m) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream out;
		byte[] result = new byte[100];
		try {
			out = new ObjectOutputStream(outputStream);
			out.writeObject(m);
			out.flush();
			
			result = outputStream.toByteArray();
		} catch (IOException e) {
			System.out.println("WARNING: INTERRUPTING SERIALIZATION!");
		}

		return result;
	}
	
	/**
	 * Deserializing the byte array back to a message.
	 * 
	 * @param content, the byte array to deserialize.
	 * 
	 * @return the deserialized message.
	 */
	public static synchronized Message deserializeMessage(byte[] content) {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
		ObjectInputStream in;
		Message result = null;
		try {
			in = new ObjectInputStream(inputStream);
			result = (Message)in.readObject();
		} catch (IOException e) {
			System.out.println("WARNING: INTERRUPTING DESERIALIZATION!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
}
