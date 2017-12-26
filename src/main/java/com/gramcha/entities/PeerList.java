/**
 * @author gramcha
 * 22-Dec-2017 6:25:14 PM
 * 
 */
package com.gramcha.entities;

import java.util.HashMap;
import java.util.Map;

public class PeerList {
	private Map<String,Peer> peers= new HashMap<>();

	public Map<String,Peer> getPeers() {
		return peers;
	}
	public void add(Peer newPeer) {
		peers.put(newPeer.getUrl(), newPeer);
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PeerList [peers=");
		builder.append(peers);
		builder.append("]");
		return builder.toString();
	}
	
}
