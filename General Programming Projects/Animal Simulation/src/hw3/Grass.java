package hw3;

import edu.du.dudraw.Draw;

public class Grass extends TerrainTile{

	private int vegetation = 100; //Grass has a vegetation variable
	
	protected Grass(GridPoint location) {
		super(location);

	}
	
	@Override
	public int getVeg() { //grass has to be able to share its vegetation
		return vegetation;
	}
	
	@Override
	public int getWet() { //is a little wet to keep humans away if they can
		return 2;
	}

	@Override
	public void setVeg(int newVeg) { //method used by avatars to affect the vegetation of the tile
		this.vegetation = newVeg;
	}
	
	@Override
	public void setColor(Draw duDwin) { //Sets color of grass to green
		duDwin.setPenColor(0, 250 - vegetation, 0);
		
	}
}
