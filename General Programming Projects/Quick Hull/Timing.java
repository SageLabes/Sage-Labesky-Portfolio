package HW4;

import java.awt.Point;

public class Timing {
	public void time() {
		bruteForce b = new bruteForce(); //initializes a brute force class
		QuickHull q = new QuickHull();
		
		System.out.println("Quickhull Algorithm:");
		for(int n = 0; n < 8; n++) {
			int numTrials = 5; //trial number
			
			Point[] test = new Point[500000*(n+1)]; //creates a list of 100 random points from 0 to 100
			
			double elapsedTime = 0.0; //elapsed time variable to be able to time things in the loop and not the whole loop
			for(int j=0; j < numTrials; j++)
			{
				for(int i = 0; i < test.length; i++) {
					test[i] = new Point((int)(Math.random()*100), (int)(Math.random()*100));
				}
				double startTime = System.currentTimeMillis(); //times only the sorting algorithm
				q.doQuickHull(test);
				double endTime = System.currentTimeMillis();
				elapsedTime+=(endTime-startTime);
			}
			
			
			System.out.print(test.length + " ");
			System.out.println((elapsedTime)/(double)numTrials);
		}
		System.out.println("Brute force Algorithm:");
		for(int n = 0; n < 8; n++) {
			int numTrials = 5; //trial number
			
			Point[] test = new Point[100*(n+1)]; //creates a list of points to test on
			
			double elapsedTime = 0.0; //elapsed time variable to be able to time things in the loop and not the whole loop
			for(int j=0; j < numTrials; j++)
			{
				for(int i = 0; i < test.length; i++) {
					test[i] = new Point((int)(Math.random()*100), (int)(Math.random()*100));
				}
				double startTime = System.currentTimeMillis(); //times only the algorithm
				b.doBruteForce(test);
				double endTime = System.currentTimeMillis();
				elapsedTime+=(endTime-startTime);
			}
			
			
			System.out.print(test.length + " ");
			System.out.println((elapsedTime)/(double)numTrials);
		}
	}
}
