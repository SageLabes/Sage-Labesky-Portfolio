package life3singleton;

import java.util.ArrayList;
import java.util.List;


public class Cell {
	private CellState state; //variable to keep track of state
	private List<Cell> neighbors; //variable to keep track of neighbors
	
	public Cell() { //the constructor calls deadstat.create() so that it is in the dead state
		state = DeadState.create();
		neighbors = new ArrayList<Cell>();
	}
	
	public void live() { //calls the cellstate live method
		this.state = state.live();
	}
	
	public void die() { //calls the cellstate die method
		this.state = state.die();
	}
	
	public boolean isAlive() { //checks if the cell is alive
		return this.state.isAlive();
	}
	
	public void addNeighbor(Cell neighborCell) { //adds a neighbor to the neighbors list
		neighbors.add(neighborCell);
	}
	
	public int nbrAliveNeighbors() { //checks how many neighbors there are
		int ret = 0;
		for(Cell n : neighbors) {
			if(n.isAlive()) {
				ret += 1;
			}
		}
		return ret;
	}
}
