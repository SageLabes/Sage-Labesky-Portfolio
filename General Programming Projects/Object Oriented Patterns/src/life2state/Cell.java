package life2state;

import java.util.ArrayList;
import java.util.List;

public class Cell {
	
	private CellState state; //Cell objects have a state, either alive or dead
	private List<Cell> neighbors; //list of neighbors
	
	public Cell() { //cells start out as dead
		state = new DeadState();
		neighbors = new ArrayList<Cell>();
	}
	
	public void live() { //switches to living
		this.state = state.live();
	}
	
	public void die() { //switches to dead
		this.state = state.die();
	}
	
	public boolean isAlive() { //Returns wether the cell is alive
		return this.state.isAlive();
	}
	
	public void addNeighbor(Cell neighborCell) { //method that adds neighbors to the list
		neighbors.add(neighborCell);
	}
	
	public int nbrAliveNeighbors() { //method to count the neighbors that are alive
		int ret = 0;
		for(Cell n : neighbors) { //traverses the list of neigbors and checks them with a counter
			if(n.isAlive()) {
				
				ret += 1;
			}
		}
		return ret;
	}
	
}
