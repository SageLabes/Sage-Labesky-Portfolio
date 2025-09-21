package ThreadNodes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionHandler implements Runnable{
	
	private ServerSocket ss;
	private ArrayList<Socket> sockets;
	private ArrayList<ObjectOutputStream> oos;
	private ArrayList<ObjectInputStream> ois;
	private ArrayList<Block> blockChain;
	private BCNode node;
	
	ConnectionHandler(ArrayList<Block> blockChain, ServerSocket ss, ArrayList<Socket> sockets, ArrayList<ObjectOutputStream> oos, ArrayList<ObjectInputStream> ois, BCNode node){
		this.ss = ss;
		this.sockets = sockets;
		this.oos = oos;
		this.ois = ois;
		this.blockChain = blockChain;
		this.node = node;
	}
	@Override
	public void run() {
		while(true) {
			try {
				addToLists();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private synchronized void addToLists() throws IOException{
		//method that adds the new nodes ois, oos, and socket to the respective lists
		Socket S = ss.accept();
		sockets.add(S);
		ObjectOutputStream oos1 = new ObjectOutputStream(S.getOutputStream());
		oos.add(oos1);
		//shares the blockChain with the new node
		oos1.writeObject(blockChain);
		
		ois.add(new ObjectInputStream(S.getInputStream()));
		//creates a new read handler thread for the new node that connected
		ReadHandler rh = new ReadHandler(oos, ois.get(ois.size()-1), node, blockChain);
		Thread rhThread = new Thread(rh);
		rhThread.start();		
	}

}
