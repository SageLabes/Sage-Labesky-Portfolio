package life1original;

// The main program used to invoke the game of life system.
 
public class Main {

    public static void main(String[] args) {
        // Create an instance of the game with a 20x20 life grid
    	//   shown in a 500x500 window
        GameOfLifeApp life = new GameOfLifeApp("The Game of Life", 20, 20, 500, 500);
    }
}

