package life6Visitor;

import java.util.ArrayList;

public interface CellState {
	
	//provides the classes that both alive and deadstate will need
	
	public boolean isAlive();
	
	public CellState live();
	
	public CellState die();
	
	public void accept(LifeVisitor v, Cell c, ArrayList<LifeCommand> commands);
}
