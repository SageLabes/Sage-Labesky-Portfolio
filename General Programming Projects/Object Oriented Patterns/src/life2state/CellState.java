package life2state;

public interface CellState {
	
	//the CellState interface is for alivestate and deadstate, it includes a method to check state and two methods to change state
	
	public boolean isAlive(); 
	
	public CellState live();
	
	public CellState die();
	
}
