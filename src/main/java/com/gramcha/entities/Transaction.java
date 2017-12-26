/**
 * @author gramcha
 * 22-Dec-2017 5:54:00 PM
 * 
 */
package com.gramcha.entities;

public class Transaction {
	private String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(data);
		return builder.toString();
	}
	
}
