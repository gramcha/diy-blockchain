/**
 * @author gramcha
 * 22-Dec-2017 6:26:20 PM
 * 
 */
package com.gramcha.service;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gramcha.config.ConfigProvider;
import com.gramcha.entities.Peer;
import com.gramcha.entities.PeerList;

@Service
public class PeerService {
	@Autowired
	ConfigProvider env;
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

	public String getThisNodeIpAddress() throws SocketException, UnknownHostException {
		String ipAddress;
		try(final DatagramSocket socket = new DatagramSocket()){
			  socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			  ipAddress = socket.getLocalAddress().getHostAddress();
			}
		return ipAddress;
	}
	public String getThisNodePortNumber() {
		return env.getPortNumber();
	}
	public String getFullUrl() throws SocketException, UnknownHostException {
		return "http://"+getThisNodeIpAddress()+":"+getThisNodePortNumber()+"/";
	}
}
