package hw3;

import java.util.Collections;
import java.util.List;

import edu.du.dudraw.Draw;

public class Alligator extends Avatar{
	
	private boolean swimming = true;

	protected Alligator(TerrainMap tm, GridPoint location) {
		super(tm, location);
	}

	@Override
	public void processEvent(double mX, double mY) {
		
	}

	@Override
	public void draw(Draw duDwin) { //alligator is drawn differently if on land or in water
		if(swimming == true) {
			duDwin.picture(location.getY()*35 + 17.5, duDwin.getYscale()[1] - location.getX()*35 + 17.5, "SwimmingGator.png", 30,30);
		} else {
			duDwin.picture(location.getY()*35 + 17.5, duDwin.getYscale()[1] - location.getX()*35 + 17.5, "gatorwalk.png", 30,20);
		}
		
	}

	@Override
	public void move() {
		double moveChance = Math.random();
		
		if(moveChance > 0.9) { //doesn't move often, alligators are kinda lazy
			List<GridPoint> neighbors = location.getNeighbors(1);
			Collections.shuffle(neighbors);
			for(GridPoint g : neighbors) {
				if(!(this.tm.getTile(g) instanceof TerrainTile)) {
					continue;
				} else if(this.tm.getTile(g) instanceof Water) { //can move to water and is swimming if it does
					this.location = g;
					this.swimming = true;
				} else if(this.tm.getTile(g) instanceof Grass) { //can also move to grass and is not swimming when on land
					this.location = g;
					this.swimming = false;
				}
			}
		}
		
	}


}
