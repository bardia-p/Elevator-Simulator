package ElevatorSimulator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ElevatorSimulator.Messages.Message;

public class Serializer {
	
	public static byte[] serializeMessage(Message m) {
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
	
	public static Message deserializeMessage(byte[] content) {
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
}
