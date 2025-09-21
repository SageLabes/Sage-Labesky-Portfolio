package hw3;

import java.util.ArrayList;
import java.util.List;

/*
 * Class to represent an x and y grid location.
 * 
 * TODO: Should have standard methods: equals, toString, hashCode, compareTo
 */
public class GridPoint implements Comparable<GridPoint>{
	
	private int xPos; //Necessary variables to hold x and y
	private int yPos;
	
	public GridPoint(int x, int y) { //constructor to take an x and y
		xPos = x;
		yPos = y;
	}
	
	public String toString() { //Returns a string of the x and y. I chose to implement this as an ordered pair
		return("(" + xPos + "," + yPos + ")");
	}
	
	public int compareTo(GridPoint that) {	//CompareTo method
		int result = this.xPos - that.xPos;
		
		if(result == 0) {
			result = this.yPos - that.yPos; //does subtraction, positive number is eventually returned if this is bigger, -1 if that is, and 0 if neither are
		}
		
		return result;
	}
	
	public boolean equals(Object obj) {
		//Method to check if two GridPoints are equal
		if(obj instanceof GridPoint) {
			GridPoint that = (GridPoint)obj;
			if(this.xPos == that.xPos) {
				if(this.yPos == that.yPos) { //Returns true if both x and y are equal
					return true;
				}
			}
		}
		return false;
	}
	
	public List<GridPoint> getNeighbors(int levels){ // Get neighbors finds the neighboring tiles at a distance given by levels
		List<GridPoint> neighbors = new ArrayList<GridPoint>(); //list of neighbors to return
		for(int y = levels*-1; y <= levels; y++) { // searches around at each combination of the gridpoint by the levels
			for(int x = levels*-1; x <= levels; x++) {
				if(x==0 && y==0) { // does nothing if it is the current tile
					
				} else {
					neighbors.add(new GridPoint(this.getX()+x, this.getY()+y)); //adds all the other neighbors
				}
			}
		}
		return neighbors;
	}
	
	public int getX() { //Getter for x position
		return xPos;
	}
	
	public int getY() { //Getter for y position
		return yPos;
	}
	@Override
	public int hashCode() { //Returns a hash code for the object
		return this.toString().hashCode();
	}
}
