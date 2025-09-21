package life4command;

public class DeadState implements CellState{
	
	
	private static DeadState instance; //the singleton instance variable
	
	private DeadState() { //empty constructor that is private
		
	}
	
	public static DeadState create() { //the singleton create method, just returns instance instead of making a new one, ensuring only one will exist
		if(instance == null) {
			instance = new DeadState();
		}
		return instance;
	}
	
	
	@Override
	public boolean isAlive() { //this is the dead state for cells
		return false;
	}

	@Override
	public CellState live() { //returns alivestate
		return AliveState.create();
	}

	@Override
	public CellState die() { //just returns instance to avoid errors
		System.out.println("Cell is already in state: dead");
		return instance;
	}

}
