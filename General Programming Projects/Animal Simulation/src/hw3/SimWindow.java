package hw3;

import java.util.ArrayList;
import java.util.List;

import edu.du.dudraw.DUDraw;
import edu.du.dudraw.Draw;
import edu.du.dudraw.DrawListener;

public class SimWindow implements DrawListener{

	// Some static constants that everyone can use
	//   the represent the window size
	public final static int windowWidth = 1050;
	public final static int windowHeight = 700;

	protected List<Avatar> avatars = new ArrayList<Avatar>();
	private TerrainMap tm;
	private Draw duDwin;

	public SimWindow(TerrainMap tm) {
		// Setup the DuDraw window
		duDwin = new Draw("COMP2381 Animal Simulation"); // The OO version of DUDraw
		duDwin.setCanvasSize(SimWindow.windowWidth, SimWindow.windowHeight);
		duDwin.enableDoubleBuffering(); // Too slow otherwise -- need to use .show() later
		duDwin.addListener(this);

		// Set the scale of the window
		// Right now it is set to match the pixels
		duDwin.setXscale(0, windowWidth);
		duDwin.setYscale(0, windowHeight);

		this.tm = tm;
	}

	public void update() {
		duDwin.clear();
		tm.draw(duDwin);
		for(int j = 0; j < avatars.size(); j++) { //Loops through all avatars, moving them and drawing them
			avatars.get(j).draw(duDwin);
			avatars.get(j).move();
			
		}
		duDwin.show();  // used in double buffering
	}
	
	public void addAvatar(Avatar a) { //adds avatars to a list
		avatars.add(a);
	}

	public void runSimulation() {
		// This is the main game loop
		update(); // Initial positing

		while(true) {

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			update();
		}	
	}

	@Override
	public void keyPressed(int arg0) {
		
	}

	@Override
	public void keyReleased(int arg0) { // this method spawns the different avatars when you press a key
		if(duDwin.hasNextKeyTyped()) {
			char type = duDwin.nextKeyTyped();
			if(type == 'c' || type == 's') { // has to be devided into sections so that they can spawn in appropriate tiles
				try { //Try and accept to catch possible issues
					this.avatars.add(AvatarFactory.createAvatar(type, tm, new GridPoint(8,18))); //Cows and sheep spawn in grass
				} catch (BadAvatarException e) {
					e.printStackTrace();
				}
			} else if(type == 'a' || type == 't') { // Alligators and toucans spawn over water
				try {
					this.avatars.add(AvatarFactory.createAvatar(type, tm, new GridPoint(9,10)));
				} catch (BadAvatarException e) {
					e.printStackTrace();
				}
			} else if(type == 'h') { //Humans spawn on roads
				try { 
					this.avatars.add(AvatarFactory.createAvatar(type, tm, new GridPoint(3,10))); //Each of these calls avatar factory to make a new avatar
				} catch (BadAvatarException e) {
					e.printStackTrace();
				}
			} else { //Still calls this so that the error message for typing a letter that isn't accepted still appears
				try {
					this.avatars.add(AvatarFactory.createAvatar(type, tm, new GridPoint(3,10)));
				} catch (BadAvatarException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	public void keyTyped(char arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(double arg0, double arg1) { //Gets the mouse clicking location
		for(Avatar a : avatars) {
			if(a instanceof Human) { //looks through all avatars to see if they need to process events due to being human
				a.processEvent(Math.floor(duDwin.mouseX()/35), Math.floor(duDwin.mouseY()/35)); //tells humans to process events
			}
		}
		
	}

	@Override
	public void mouseDragged(double arg0, double arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(double arg0, double arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(double arg0, double arg1) {
		// TODO Auto-generated method stub
		
	}
}