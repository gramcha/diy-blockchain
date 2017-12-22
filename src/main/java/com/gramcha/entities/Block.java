/**
 * @author gramcha
 * 22-Dec-2017 2:48:54 PM
 * 
 */
package com.gramcha.entities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Block {

	Integer id;
	String previousBlockHash;
	Object transactionData;
	String timeStamp;
	String blockHash;
	/**
	 * @param id
	 * @param previousHash
	 * @param transactionData
	 * @param timeStamp
	 * @throws NoSuchAlgorithmException 
	 */
	public Block(Integer id, String previousBlockHash, Object transactionData, String timeStamp) throws NoSuchAlgorithmException {
		super();
		this.id = id;
		this.previousBlockHash = previousBlockHash;
		this.transactionData = transactionData;
		this.timeStamp = timeStamp;
		
		this.blockHash = calculateBlockHash(id, previousBlockHash, transactionData, timeStamp);
	}
	private static String calculateBlockHash(Integer id, String previousBlockHash, Object transactionData, String timeStamp)
			throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest((id+previousBlockHash+transactionData+timeStamp).getBytes());
		return Base64.getEncoder().encodeToString(hash);
	}
	public static String calculateHash(Block block) throws NoSuchAlgorithmException {
		return calculateBlockHash(block.getId(), block.getPreviousBlockHash(), block.getTransactionData(), block.getTimeStamp());
	}
	public Integer getId() {
		return id;
	}
	public String getPreviousBlockHash() {
		return previousBlockHash;
	}
	public Object getTransactionData() {
		return transactionData;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public String getBlockHash() {
		return blockHash;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blockHash == null) ? 0 : blockHash.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((previousBlockHash == null) ? 0 : previousBlockHash.hashCode());
		result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
		result = prime * result + ((transactionData == null) ? 0 : transactionData.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Block other = (Block) obj;
		if (blockHash == null) {
			if (other.blockHash != null)
				return false;
		} else if (!blockHash.equals(other.blockHash))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (previousBlockHash == null) {
			if (other.previousBlockHash != null)
				return false;
		} else if (!previousBlockHash.equals(other.previousBlockHash))
			return false;
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		if (transactionData == null) {
			if (other.transactionData != null)
				return false;
		} else if (!transactionData.equals(other.transactionData))
			return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Block [id=");
		builder.append(id);
		builder.append(", previousBlockHash=");
		builder.append(previousBlockHash);
		builder.append(", transactionData=");
		builder.append(transactionData);
		builder.append(", timeStamp=");
		builder.append(timeStamp);
		builder.append(", blockHash=");
		builder.append(blockHash);
		builder.append("]");
		return builder.toString();
	}
	
}

