package life2state;

public class DeadState implements CellState{

	@Override
	public boolean isAlive() { //returns that a deadstate cell is dead
		return false;
	}

	@Override
	public CellState live() { //Returns a new alive state
		return new AliveState();
	}

	@Override
	public CellState die() { //returns a new dead state to avoid errors
		System.out.println("Cell is already in state: dead");
		return new DeadState();
	}

}
