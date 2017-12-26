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
import com.gramcha.entities.Transaction;

@Service
public class BlockChainService {
	@Autowired
	ConfigProvider env;
	@Autowired
	PeerService peerService;
	@Autowired
	BlockChainServiceData data;
	
	public BlockChainList getBlockChain() {
		return data.getChainList();
	}

	public Block addTransaction(Transaction transactionData) throws Exception {
		Block newBlock = createNextBlock(transactionData);
		if (isValidNewBlock(newBlock)) {
			System.out.println("before transaction = "+data.getChainList());
			data.add(newBlock);
			System.out.println("after transaction = "+data.getChainList());
			new Thread(() -> {
				handleBroadCastChainToOthers("queryallnodes");
			}).start();
		} else
			System.out.println("newblock is invalid!!!");
		return newBlock;
	}

	private String getTimeStamp() {
		return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new Date());
	}

	private Block createNextBlock(Transaction transactionData) throws Exception {
		Block previousBlock = getLastBlock();
		return new Block(previousBlock.getId() + 1, previousBlock.getBlockHash(), transactionData.getData(), getTimeStamp());
	}

	private Block getLastBlock() throws Exception {
		return getLastBlock(this.data.getTheBlockChain());
	}

	private Block getLastBlock(List<Block> blockChainList) throws Exception {
		if (false == CollectionUtils.isEmpty(blockChainList)) {
			return blockChainList.get(blockChainList.size() - 1);
		}
		throw new Exception("genesis block missing !!!");
	}

	private Boolean isValidNewBlock(Block newBlock) throws Exception {
		return isValidNewBlock(newBlock, getLastBlock());
	}

	private Boolean isValidNewBlock(Block newBlock, Block previousBlock) throws Exception {

		if(previousBlock.getId() + 1 != newBlock.getId()) {
			System.out.println("block id mismatch");
		} else if(false == newBlock.getPreviousBlockHash().equals(previousBlock.getBlockHash())) {
			System.out.println("previous hash mismatch");
		} else if(false == newBlock.getBlockHash().equals(Block.calculateHash(newBlock))) {
			System.out.println("*******");
			System.out.println("new block hash calc mismatch");
			System.out.println("newBlock = "+newBlock);
			System.out.println("newBlock.getBlockHash() = "+newBlock.getBlockHash());
			System.out.println("Block.calculateHash = "+Block.calculateHash(newBlock));
			System.out.println("Block.calculateBlockHash()= "+Block.calculateBlockHash(newBlock.getId(),newBlock.getPreviousBlockHash(),newBlock.getTransactionData(),newBlock.getTimeStamp()));
			System.out.println("*******");
		}
		return previousBlock.getId() + 1 == newBlock.getId()
				&& newBlock.getPreviousBlockHash().equals(previousBlock.getBlockHash())
				&& newBlock.getBlockHash().equals(Block.calculateHash(newBlock));
	}

	public Object handleBroadCastChain(BlockChainList receivedBlockChainList) throws Exception {
		List<Block> receivedList = receivedBlockChainList.getData();
		Collections.sort(receivedList, Comparator.comparingInt(Block::getId));
		Block lastBlockOfReceivedList = getLastBlock(receivedList);
		Block lastBlockofThisNode = getLastBlock();
		System.out.println("lastBlockOfReceivedList -"+lastBlockOfReceivedList.getId());
		System.out.println("lastBlockofThisNode -"+lastBlockofThisNode.getId());
		if (lastBlockOfReceivedList.getId() > lastBlockofThisNode.getId()) {
			if (lastBlockofThisNode.getBlockHash().equals(lastBlockOfReceivedList.getPreviousBlockHash())) {
				data.add(lastBlockOfReceivedList);
				System.out.println("new block added");
				new Thread(() -> {
					handleBroadCastChainToOthers("queryallnodes");
				}).start();
			}
		} else if (receivedList.size() == 1) {
			broadcastRequestToOrchestrator();
			System.out.println("broadcastRequestToOrchestrator");
		} else {
			System.out.println("update chain");
			updateChain(receivedList);
		}
		return null;
	}

	private void updateChain(List<Block> receivedList) throws Exception {
		if(isValidChain(receivedList) && receivedList.size() > data.getTheBlockChain().size()) {
			System.out.println("chain updation happening...");
			data.updateBlockChainData(data.getTheBlockChain());
			new Thread(() -> {
				handleBroadCastChainToOthers("queryallnodes");
			}).start();
		} else {
			System.out.println("invlaid chain or size less than current chain");
		}
	}

	private Boolean isValidChain(List<Block> receivedList) throws Exception {
		Block item = receivedList.get(0);
		System.out.println("recvd genesis block -"+item);
		Block genesisblock = data.getGenesisBlock();
		System.out.println("this genesis block -"+genesisblock);
		if (false == item.equals(genesisblock)) {
			System.out.println("genesis check failed");
			return false;
		}
		List<Block> tlist = new ArrayList<>();
		tlist.add(item);
		for (int i = 1; i < receivedList.size(); i++) {
			if (isValidNewBlock(receivedList.get(i), tlist.get(i - 1))) {
				tlist.add(receivedList.get(i));
			} else {
				System.out.println("invalid block" +"received block ("+receivedList.get(i)+")"+" != tlist ("+tlist.get(i - 1)+")");
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

	public Object handleBroadCastChainToOthers(String req) {
		if (true == env.isOrchestrator()) {
			System.out.println("sending list from orchestrator to others...");
			System.out.println("chain = "+this.data.getChainList());
			peerService.sendBlockChainListToOtherPeers(this.data.getChainList());
		} else {
			System.out.println("sending list to orchestrator...");
			System.out.println("chain = "+this.data.getChainList());
			peerService.sendBlockChainListToOrchestrator(this.data.getChainList());
		}
		return null;
	}
}
