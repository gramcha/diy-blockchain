/**
 * @author gramcha
 * 22-Dec-2017 6:30:53 PM
 * 
 */
package com.gramcha.entities;

public class Peer {
	private String id;
	private String url;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Peer [id=");
		builder.append(id);
		builder.append(", url=");
		builder.append(url);
		builder.append("]");
		return builder.toString();
	}
	
}
