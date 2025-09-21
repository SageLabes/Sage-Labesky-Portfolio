package HW4;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import edu.du.dudraw.DUDraw;

public class QuickHull {
	public Set<Point> doQuickHull(Point[] p){
		double maxX = 0; //variables to find the first and last x value
		double minX = 101;
		Point smallestX = null;
		Point largestX = null;
		
		Set<Point> result = new HashSet<Point>(); //List to store the hull
		
		for(Point c: p) { //loop to set the max and min variables
			if(c.x > maxX) {
				maxX = c.x;
				largestX = c;
			} else if(c.x < minX) {
				minX = c.x;
				smallestX = c;
			}
		}
		
		result.add(smallestX); //add the points with smallest and largest x since they are always on the hull
		result.add(largestX);
		
		//creates an upper and lower array to split the points
		ArrayList<Point> upper = new ArrayList<Point>();
		ArrayList<Point> lower = new ArrayList<Point>();
		
		for(Point v : p) { //divides upper and lower arrays
			int detAtoV = valueBasedOnLineDistance(smallestX, largestX, v);
			if(detAtoV > 0) {
				upper.add(v);
			} else if(detAtoV < 0) {
				lower.add(v);
			}
		}
		// calls the recursive quickhull method to find the full hull on top and bottom
		QuickHull(upper, smallestX, largestX, result, 0);
		QuickHull(lower, largestX, smallestX, result, 0);

		return result;
		
	}
	
	private void QuickHull(ArrayList<Point> p, Point a, Point b, Set<Point> result, int stop) {
		if(p.size()>0) { //base case, recursion lasts while the amount of points is > 0, does nothing when it stops
			Point furthestPoint = new Point(1,1);
			int maxVal = 0;
			
			for(Point c : p) { //this loop finds the furthest point from the passed line ab
				if(Math.abs(valueBasedOnLineDistance(a,b,c)) > maxVal) {
					maxVal = Math.abs(valueBasedOnLineDistance(a,b,c));
					furthestPoint = c;
				}
			}
			
			result.add(furthestPoint); //furthest point will be on the hull so it is added
			
			//creates two arrays to divide left and right of the lines ac and bc
			ArrayList<Point> left = new ArrayList<Point>();
			ArrayList<Point> right = new ArrayList<Point>();
		
			for(Point s: p) { //divides the points into left and right, many of them are left out since they cannot be on the hull
				int detAtoS = valueBasedOnLineDistance(a, furthestPoint, s);
				int detBtoS = valueBasedOnLineDistance(furthestPoint, b, s);
				
				if(detAtoS > 0) {
					right.add(s);
				} else if(detBtoS > 0) {
					left.add(s);
				}
			}
			
			if(stop != 1) { //debug tool, stop allows the recursion to stop after one call to each left and right, only changable here
				QuickHull(right, a, furthestPoint, result, 0);
				QuickHull(left, furthestPoint, b, result, 0);
			}
		}
	}
	
	private int valueBasedOnLineDistance(Point a, Point b, Point p) { //computes cross product
		int v1x = b.x - a.x;
		int v1y = b.y - a.y;
		int v2x = p.x - a.x;
		int v2y = p.y - a.y;
		return v1x * v2y - v1y * v2x; //removed the abs() from the suedocode so I could use it to look above/below and left/right of a line
	}
}