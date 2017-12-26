/**
 * @author gramcha
 * 22-Dec-2017 2:40:37 PM
 * 
 */
package com.gramcha.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gramcha.entities.Block;
import com.gramcha.entities.BlockChainList;
import com.gramcha.entities.Transaction;
import com.gramcha.service.BlockChainService;


@RestController
public class Index {
	@Autowired
	BlockChainService blockChainService;
	
	@RequestMapping(value="/")
	public ResponseEntity<String> ping() throws Exception{
		return ResponseEntity.ok("pong - ");
	}
	@RequestMapping(value="/blockchain")
	public ResponseEntity<BlockChainList> getBlockChain() throws Exception{		
		return ResponseEntity.ok(blockChainService.getBlockChain());
	}
	@RequestMapping(value="/transaction")
	public ResponseEntity<Block> postTransaction(@RequestBody Transaction transaction) throws Exception{
		System.out.println("/transaction endpoint...."+ transaction);
		return ResponseEntity.ok(blockChainService.addTransaction(transaction));
	}
	@RequestMapping(value="/broadcast")
	public ResponseEntity<Object> broadcastBlockChain(@RequestBody BlockChainList blockChainList) throws Exception{
		return ResponseEntity.ok(blockChainService.handleBroadCastChain(blockChainList));
	}
	@RequestMapping(value="/broadcasttoothers")
	public ResponseEntity<Object> broadcastBlockChainToOthers(@RequestBody String req){
		return ResponseEntity.ok(blockChainService.handleBroadCastChainToOthers(req));
	}
}
