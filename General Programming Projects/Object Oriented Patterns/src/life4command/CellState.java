package life4command;

public interface CellState {
	
	//provides the classes that both alive and deadstate will need
	
	public boolean isAlive();
	
	public CellState live();
	
	public CellState die();
	
}
