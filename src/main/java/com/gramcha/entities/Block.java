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
	private String calculateBlockHash(Integer id, String previousBlockHash, Object transactionData, String timeStamp)
			throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest((id+previousBlockHash+transactionData+timeStamp).getBytes());
		return Base64.getEncoder().encodeToString(hash);
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

