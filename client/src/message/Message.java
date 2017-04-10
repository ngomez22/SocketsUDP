package message;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = -4734548251876081005L;
	private int seqNum;
	private int total;
	private long timestamp;
	private String hashCode;
	private byte[] chunkOfFile;

	public Message(int seqNum, int total, String hash, byte[] chunk) {
		this.seqNum = seqNum;
		this.total = total;
		this.timestamp = System.currentTimeMillis();
		this.hashCode = hash;
		this.chunkOfFile = chunk;
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

	public String getHashCode() {
		return hashCode;
	}

	public byte[] getChunkOfFile() {
		return chunkOfFile;
	}
	
}
