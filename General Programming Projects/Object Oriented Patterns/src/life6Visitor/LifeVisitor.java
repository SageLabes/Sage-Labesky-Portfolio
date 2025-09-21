package life6Visitor;

import java.util.ArrayList;

public abstract class LifeVisitor { //abstract visitor class
	
	public void visit(Cell c,  ArrayList<LifeCommand> commands) { //initial visit method takes a cell and calls accept which is sent to the state level
		c.accept(this, commands);
	}
	
	//Abstract classes needed for concrete visitors
	public abstract void visitLiveCell(Cell c, ArrayList<LifeCommand> commands);
	
	public abstract void visitDeadCell(Cell c, ArrayList<LifeCommand> commands);
}
