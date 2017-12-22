/**
 * @author gramcha
 * 22-Dec-2017 6:25:14 PM
 * 
 */
package com.gramcha.entities;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

public class PeerList {
	private List<Peer> peers= new ArrayList<>();

	public List<Peer> getPeers() {
		return peers;
	}
	public void add(Peer newPeer) {
		peers.add(newPeer);
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
