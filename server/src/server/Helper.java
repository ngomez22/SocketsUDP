package server;

import message.Message;

public class Helper {
	
	private String filename;
	private int count;
	private int total;
	private long diffSum;
	private boolean[] received;
	
	public Helper(String filename, int total) {
		this.filename = filename;
		this.count = 0;
		this.total = total;
		this.diffSum = 0;
		this.received = new boolean[total];
	}
	
	public void processMsg(Message m, long timeDiff) {
		System.out.println(m.getSeqNum() + ": " + timeDiff + " (from " + filename + ")");
		count++;
		diffSum += timeDiff;
		if(m.getSeqNum() == total) {
			done();
		}
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
	
	public void done() {
		System.out.println("----DONE----");
		System.out.println("Received " + count + "/" + total);
		System.out.println("Avg. delay was " + diffSum/count);
		count = 0;
		diffSum = 0;
		total = -1;
		
	}
}
