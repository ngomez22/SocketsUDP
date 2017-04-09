package server;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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
		System.out.println("Started server in port " + serverPort);
		
 		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			int objLength = receivePacket.getLength();
			Message msg = getObject(receivePacket.getData());
			String ip = receivePacket.getAddress().toString();
			int port = receivePacket.getPort();
			long timeDiff = System.currentTimeMillis() - msg.getTimestamp();
			Helper ipHelper = helpers.get(ip);
			if(ipHelper == null) {
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
		String filename = ip.replace(".", "-").replace("\\", "").replace("/", "") + ".txt"; 
		System.out.println("Assigned " + filename + " to a new helper");
		return "data/" + filename;
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
