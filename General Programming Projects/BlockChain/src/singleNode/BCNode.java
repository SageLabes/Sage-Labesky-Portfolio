package singleNode;

import java.util.ArrayList;
import java.io.*;
import java.net.ServerSocket;


public class BCNode {
	public static void main(String[] args) {
		BCNode chain = new BCNode();
		System.out.println(chain.toString());
		Block block1 = new Block("Hello World?");
		chain.addBlock(block1);
		System.out.println(chain.toString());
		Block block2 = new Block("HELLO WORLD!?!?!?!");
		chain.addBlock(block2);
		System.out.println(chain.toString());
	}
	
	
	ArrayList<Block> blockChain = new ArrayList<Block>();
	int n = 1;
	String prefixZeros = new String(new char[n]).replace('\0', '0');
	int port;
	
	public BCNode() {
		Block genesisBlock = new Block();
		blockChain.add(genesisBlock);
	}
	
	public BCNode(int port, ArrayList<Integer> remotePorts) throws IOException {
		this.port = port;
		ServerSocket SS = new ServerSocket(port);
		//if this is the first node it creates the genesis block
		int numberOfNodes = remotePorts.size();
		//Need threads here doing this
		for(int i = 0; i < numberOfNodes; i++) {
			
		}
		
		if(remotePorts.size() == 0) {
			Block genesisBlock = new Block();
			blockChain.add(genesisBlock);
		} else {
			
		}
		
		
		
		
		
	}
	
	public void addBlock(Block b) {
		String prevHash = blockChain.get(blockChain.size()-1).getCurrentBlockHash();
		b.setPreviousBlockHash(prevHash);
		b.calculateBlockHash();
		while(!(b.getCurrentBlockHash().substring(0, n).equals(prefixZeros))) {
			b.setNonce(b.getNonce()+1);
			b.calculateBlockHash();
		}
		
		blockChain.add(b);
		if(!validateChain()) {
			blockChain.remove(blockChain.size()-1);
		}
	}
	
	private boolean validateChain(){
		boolean isValid = true;
		for(int i = 1; i < blockChain.size(); i++) {
			Block currentBlock = blockChain.get(i);
			String currentHash = currentBlock.getCurrentBlockHash();
			String recalcHash = currentBlock.calculateBlockHash();
			if(!currentHash.equals(recalcHash)){

				isValid = false;
			}
			if(i != 0 && !(currentBlock.getPreviousBlockHash().equals(blockChain.get(i-1).getCurrentBlockHash()))) {
				isValid = false;
			}
			
			if(i != 0 && !(currentHash.substring(0, n).equals(prefixZeros))) {
				isValid = false;
			}
		}
		return isValid;
	}
	
	@Override
	public String toString() {
		String output = "";
		for(Block b:blockChain) {
			output += b.toString() + " -> ";
		}
		return output;
	}
}
