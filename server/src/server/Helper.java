package server;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import message.Message;

public class Helper {
	
	private String filename;
	private BufferedWriter bw;
	private int count;
	private int total;
	private long diffSum;
	private ArrayList<byte[]> chunks;
	private Byte[] file;
	
	public Helper(String filename, int total) throws FileNotFoundException {
		this.filename = filename;
		this.bw = new BufferedWriter(new PrintWriter(filename));
		this.count = 0;
		this.total = total;
		this.diffSum = 0;
		this.chunks = new ArrayList();
	}
	
	public void processMsg(Message m, long timeDiff) throws IOException {
		if(m.getTotal() != total) {
			done();
			total = m.getTotal();
		}
		System.out.println(m.getSeqNum() + ": " + timeDiff + "\n");
		chunks.add(m.getChunkOfFile());
		count++;
		diffSum += timeDiff;
		if(m.getSeqNum() == total) {
			done();
		}
		bw.flush();
	}

	public String getFilename() {
		return filename;
	}
	
	public int getTotal() {
		return total;
	}
	
	public int getLostCount() {
		return total - count;
	}
	
	public void join() {
		ArrayList<Byte> fileList = new ArrayList<>();
		for(int i=0; i<chunks.size(); i++) {
			for(int j=0; j<chunks.get(i).length; j++) {
				fileList.add(chunks.get(i)[j]);
			}
		}
		file = fileList.toArray(new Byte[fileList.size()]);
	}
	
	public void done() throws IOException {
		System.out.println("----DONE----\n");
		System.out.println("Received " + count + "/" + total + "\n");
		System.out.println("Avg. delay was " + diffSum/count + "\n");
		
		join();
		
		count = 0;
		diffSum = 0;
		total = -1;
	}
}
