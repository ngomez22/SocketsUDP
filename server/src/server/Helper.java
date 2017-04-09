package server;

import message.Message;

public class Helper {
	
	private String filename;
	private int currentSeqNum;
	private int lostCount;
	
	public Helper(String filename, int currentSeqNum) {
		this.filename = filename;
		this.currentSeqNum = currentSeqNum;	
		this.lostCount = 0;
	}
	
	public void processMsg(Message m, long timeDiff) {
		int newCurrent = m.getSeqNum();
		System.out.println(newCurrent + ": " + timeDiff + " (from " + filename + ")");
	}


	public String getFilename() {
		return filename;
	}


	public int getCurrentSeqNum() {
		return currentSeqNum;
	}


	public void setCurrentSeqNum(int currentSeqNum) {
		this.currentSeqNum = currentSeqNum;
	}
	
	public int getLostCount() {
		return lostCount;
	}
	
	public void incLostCount(int inc) {
		lostCount += inc;
	}
}
