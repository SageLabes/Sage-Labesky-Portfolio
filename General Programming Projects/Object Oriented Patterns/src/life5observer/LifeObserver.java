package life5observer;

import edu.du.dudraw.DrawListener;

public abstract class LifeObserver implements DrawListener{

	public GameOfLife game; //stores an instance of a GameOfLife so the UI can access it
	
	public LifeObserver(GameOfLife game) { //constructor requires a game
		this.game = game;
	}
	
	public abstract void update();
}
