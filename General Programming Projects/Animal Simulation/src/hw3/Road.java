package hw3;

import edu.du.dudraw.DUDraw;
import edu.du.dudraw.Draw;

public class Road extends TerrainTile{

	
	
	protected Road(GridPoint location) {
		super(location);
	}
	
	@Override
	public void setColor(Draw duDwin) { //Sets road to yellow
		duDwin.setPenColor(255, 255, 0);
		
	}

}
