/**
 * @author gramcha
 * 26-Dec-2017 4:21:55 PM
 * 
 */
package com.gramcha.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.gramcha.entities.Block;
import com.gramcha.entities.BlockChainList;

@Service
public class BlockChainServiceData {
	private BlockChainList chainList = new BlockChainList();
	private List<Block> theBlockChain = new ArrayList<>();
	private Block genesisBlock = null;
	public BlockChainList getChainList() {
		return chainList;
	}

	public List<Block> getTheBlockChain() {
		return chainList.getData();
	}

	Block getGenesisBlock() throws NoSuchAlgorithmException {
		return genesisBlock;
	}
	
	void updateBlockChainData(List<Block> blocks) {
		chainList.setData(blocks);
	}
	
	@PostConstruct
	void InitChain() throws NoSuchAlgorithmException {
		this.genesisBlock = new Block(0, null, "Genesis Block - First transaction 0", null);
		theBlockChain.add(getGenesisBlock());
		chainList.setData(theBlockChain);
	}

	public void add(Block newBlock) {
		chainList.addBlockToList(newBlock);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BlockChainServiceData [chainList=");
		builder.append(chainList);
		builder.append(", theBlockChain=");
		builder.append(theBlockChain);
		builder.append(", genesisBlock=");
		builder.append(genesisBlock);
		builder.append("]");
		return builder.toString();
	}

}