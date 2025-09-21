package HW4;
import java.util.*;
import java.awt.Point;
import java.util.ArrayList;

public class bruteForce {
	public Set<Point> doBruteForce(Point[] p){
		Set<Point> ret = new HashSet<Point>(); //creates a set to return the items in the convex hull
		for(int i = 0; i < p.length; i++) {
			for(int j = 1; j < p.length; j++) { //nested loop to compare lines
				if(i != j) {
					int countPos = 0; // variables to keep track of how many points are above or below the line
					int countNeg = 0;
					for(int x = 2; x < p.length; x++) {
						if(x != i && x != j) {
							int det = (p[j].x - p[i].x)*(p[x].y-p[i].y)-((p[j].y-p[i].y)*(p[x].x-p[i].x)); //checks if a third point is above or below with determinant
							if(det > 0) {
								countPos+=1;
							}
							if(det < 0) {
							countNeg += 1;
							}
						}
					}
					if(countPos == 0 || countNeg == 0) { //if all points are either below or above, i and j are on the hull and are added to the set
						ret.add(p[i]);
						ret.add(p[j]);
					}
				}
			}
		}
		return ret;
		
	}
}
