package life4command;

public class AliveState implements CellState{
	
	
	private static AliveState instance;
	
	private AliveState() { //constructor is private due to singleton conventions
		
	}
	
	public static AliveState create() { //The singleton create method that calls the constructor only once
		if(instance == null) {
			instance = new AliveState();
		}
		return instance;
	}
	
	@Override
	public boolean isAlive() { //checks if cell is alive
		return true;
	}

	@Override
	public CellState live() { //returns the same state to avoid errors
		System.out.println("Cell is already in state: alive");
		return instance;
	}

	@Override
	public CellState die() { //returns the deadstate
		return DeadState.create();
	}

}
