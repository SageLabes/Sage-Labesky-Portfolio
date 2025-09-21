package life2state;

public class AliveState implements CellState{

	@Override
	public boolean isAlive() { //Checks if the state is alive
		return true;
	}

	@Override
	public CellState live() { //the state is already alive but it returns a new alivestate to avoid errors
		System.out.println("Cell is already in state: alive");
		return new AliveState();
	}

	@Override
	public CellState die() { //creates and returns a new deadstate
		return new DeadState();
	}

}
