/**
 * @author gramcha
 * 22-Dec-2017 3:27:53 PM
 * 
 */
package com.gramcha.service;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.gramcha.config.ConfigProvider;
import com.gramcha.entities.Block;
import com.gramcha.entities.BlockChainList;
import com.gramcha.entities.Peer;

@Service
public class BlockChainService {
	@Autowired
	ConfigProvider env;
	@Autowired
	PeerService peerService;
	BlockChainList chainList = new BlockChainList();
	List<Block> theBlockChain = new ArrayList<>();

	@PostConstruct
	void InitChain() throws NoSuchAlgorithmException {
		theBlockChain.add(getGenesisBlock());
		chainList.setData(theBlockChain);
	}

	public BlockChainList getBlockChain() {
		return chainList;
	}

	public Block addTransaction(Object transactionData) throws Exception {
		Block newBlock = createNextBlock(transactionData);
		if (isValidNewBlock(newBlock))
			theBlockChain.add(newBlock);
		else
			System.out.println("newblock is invalid!!!");
		new Thread(() -> {
			handleBroadCastChainToOthers("queryallnodes");
		}).start();
		return newBlock;
	}

	Block getGenesisBlock() throws NoSuchAlgorithmException {
		return new Block(0, null, "Genesis Block - First transaction 0", null);
	}

	private String getTimeStamp() {
		return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new Date());
	}

	private Block createNextBlock(Object transactionData) throws Exception {
		Block previousBlock = getLastBlock();
		return new Block(previousBlock.getId() + 1, previousBlock.getBlockHash(), transactionData, getTimeStamp());
	}

	private Block getLastBlock() throws Exception {
		return getLastBlock(this.theBlockChain);
	}

	private Block getLastBlock(List<Block> blockChainList) throws Exception {
		if (false == CollectionUtils.isEmpty(theBlockChain)) {
			return blockChainList.get(theBlockChain.size() - 1);
		}
		throw new Exception("genesis block missing !!!");
	}

	private Boolean isValidNewBlock(Block newBlock) throws Exception {
		return isValidNewBlock(newBlock, getLastBlock());
	}

	private Boolean isValidNewBlock(Block newBlock, Block previousBlock) throws Exception {

		return previousBlock.getId() + 1 == newBlock.getId()
				&& newBlock.getPreviousBlockHash().equals(previousBlock.getBlockHash())
				&& newBlock.getBlockHash().equals(Block.calculateHash(newBlock));
	}

	public Object handleBroadCastChain(BlockChainList receivedBlockChainList) throws Exception {
		List<Block> receivedList = receivedBlockChainList.getData();
		Collections.sort(receivedList, Comparator.comparingInt(Block::getId));
		Block lastBlockOfReceivedList = getLastBlock(receivedList);
		Block lastBlockofThisNode = getLastBlock();
		if (lastBlockOfReceivedList.getId() > lastBlockofThisNode.getId()) {
			if (lastBlockofThisNode.getBlockHash().equals(lastBlockOfReceivedList.getPreviousBlockHash())) {
				theBlockChain.add(lastBlockOfReceivedList);
			}
		} else if (receivedList.size() == 1) {
			broadcastRequestToOrchestrator();
		} else {
			updateChain(receivedList);
		}
		return null;
	}

	private void updateChain(List<Block> receivedList) throws Exception {
		if(isValidChain(receivedList) && receivedList.size() > theBlockChain.size()) {
			theBlockChain = receivedList;
			chainList.setData(theBlockChain);
		}
	}

	private Boolean isValidChain(List<Block> receivedList) throws Exception {
		Block item = receivedList.get(0);
		if (item.equals(getGenesisBlock()))
			return false;
		List<Block> tlist = new ArrayList<>();
		tlist.add(item);
		for (int i = 1; i < receivedList.size(); i++) {
			if (isValidNewBlock(receivedList.get(i), tlist.get(i - 1))) {
				tlist.add(receivedList.get(i));
			} else {
				return false;
			}
		}
		return true;
	}

	private void broadcastRequestToOrchestrator() {
		// TODO Auto-generated method stub
		if (false == env.isOrchestrator()) {
			String orchestratorUrl = env.getOrchestratorUrl();
			RestTemplate restTemplate = new RestTemplate();
			String targetUrl = orchestratorUrl + "broadcasttoothers";
			String orchestratorResponse = restTemplate.postForObject(targetUrl, "queryallnodes", String.class);
			System.out.println(orchestratorResponse);
		}
	}

	/**
	 * @param req
	 * @return
	 */
	public Object handleBroadCastChainToOthers(String req) {
		if (true == env.isOrchestrator()) {
			peerService.sendBlockChainList(this.chainList);
		}
		return null;
	}
}
