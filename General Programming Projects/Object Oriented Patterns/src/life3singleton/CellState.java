package life3singleton;

public interface CellState {
	
	//provides the classes that both alive and deadstate will need
	
	public boolean isAlive();
	
	public CellState live();
	
	public CellState die();
	
}
