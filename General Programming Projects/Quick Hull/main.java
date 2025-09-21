package HW4;
import java.awt.Point;
import java.util.*;
import edu.du.dudraw.DUDraw;
import edu.du.dudraw.Draw;

public class main {

	public static void main(String[] args) {
		bruteForce b = new bruteForce(); //initializes a brute force class
		QuickHull q = new QuickHull();
		Timing t = new Timing();
		
		
		//Draw window = new Draw(); //creates the dudraw window and sets the sizes
		//window.setCanvasSize(500,500);
		//window.setXscale(0, 100);
		//window.setYscale(0, 100);
		
		DUDraw.setCanvasSize(500,500);
		DUDraw.setXscale(0,100);
		DUDraw.setYscale(0,100);
		
		Point[] points = new Point[50]; //creates a list of 50 random points from 0 to 100
		for(int i = 0; i < points.length; i++) {
			points[i] = new Point((int)(Math.random()*98+1), (int)(Math.random()*98+1));
		}
		
		// traverses the points and plots a point where each one is
		for(Point p : points) {
			DUDraw.setPenColor(DUDraw.BLACK);
			DUDraw.filledCircle(p.getX(), p.getY(), 0.5);
		}
		
		Set<Point> hull1 = b.doBruteForce(points); //calls brute force
		Set<Point> hull2 = q.doQuickHull(points);
		
		//CHANGE hull2 TO hull1 TO SEE BRUTE FORCE WORKING
		for(Point v: hull2) { // plots orange points where the points of the convex hull are
			DUDraw.setPenColor(DUDraw.ORANGE);
			DUDraw.filledCircle(v.x, v.y, 0.5);
		}
		
		//rather than having two main methods, I have timing as its own method
		t.time();
		
	}

}
