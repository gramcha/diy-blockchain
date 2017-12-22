/**
 * @author gramcha
 * 22-Dec-2017 6:22:33 PM
 * 
 */
package com.gramcha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gramcha.entities.Peer;
import com.gramcha.entities.PeerList;
import com.gramcha.entities.Transaction;
import com.gramcha.service.PeerService;

@RestController
public class PeersController {
	@Autowired
	PeerService peerService;
	@RequestMapping(value="/peers")
	public ResponseEntity<PeerList> getBlockChain() throws Exception{		
		return ResponseEntity.ok(peerService.getPeerList());
	}
	@RequestMapping(value="/addpeer")
	public ResponseEntity<String> postTest(@RequestBody Peer newPeer){
		peerService.addPeer(newPeer);
		return ResponseEntity.ok("added");
	}
}
