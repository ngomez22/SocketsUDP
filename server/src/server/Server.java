package server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

import message.Message;


public class Server {
	
	public static HashMap<String, Helper> helpers;
	
	public static void main(String args[]) throws Exception {
		helpers = new HashMap<>();
		int serverPort = 4321;
		if(args.length > 0) {
			serverPort = Integer.parseInt(args[0]);
		}
		DatagramSocket serverSocket = new DatagramSocket(serverPort);
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			Message msg = getObject(receivePacket.getData());
			String ip = receivePacket.getAddress().toString();
			int port = receivePacket.getPort();
			long timeDiff = System.currentTimeMillis() - msg.getTimestamp();
			Helper ipHelper = helpers.get(ip);
			if(ipHelper == null) {
				ipHelper = new Helper(filename(ip, port), 0);
				helpers.put(ip, ipHelper);
			} 
			ipHelper.processMsg(msg, timeDiff);
		}
	}
	
	public static String filename(String ip, int port) {
		return ip.replace(".", "-") + "--" + port;
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
