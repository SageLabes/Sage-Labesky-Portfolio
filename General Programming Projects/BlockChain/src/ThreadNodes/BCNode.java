package ThreadNodes;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class BCNode {
	private ArrayList<Socket> socList = new ArrayList<Socket>();
	private ArrayList<ObjectOutputStream> oos = new ArrayList<ObjectOutputStream>();
	private ArrayList<ObjectInputStream> ois = new ArrayList<ObjectInputStream>();
	private ArrayList<Block> blockChain = new ArrayList<Block>();
	//Change n to change the amount of prefix 0s and thus the mining speed
	private int n = 2;
	private String prefixZeros = new String(new char[n]).replace('\0', '0');
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Scanner keyScan = new Scanner(System.in);
		        
	        // Grab my port number on which to start this node
	        System.out.print("Enter port to start (on current IP): ");
	        int myPort = keyScan.nextInt();
	        
	        // Need to get what other Nodes to connect to
	        System.out.print("Enter remote ports (current IP is assumed): ");
	        keyScan.nextLine(); // skip the NL at the end of the previous scan int
	        String line = keyScan.nextLine();
	        ArrayList<Integer> remotePorts = new ArrayList<Integer>();
	        if (line != "") {
	            String[] splitLine = line.split(" ");
	            for (int i=0; i<splitLine.length; i++) {
	                remotePorts.add(Integer.parseInt(splitLine[i]));
	            }
	        }
	        // Create the Node
	        BCNode n = new BCNode(myPort, remotePorts);
	        
	        String ip = "";
	        try {
	             ip = Inet4Address.getLocalHost().getHostAddress();
	        } catch (UnknownHostException e) {
	            e.printStackTrace();
	            System.exit(1);
	        }
	        
	        System.out.println("Node started on " + ip + ": " + myPort);
	        
	        // Node command line interface
	        while(true) {
	            System.out.println("\nNODE on port: " + myPort);
	            System.out.println("1. Display Node's blockchain");
	            System.out.println("2. Create/mine new Block");
	            System.out.println("3. Kill Node");
	            System.out.print("Enter option: ");
	            int in = keyScan.nextInt();
	            
	            if (in == 1) {
	                System.out.println(n);
	                
	            } else if (in == 2) {
	                // Grab the information to put in the block
	                System.out.print("Enter information for new Block: ");
	                String blockInfo = keyScan.next();
	                Block b = new Block(blockInfo);
	                n.addBlockSocket(b);
	                
	            } else if (in == 3) {
	                // Take down the whole virtual machine (and all the threads)
	                //   for this Node.  If we just let main end, it would leave
	                //   up the Threads the node created.
	                keyScan.close();
	                System.exit(0);
	            }
	        }
	}
	
	
	
	
	public BCNode() {
		Block genesisBlock = new Block();
		blockChain.add(genesisBlock);
	}
	
	public BCNode(int port, ArrayList<Integer> remotePorts) throws IOException, ClassNotFoundException {
		ServerSocket SS = new ServerSocket(port);

		//if this is the first node it creates the genesis block
		if(remotePorts.size() == 0) {
			Block genesisBlock = new Block();
			blockChain.add(genesisBlock);
		} else { //gets the full blockchain from previous nodes if it is added after them
			for(int i = 0; i < remotePorts.size(); i++) {
				socList.add(new Socket("localhost", remotePorts.get(i)));
				int index = socList.size()-1;
				oos.add(new ObjectOutputStream(socList.get(index).getOutputStream()));
				ois.add(new ObjectInputStream(socList.get(index).getInputStream()));
				blockChain = (ArrayList<Block>)ois.get(ois.size()-1).readObject();
			}
		}
		//Starts the connection handler thread
		ConnectionHandler ch = new ConnectionHandler(blockChain, SS, socList, oos, ois, this);
		Thread connectionHandlerTH = new Thread(ch);
		connectionHandlerTH.start();
		//starts read handler for every node that already exists when this one is instantiated
		for(int i = 0; i < socList.size(); i++) {
			ReadHandler rh = new ReadHandler(oos, ois.get(i) , this, blockChain);
			Thread rhThread = new Thread(rh);
			rhThread.start();
		}
		
		
	}
	
	private void addBlock(Block b) { //addblock for one node, doesn't interact with thread and sockets just block chain
		String prevHash = blockChain.get(blockChain.size()-1).getCurrentBlockHash();
		b.setPreviousBlockHash(prevHash);
		b.calculateBlockHash();
		while(!(b.getCurrentBlockHash().substring(0, n).equals(prefixZeros))) {
			b.setNonce(b.getNonce()+1);
			b.calculateBlockHash();
		}
		//checks that block is valid
		blockChain.add(b);
		if(!validateChain()) {
			blockChain.remove(blockChain.size()-1);
		}
	}
	
	private void addBlockSocket(Block b){ //socket side of add block, calls add block for stuff on this node
		int before = blockChain.size();
		addBlock(b);
		int after = blockChain.size();
		if(before != after) {
			for(int i = 0; i < oos.size(); i++) {
				try {
					oos.get(i).writeObject(b);
					oos.get(i).reset();
				} catch (IOException e) {

				}
			}
		}
	}
	
	public synchronized void readNewBlock(Block b) {
		//helper method used in read handler that adds a block to the blockchain without doing any new calculations
		blockChain.add(b);
		if(!validateChain()) {
			blockChain.remove(blockChain.size()-1);
		}
	}
	
	private boolean validateChain(){
		//validates the whole block chain
		boolean isValid = true;
		for(int i = 1; i < blockChain.size(); i++) {
			Block currentBlock = blockChain.get(i);
			String currentHash = currentBlock.getCurrentBlockHash();
			String recalcHash = currentBlock.calculateBlockHash();
			//checks all three conditions a valid block must pass
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
