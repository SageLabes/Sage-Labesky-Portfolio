package hw3;

public class TerrainTileFactory {
	public static TerrainTile newTerrainTile(String titleStr, GridPoint loc) throws InvalidTerrainTypeException{ //factory to create terrain tiles
		if(titleStr.toLowerCase().equals("r")) { //checks each letter to see which should be added
			return new Road(loc);
		} else if(titleStr.toLowerCase().equals("m")){
			return new Mountain(loc);
		} else if(titleStr.equals("g")) {
			return new Grass(loc);
		} else if(titleStr.equals("w")){
			return new Water(loc);
		} else { //If the passed character doesn't match one of the four letters it throws the terrain type exception
			throw new InvalidTerrainTypeException("Terrain type provided is not excepted");
		}
	}
}
