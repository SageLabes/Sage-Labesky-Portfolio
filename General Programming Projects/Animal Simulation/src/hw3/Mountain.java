package hw3;

import edu.du.dudraw.Draw;

public class Mountain extends TerrainTile{ //Inherits from TerrainTile

	private int vegetation = (int)(Math.random()*50); //Mountain has a special variable for vegetation
	
	protected Mountain(GridPoint location) {
		super(location);
	}

	@Override
	public int getVeg() { //mountains have vegetation
		return vegetation;
	}
	
	@Override
	public int getBumpy() { //mountains are bumpy and most avatars cannot cross them
		return 100;
	}
	
	@Override
	public void setVeg(int newVeg) {
		this.vegetation = newVeg;
	}
	
	@Override
	public void setColor(Draw duDwin) { //Sets color to mountain colors
		duDwin.setPenColor(100, vegetation + 50, 100);
		
	}

}
