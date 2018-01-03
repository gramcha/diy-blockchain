# diy-blockchain

Simple implementation of block chain. This is developed as springboot web application and exposes some rest endpoints for communicating within peers.

## What is block?
Each block typically contains a cryptographic hash as a link to a previous block, a timestamp and transaction data. It will also have the cryptographic hash of the current block. First block called genesis block which has root dummy transaction at the start. This block will have null for the previous block hash

Members of a block in this implementation 

    * previousBlockHash - cryptographic hash value of previous block
    * transactionData - record of the current transaction
    * timeStamp - timestamp of the current transaction
    * id - block id
    * blockHash - cryptographic hash value of this block

## What is block chain?
Block chain is a continuously growing list of transaction records, called blocks, which are linked and secured using cryptography. By design, blockchains are inherently resistant to modification of the data. An open, distributed ledger that can record transactions between two parties efficiently and in a verifiable and permanent way.For use as a distributed ledger, a blockchain is typically managed by a peer-to-peer network collectively adhering to a protocol for validating new blocks.

In this implementation peer-peer is not implemented using web sockets. Instead rest service used for the broadcasting the a transaction from a node to other peers in network.

## System Design
This system designed as an orchestrator and normal peers. This can be configured in the application.properties file. Because we did not implement true peer to peer network here. The orchestrator will have the list of peers and each pair will be updated whenever a new transaction happens in any other peer or orchestrator.

**application.properties file of an orchestrator**

    server.port = 8080
    isOrchestrator=true
    OrchestratorUrl=http://localhost:8080/

**application.properties file of a peer**

    server.port = 8081
    isOrchestrator=false
    OrchestratorUrl=http://localhost:8080/

Each peer will have the different server.port. This web service will act as an orchestrator if **isOrchestrator** is true in **application.properties** file. The peer will contact orchestrator and will register its address with orchestrator. The orhestorator will return the available full block chain ledger to newly registered peer.


# Endpoints

1.**addpeer**
This endpoint is opened for the orchestrator service. Each peer node will register its address during startup time by calling this endpoint of orchestrator.

2.**transaction**
This endpoint accepts the transaction data. The endpoint is open in orchestrator and other peers. The new block will be added when the transition happens.

3.**broadcast**
This endpoint is open for orchestrate service. It is used by Peer to send its block chain list to orchestor for updating other peers about the new block addition. Orchestrator will validate the incoming block chain from a peer, it will update its own and request to other peers for updating same.
 
