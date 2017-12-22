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

import com.gramcha.entities.Block;

@Service
public class BlockChainService {

	List<Block> theBlockChain = new ArrayList<>();

	@PostConstruct
	void InitChain() throws NoSuchAlgorithmException {
		theBlockChain.add(getGenesisBlock());
	}

	Block getGenesisBlock() throws NoSuchAlgorithmException {
		return new Block(0, null, "Genesis Block - First transaction 0", getTimeStamp());
	}

	private String getTimeStamp() {
		return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(new Date());
	}
	
	public Block createNextBlock(Object transactionData) {
		return null;
	}
}
