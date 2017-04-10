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

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import message.Message;


public class Server {
	
	public static HashMap<String, Helper> helpers;
	private static int corruptedPackets = 0;
		
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
			
			if(msg.getSeqNum() == msg.getTotal())
			{
				if(validateHash(msg))
				{
					System.out.println("Archivo recibido correctamente.");
				}
				else
				{
					System.out.println("Archivo recibido esta corrupto.");
				}
			}
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
	
	private static boolean validateHash(Message msg) {
		byte[] receivedChunk = msg.getChunkOfFile();
		MessageDigest md5;
		String hex ="";
		try {
			md5 = MessageDigest.getInstance("MD5");
			hex = (new HexBinaryAdapter()).marshal(md5.digest(receivedChunk));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		if(hex.equals(msg.getHashCode()))
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}

	private static boolean digest(byte[] msg, int objLength) {
		boolean res = false;
		byte[] content = new byte[81];
		byte[] hashBytes = new byte[16];
		for(int i = 0; i < 97; i++)
		{
			
			if(i > 80)
			{
				hashBytes[i-81] = msg[i];
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
