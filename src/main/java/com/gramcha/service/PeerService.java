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

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gramcha.config.ConfigProvider;
import com.gramcha.entities.BlockChainList;
import com.gramcha.entities.Peer;
import com.gramcha.entities.PeerList;

@Service
public class PeerService {
	@Autowired
	ConfigProvider env;
	private PeerList peerList = new PeerList();
	@Autowired
	BlockChainServiceData data;
	
	@PostConstruct
	void init() throws SocketException, UnknownHostException {
		if (false == env.isOrchestrator()) {
			String orchestratorUrl = env.getOrchestratorUrl();
			RestTemplate restTemplate = new RestTemplate();
			String targetUrl = orchestratorUrl + "addpeer";
			Peer node = new Peer();
			node.setId("some id");// set uuid
			node.setUrl(getFullUrl());
			System.out.println("before orc update - "+data.getChainList().getData());
			BlockChainList orchestratorResponse = restTemplate.postForObject(targetUrl, node, BlockChainList.class);
			data.updateBlockChainData(orchestratorResponse.getData());
			System.out.println("after orc update - "+data.getChainList().getData());
		}
	}

	public PeerList getPeerList() {
		return peerList;
	}

	public BlockChainList addPeer(Peer newPeer) {
		peerList.add(newPeer);
		return data.getChainList();
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
		try (final DatagramSocket socket = new DatagramSocket()) {
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			ipAddress = socket.getLocalAddress().getHostAddress();
		}
		return ipAddress;
	}

	public String getThisNodePortNumber() {
		return env.getPortNumber();
	}

	public String getFullUrl() throws SocketException, UnknownHostException {
		return "http://" + getThisNodeIpAddress() + ":" + getThisNodePortNumber() + "/";
	}

	public void sendBlockChainListToOtherPeers(BlockChainList chainList) {
		peerList.getPeers().forEach((url,peer)->{
			String targetUrl = peer.getUrl() + "broadcast";
			String peerResponse = sendToSpecificPeer(chainList, targetUrl);
			System.out.println(peerResponse);
			});
	}

	private String sendToSpecificPeer(BlockChainList chainList, String targetUrl) {
		RestTemplate restTemplate = new RestTemplate();
		System.out.println("********");
		System.out.println(targetUrl);
		System.out.println("********");
		return restTemplate.postForObject(targetUrl, chainList, String.class);
	}
	public void sendBlockChainListToOrchestrator(BlockChainList chainList) {
		RestTemplate restTemplate = new RestTemplate();
		String targetUrl = env.getOrchestratorUrl() + "broadcast";
		String orchestratorResponse = sendToSpecificPeer(chainList, targetUrl);
		System.out.println(orchestratorResponse);
	}
	
}
