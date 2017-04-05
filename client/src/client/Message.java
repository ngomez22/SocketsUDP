package client;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 4400321123717576207L;
	private int seqNun;
	private long timestamp;

	public Message(int seqNum) {
		this.seqNun = seqNum;
		this.timestamp = System.currentTimeMillis();
	}

	public int getSeqNun() {
		return seqNun;
	}

	public long getTimestamp() {
		return timestamp;
	}
}
