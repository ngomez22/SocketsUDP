package message;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 4400321123717576207L;
	private int seqNum;
	private int total;
	private long timestamp;

	public Message(int seqNum, int total) {
		this.seqNum = seqNum;
		this.total = total;
		this.timestamp = System.currentTimeMillis();
	}

	public int getSeqNum() {
		return seqNum;
	}

	public long getTimestamp() {
		return timestamp;
	}
	
	public int getTotal() {
		return total;
	}
}
