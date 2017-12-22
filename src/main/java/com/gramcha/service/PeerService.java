/**
 * @author gramcha
 * 22-Dec-2017 6:26:20 PM
 * 
 */
package com.gramcha.service;

import org.springframework.stereotype.Service;

import com.gramcha.entities.Peer;
import com.gramcha.entities.PeerList;

@Service
public class PeerService {
	private PeerList peerList = new PeerList();

	public PeerList getPeerList() {
		return peerList;
	}

	public void addPeer(Peer newPeer) {
		peerList.add(newPeer);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PeerService [peerList=");
		builder.append(peerList);
		builder.append("]");
		return builder.toString();
	}
}
