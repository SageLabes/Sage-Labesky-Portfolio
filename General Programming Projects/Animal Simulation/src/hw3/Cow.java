package hw3;

import edu.du.dudraw.Draw;

public class Cow extends FarmAnimal{

	protected Cow(TerrainMap tm, GridPoint location) {
		super(tm, location);
	}

	@Override
	public void draw(Draw duDwin) {
		duDwin.picture(location.getY()*35 + 17.5, duDwin.getYscale()[1] - location.getX()*35 + 17.5, "moobloom.png", 30,30); //sets cow image
		
	}

	@Override
	public void processEvent(double mX, double mY) {
		// TODO Auto-generated method stub
		
	}
	
	
	protected void eat() {
		if(this.tm.getTile(location).getVeg() < 100) {
			this.tm.getTile(location).setVeg(this.tm.getTile(location).getVeg()+1); //Cow's unique attribuite is that it replenishes grass, dont let the name eat fool you!
		}
		
		
	}
	

}