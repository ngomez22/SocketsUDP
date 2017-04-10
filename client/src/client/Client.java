package client;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import message.Message;

public class Client {

	public static final String FILE = "files/small.png";
	public static final int BUFFER_SIZE = 800;

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
		ArrayList<byte[]> chunks = getFile();
		for (int i = 0; i < chunks.size(); i++) {
			byte[] actual = chunks.get(i);
			String hash = digestFileToString(actual);
			Message m = new Message(i + 1, chunks.size(), hash, actual);
			byte[] object = messageToBytes(m);
			System.out.println(object.length);
			DatagramPacket sendData = new DatagramPacket(object, object.length, server, port);
			clientSocket.send(sendData);
			System.out.println("Sent object #" + i + " at " + m.getTimestamp());
		}
		clientSocket.close();
	}

	public ArrayList<byte[]> getFile() {
		ArrayList<byte[]> data = new ArrayList<>();
		try {
			
			
			File file = new File(FILE);
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);

			long fileLength = file.length();
			byte[] contents = new byte[BUFFER_SIZE];
			long current = 0;
			int count;
			while ((count = fis.read(contents)) > -1) {
				data.add(Arrays.copyOf(contents, contents.length));
				current += count;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	private byte[] digest(byte[] object) {
		byte[] res = null;
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(object);
			byte[] combined = new byte[object.length + messageDigest.length];
			for (int i = 0; i < combined.length; ++i) {
				combined[i] = i < object.length ? object[i] : messageDigest[i - object.length];
			}
			res = combined;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return res;

	}
	
	private String digestFileToString(byte[] object) {
		MessageDigest md5;
		String hex ="";
		try {
			md5 = MessageDigest.getInstance("MD5");
			hex = (new HexBinaryAdapter()).marshal(md5.digest(object));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return hex;

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
			} catch (IOException ex) {
			}
		}
		return message;
	}
}
