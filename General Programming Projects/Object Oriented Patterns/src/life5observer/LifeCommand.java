package life5observer;

public abstract class LifeCommand {
	//abstract command class, has a cell variable since that cell is what the command is given to
	protected Cell reciever;
	
	public LifeCommand(Cell r) { //constructor takes the cell
		reciever = r;
	}
	
	public abstract void execute(); //all command classes have an execute function
	
}
