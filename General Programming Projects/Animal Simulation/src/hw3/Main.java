package hw3;

public class Main {

	public static void main(String[] args) {
		// Load in the map
		TerrainMap tm = null;
		try {
			tm = new TerrainMap("map1.txt");
		} catch(TerrainFileException e) {
			e.printStackTrace();
		}
		
		// Make the display panel
		SimWindow window = new SimWindow(tm);
		
		//ADDING AVATARS REQUIRES HITTING KEYS AFTER RUNNING, HIT a,t,s,c,or h TO SUMMON CRITTERS
		
		
		// Start the simulation
		window.runSimulation();
	}

}
