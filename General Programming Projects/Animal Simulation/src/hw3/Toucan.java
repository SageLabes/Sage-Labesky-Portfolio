package hw3;

import java.util.Collections;
import java.util.List;

import edu.du.dudraw.Draw;

public class Toucan extends Avatar{
	
	private boolean flying = true;

	protected Toucan(TerrainMap tm, GridPoint location) {
		super(tm, location);
	}

	@Override
	public void processEvent(double mX, double mY) {
		
	}

	@Override
	public void draw(Draw duDwin) { //toucan is either on the ground or flying so it is drawn in different ways
		if(flying == true) {
			duDwin.picture(location.getY()*35 + 17.5, duDwin.getYscale()[1] - location.getX()*35 + 17.5, "flyingToucan.png", 30,30);
		} else {
			duDwin.picture(location.getY()*35 + 17.5, duDwin.getYscale()[1] - location.getX()*35 + 17.5, "groundToucan.png", 30,20);
		}
	}

	@Override
	public void move() {
		double moveChance = Math.random();
		double restChance = Math.random(); //has a chance to rest while on the mountains
		List<GridPoint> neighbors = location.getNeighbors(2); //toucan is very fast so it can see 2 layers of neighbors
		Collections.shuffle(neighbors);
		if(flying == true) {
			for(GridPoint g : neighbors) {
				if(!(this.tm.getTile(g) instanceof TerrainTile)) {
					continue;
				} else if(moveChance > 0.25) {
					this.location = g;
				} else if(tm.getTile(location) instanceof Mountain) { //if rest chance is high and the toucan is on mountains it rests
					if(restChance > 0.1) {
						flying = false;
					}
				}
			}
		}
		if(restChance > 0.9) { //if rest chance is high and toucan is resting it will stop resting
			flying = true;
		}
		
	}

}
