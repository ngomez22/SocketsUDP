package server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {
	public static void main(String args[]) throws Exception {
		DatagramSocket serverSocket = new DatagramSocket(9876);
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData,
					receiveData.length);
			serverSocket.receive(receivePacket);
			String sentence = new String(receivePacket.getData());
			System.out.println("RECEIVED: " + sentence);
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			String capitalizedSentence = sentence.toUpperCase();
			sendData = capitalizedSentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData,
					sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
		}
	}
	
	public Object getObject(byte[] yourBytes) {
		Object o = null;
		try
		{
			ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
			ObjectInput in = null;
			in = new ObjectInputStream(bis);
			o = in.readObject();

			if (in != null) {
				in.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return o;
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
