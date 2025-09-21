package singleNode;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Block {
	private String data;
	private long timeStamp;
	private int nonce;
	private String currentBlockHash;
	private String previousBlockHash;
	
	public Block() {
		this.data = "Genesis Block";
		this.currentBlockHash = "";
		this.previousBlockHash = "";
		this.timeStamp = new Date().getTime();
		this.nonce = 0;
		this.currentBlockHash = calculateBlockHash();
	}	
	
	public Block(String data) {
		this.data = data;
		timeStamp = new Date().getTime();
		this.nonce = 0;
		this.currentBlockHash = calculateBlockHash();
	}
	
	public String calculateBlockHash() {
		try {
			String blockString = data + timeStamp + nonce + previousBlockHash;
			MessageDigest myDigest=MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = myDigest.digest(blockString.getBytes("UTF-8"));

			StringBuffer buffer = new StringBuffer();
			for (byte b: hashBytes) {
			      buffer.append(String.format("%02x", b));
			}
			String hashStr = buffer.toString();
			this.currentBlockHash = hashStr;
			return hashStr;
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int getNonce() {
		return nonce;
	}
	
	public void setNonce(int num) {
		this.nonce=num;
	}
	
	public String getCurrentBlockHash() {
		return currentBlockHash;
	}
	
	public void setCurrentBlockHash(String hash) {
		this.currentBlockHash = hash;
	}
	
	public String getPreviousBlockHash() {
		return previousBlockHash;
	}
	
	public void setPreviousBlockHash(String hash) {
		this.previousBlockHash = hash;
	}
	
	@Override
	public String toString() {
		String output = data + ", " + timeStamp;
		return output;
	}
}
