package client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
	
	private String ip;
	private int port;
	private int numMessages;
	
	public Client(String ip, int port, int numMessages) {
		this.ip = ip;
		this.port = port;
		this.numMessages = numMessages;
	}
	
	public void send() throws Exception {
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress server = InetAddress.getByName(ip);
		for(int i=0; i<numMessages; i++) {
			Message m = new Message(i+1);
			byte[] object = messageToBytes(m);
			DatagramPacket sendData = new DatagramPacket(object, object.length, server, port);
			clientSocket.send(sendData);
			System.out.println("Sent object #" + i + " at " + m.getTimestamp());
		}
		clientSocket.close();
	}

	public byte[] messageToBytes(Message m) {
		byte[] message = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(m);
			out.flush();
			message = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException ex) {}
		}
		return message;
	}

	public class Message implements Serializable {
		private int seqNun;
		private long timestamp;

		public Message(int seqNum) {
			this.seqNun = seqNum;
			this.timestamp = System.currentTimeMillis();
		}

		public int getSeqNun() {
			return seqNun;
		}

		public long getTimestamp() {
			return timestamp;
		}
	}
}
