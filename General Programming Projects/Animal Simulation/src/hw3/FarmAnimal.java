package hw3;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import edu.du.dudraw.Draw;

public abstract class FarmAnimal extends Avatar{

	protected FarmAnimal(TerrainMap tm, GridPoint location) {
		super(tm, location);
	}


	public abstract void draw(Draw duDwin); //abstract draw method that all avatars need

	@Override
	public void move() { //the farm animals move in the same pattern so move is declared in the super class
		double moveChance = Math.random(); //dont move all the time
		List<GridPoint> neighbors = location.getNeighbors(1);
		Collections.shuffle(neighbors); //randomizes the list of gridpoints so movement pattern isn't the same
		for(GridPoint g : neighbors) {
			if(!(this.tm.getTile(g) instanceof TerrainTile)) { //makes sure the tile is actually a terraintile and isn't out of bounds
				continue;
			} else if(moveChance > 0.65 && (this.tm.getTile(g) instanceof Grass || this.tm.getTile(g) instanceof Road)) { //moves the avatar if conditions are met
				this.location = g;
			}
			if(this.tm.getVeg(this.location) > 0 && this.tm.getVeg(this.location) <= 100) { //allows the avatars to eat (the cow is special so "eat" actually replenishes grass)
				this.eat();
			}
		}
	}


	protected abstract void eat(); // abstract eat method that farm animals share

}
