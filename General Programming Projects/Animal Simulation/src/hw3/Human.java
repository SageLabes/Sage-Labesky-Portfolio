package hw3;

import java.util.ArrayList;
import java.util.List;

import edu.du.dudraw.Draw;

public class Human extends Avatar{
	
	private List<GridPoint> path = new ArrayList<GridPoint>();

	protected Human(TerrainMap tm, GridPoint location) {
		super(tm, location);
	}

	@Override
	public void draw(Draw duDwin) {
		duDwin.picture(location.getY()*35 + 17.5, duDwin.getYscale()[1] - location.getX()*35 + 17.5, "human.png", 30,30); //draws human picture
		
	}

	@Override
	public void move() {
		for(int i = 0; i < path.size() - 1; i++) { //Moves the human along the path
			if(path.get(i).equals(location)) {
				location = path.get(i+1);
				break;
			} else {
				continue;
			}
		}
		
	}

	@Override
	public void processEvent(double mX, double mY) { //human uses process event
		int mouseX = (int)mX;
		int mouseY = (int)mY;
		if(mouseX == 0) { //I had a bug where the human would get stuck with an error if it was on the edge of the screen so I made it unable to reach the edge
			mouseX += 1;
		} else if(mouseX == 29){
			mouseX -= 1;
		}
		if(mouseY == 0) { //same for y
			mouseY += 1;
		} else if(mouseY == 19) {
			mouseY -= 1;
		}
		//if(mouseY == )
		PathFinder pt = new PathFinder(tm); //uses path finder to create a list of steps the human must take
		path = new ArrayList<GridPoint>();
		path = pt.findPath(location, new GridPoint(20 - mouseY, mouseX));
		
	}

}
