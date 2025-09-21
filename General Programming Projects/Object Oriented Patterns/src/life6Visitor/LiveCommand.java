package life6Visitor;

public class LiveCommand extends LifeCommand{

	public LiveCommand(Cell r) {
		super(r);
	}

	@Override
	public void execute() { //for live command, execute tells the cell to live
		this.reciever.live();
	}

}
