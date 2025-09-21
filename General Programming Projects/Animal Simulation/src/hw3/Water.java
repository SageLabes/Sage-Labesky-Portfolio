package hw3;

import edu.du.dudraw.DUDraw;
import edu.du.dudraw.Draw;

public class Water extends TerrainTile{

	protected Water(GridPoint location) { //The typical constructor that TerrainTile already has
		super(location);
	}

	@Override
	public int getWet() { //Is water wet? yes.
		return 85;
	}
	
	@Override
	public void setColor(Draw duDwin) { //Sets water to water color
		duDwin.setPenColor(0, 100, 255);
		
	}

}
