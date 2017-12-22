/**
 * @author gramcha
 * 22-Dec-2017 3:27:53 PM
 * 
 */
package com.gramcha.service;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.gramcha.entities.Block;
import com.gramcha.entities.BlockChainList;

@Service
public class BlockChainService {

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
		if(isValidNewBlock(newBlock))
			theBlockChain.add(newBlock);
		else
			System.out.println("newblock is invalid!!!");
		return newBlock;
	}
	
	Block getGenesisBlock() throws NoSuchAlgorithmException {
		return new Block(0, null, "Genesis Block - First transaction 0", getTimeStamp());
	}

	private String getTimeStamp() {
		return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new Date());
	}
	
	private Block createNextBlock(Object transactionData) throws Exception {
		Block previousBlock = getLastBlock();
		return new Block(previousBlock.getId()+1, previousBlock.getBlockHash(), transactionData, getTimeStamp());
	}
	
	private Block getLastBlock() throws Exception {
		if(false == CollectionUtils.isEmpty(theBlockChain)) {
			return theBlockChain.get(theBlockChain.size()-1);
		}
		throw new Exception("genesis block missing !!!");
	}
	
	private Boolean isValidNewBlock(Block newBlock) throws Exception {
		Block latestBlock = getLastBlock();		
		return newBlock.getPreviousBlockHash().equals(latestBlock.getBlockHash())
				&& newBlock.getId() == latestBlock.getId()
				&& newBlock.getBlockHash().equals(Block.calculateHash(newBlock));
	}
}
