package life6Visitor;

// The main program used to invoke the game of life system.
 
public class Main {

    public static void main(String[] args) {
        // Create an instance of the game with a 20x20 life grid
    	//   shown in a 500x500 window
        GameOfLife life = new GameOfLife(20, 20);
        //initiates the UI seperately
        GameOfLifeUI gameUI = new GameOfLifeUI(life, "The Game of Life", 500, 500);
    }
}

