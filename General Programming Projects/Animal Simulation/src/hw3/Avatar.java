package hw3;

import edu.du.dudraw.Draw;

public abstract class Avatar implements Drawable { //abstract avatar class
	
	protected GridPoint location;
	protected TerrainMap tm;
	
	
	protected Avatar (TerrainMap tm, GridPoint location) { //constructor used for all avatars
		this.location = location;
		this.tm = tm;
	}
	
	// methods used for all avatars
	public abstract void processEvent(double mX, double mY);

	public abstract void draw(Draw duDwin);
	
	public abstract void move();
}
