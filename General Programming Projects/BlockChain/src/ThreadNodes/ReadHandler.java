package ThreadNodes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ReadHandler implements Runnable{
	
	private ArrayList<ObjectOutputStream> oos;
	private ObjectInputStream ois;
	private BCNode node;
	private ArrayList<Block> blockChain;
	
	ReadHandler(ArrayList<ObjectOutputStream> oos, ObjectInputStream ois, BCNode node, ArrayList<Block> blockChain){
		this.oos = oos;
		this.ois = ois;
		this.node = node;
		this.blockChain = blockChain;
	}

	@Override
	public void run() {
		while(true) {
			try {
				//reads a new block, calls helper method to validate chain
				Block newBlock = (Block)ois.readObject();
				int before = blockChain.size();
				node.readNewBlock(newBlock);
				int after = blockChain.size();
				if(before != after) {
					for(int i = 0; i < oos.size(); i++) {
						oos.get(i).writeObject(newBlock);
						oos.get(i).reset();
					}
				}
				
			} catch (ClassNotFoundException | IOException e) {
				return;
			} 
		}
		
	}
}
