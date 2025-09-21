package hw3;

import edu.du.dudraw.Draw;

public class Sheep extends FarmAnimal{

	protected Sheep(TerrainMap tm, GridPoint location) {
		super(tm, location);
	}

	@Override
	public void draw(Draw duDwin) {
		duDwin.picture(location.getY()*35 + 17.5, duDwin.getYscale()[1] - location.getX()*35 + 17.5, "sheep.png", 30,30); //sets sheep image
		
	}
	
	public GridPoint getLocation() {
		return this.location;
	}

	@Override
	public void processEvent(double mX, double mY) {
		// TODO Auto-generated method stub
		
	}
	
	protected void eat() {
		if(this.tm.getTile(location).getVeg() > 1) {
			this.tm.getTile(location).setVeg(this.tm.getTile(location).getVeg()-1); //sheeps unique attribute is that it eats vegetation
		}
	}

}
