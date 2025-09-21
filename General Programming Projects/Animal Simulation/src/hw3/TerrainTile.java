package hw3;

import edu.du.dudraw.Draw;

// This is an abstract class
public abstract class TerrainTile implements Drawable {
	
	// Every tile has a location
	private GridPoint location;
	
	protected TerrainTile (GridPoint location) {
		this.location = location;
	}

	public int getVeg() { //default return of 0 for all attributes
		return 0;
	}
	
	public int getWet() {
		return 0;
	}
	
	public int getBumpy() {
		return 0;
	}
	
	public void setVeg(int newVeg) { //some classes have to be able to set veg
		
	}
	
	// Draws the tile on the given Window
	public void draw(Draw duDwin) {
		this.setColor(duDwin); //Each tile calls its own setColor
		//Lots of math to get locations and sizes correctly
		duDwin.filledRectangle(location.getY()*35 + 17.5, duDwin.getYscale()[1] - location.getX()*35 + 17.5, 17.2, 17.2); 
		// the abstract method.

	}
	
	// Part of the draw template for concrete tiles to set the color
	public abstract void setColor(Draw duDwin);
	
}

