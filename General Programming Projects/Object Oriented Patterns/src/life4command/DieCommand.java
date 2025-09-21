package life4command;

public class DieCommand extends LifeCommand{

	public DieCommand(Cell r) {
		super(r);
	}


	@Override
	public void execute() { //for die command, the command tells the cell to die
		this.reciever.die();
		
	}

}
