package life6Visitor;

import java.util.ArrayList;

public class StandardLifeVisitor extends LifeVisitor{

	@Override
	public void visitLiveCell(Cell c, ArrayList<LifeCommand> commands) { //handled if the cell is alive
		int n = c.nbrAliveNeighbors();
    
        if (!(n == 2 || n == 3)) { //cells with 2 or 3 alive neighbors perform in a certain way
            commands.add(new DieCommand(c));
        }
        
            
        
	}

	@Override
	public void visitDeadCell(Cell c, ArrayList<LifeCommand> commands) { //handled if the cell is dead
		int n = c.nbrAliveNeighbors();
		
		if (n == 3) { //Cells with exactly 3 neighbors that arent alive perform a certain way
        	commands.add(new LiveCommand(c));
        }
	}

}
