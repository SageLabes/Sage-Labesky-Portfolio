package life6Visitor;

import edu.du.dudraw.DrawListener;

public abstract class LifeObserver implements DrawListener{

	public GameOfLife game; //observers take in a GameOfLife and store it so they can access it
	
	public LifeObserver(GameOfLife game) { //constructor requires a game
		this.game = game;
	}
	
	public abstract void update();
}
