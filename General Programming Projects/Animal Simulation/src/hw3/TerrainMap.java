package hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import edu.du.dudraw.Draw;

class TerrainFileException extends Exception{ //Parent class for terrain file exceptions
	public TerrainFileException(String message) {
		super(message);
	}
}

class InvalidTerrainTypeException extends TerrainFileException{ //Specific exception for terrain types that are not allowed
	public InvalidTerrainTypeException(String message) {
		super(message);
	}
	
}

class MalformedTerrainFileException extends TerrainFileException{ //Specific exception for terrain files that are not set up correctly
	public MalformedTerrainFileException(String message) {
		super(message);
	}
	
}
// The TerrainMap represents a 2D grid of Tiles

public class TerrainMap implements Drawable {

	private Map<GridPoint, TerrainTile> theTiles = new HashMap<GridPoint, TerrainTile>();
	
	// public static constants set in the constructor.
	public static int gridWidth;
	public static int gridHeight;

	// Constructor to read from file
	public TerrainMap(String filename) throws TerrainFileException{
		
		try { //Reading the file in a try in order to catch file name errors
			File terrain = new File(filename);
			Scanner s = new Scanner(terrain); //Using scanner to read the file
			int row = 0; //Keeps track of row for x values of locations
			while (s.hasNextLine()) { //Traverses each file line
				String[] temp = s.nextLine().split(" "); //Turns each file line into an array
				if(row == 0) { //Since the first line of the file is the width and height row 0 is handled differently
					try { //try to catch errors in the numbers given for width and height
						gridWidth = Integer.valueOf(temp[0]);
						gridHeight = Integer.valueOf(temp[1]);
					} catch(NumberFormatException e) {
						throw new MalformedTerrainFileException("Valid size of file not presented");
					}
				} else { //for all rows except the first one, this code activates
					if(temp.length != gridWidth) { //if the line is longer than the width the file is invalid
						throw new MalformedTerrainFileException("Line(s) in file not valid size");
					}
					for(int i = 0; i < gridWidth; i++ ) { //Creates the tile objects depending on what letter is shown
						theTiles.put(new GridPoint(row, i), TerrainTileFactory.newTerrainTile(temp[i], new GridPoint(row, i)));
						
					}
				}
				row++;
				
			}
			s.close(); //file is closed
		} catch (FileNotFoundException e) { // file exceptions are caught
			e.printStackTrace();
		}
		// TODO: make sure you set gridWidth and gridHeight static data members when you 
		// read the map.
	}
	
	public void draw(Draw duDwin) {
		for(Map.Entry<GridPoint, TerrainTile> x : theTiles.entrySet()) {
			x.getValue().draw(duDwin);
		}
	}
	
	
	//Methods to get information important to the various avatars such as vegetation and tiles
	public int getVeg(GridPoint g) {
		return theTiles.get(g).getVeg();
	}
	
	public int getWet(GridPoint g) {
		return theTiles.get(g).getWet();
	}
	
	public int getBumpy(GridPoint g) {
		return theTiles.get(g).getBumpy();
	}
	
	public TerrainTile getTile(GridPoint g) {
		return theTiles.get(g);
	}
	
	public String toString() {
		return theTiles.toString();
	}

}
