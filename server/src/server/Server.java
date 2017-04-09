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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
			int objLength = receivePacket.getLength();
			System.out.println(receivePacket.getData().length);
			System.out.println("Llego bien: " +digest(receivePacket.getData(), objLength));	
			Message msg = getObject(receivePacket.getData());
			String ip = receivePacket.getAddress().toString();
			int port = receivePacket.getPort();
			long timeDiff = System.currentTimeMillis() - msg.getTimestamp();
			Helper ipHelper = helpers.get(ip);
			if(ipHelper == null || ipHelper.getTotal() == -1) {
				ipHelper = new Helper(filename(ip, port), msg.getTotal());
				helpers.put(ip, ipHelper);
			} 
			ipHelper.processMsg(msg, timeDiff);
		}
	}
	
	private static boolean digest(byte[] msg, int objLength) {
		boolean res = false;
		byte[] content = new byte[objLength];
		byte[] hashBytes = new byte[msg.length - objLength];
		System.out.println(msg.length);
		for(int i = 0; i < msg.length; i++)
		{
			
			if(i >= objLength-1)
			{
				hashBytes[i] = msg[i];
			}
			else
			{
				content[i] = msg [i];
			}
		}
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] calculatedHash = md.digest(content);
			res = Arrays.equals(calculatedHash, hashBytes);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return res;
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
