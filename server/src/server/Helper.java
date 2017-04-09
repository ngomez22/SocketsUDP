package server;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import message.Message;

public class Helper {
	
	private String filename;
	private BufferedWriter bw;
	private int count;
	private int total;
	private long diffSum;
	
	public Helper(String filename, int total) throws FileNotFoundException {
		this.filename = filename;
		this.bw = new BufferedWriter(new PrintWriter(filename));
		this.count = 0;
		this.total = total;
		this.diffSum = 0;
	}
	
	public void processMsg(Message m, long timeDiff) throws IOException {
		if(m.getTotal() != total) {
			done();
			total = m.getTotal();
		}
		bw.write(m.getSeqNum() + ": " + timeDiff + "\n");
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
	
	public void done() throws IOException {
		bw.write("----DONE----\n");
		bw.write("Received " + count + "/" + total + "\n");
		bw.write("Avg. delay was " + diffSum/count + "\n");
		count = 0;
		diffSum = 0;
		total = -1;
		
	}
}
