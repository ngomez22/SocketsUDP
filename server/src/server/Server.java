package server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import message.Message;


public class Server {
	public static void main(String args[]) throws Exception {
		int serverPort = 4321;
		if(args.length > 0) {
			serverPort = Integer.parseInt(args[0]);
		}
		DatagramSocket serverSocket = new DatagramSocket(serverPort);
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData,
					receiveData.length);
			serverSocket.receive(receivePacket);
			Message recibido = getObject(receivePacket.getData());
			System.out.println(recibido.getSeqNun());
			System.out.println(recibido.getTimestamp());
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
		}
	}
	
	public static Message getObject(byte[] yourBytes) {
		Message o = null;
		try
		{
			ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
			ObjectInput in = null;
			in = new ObjectInputStream(bis);
			o = (Message) in.readObject();

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
	
}
