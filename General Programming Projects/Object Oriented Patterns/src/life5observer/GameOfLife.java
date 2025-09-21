package life5observer;

import java.util.ArrayList;

import edu.du.dudraw.DrawListener;

public class GameOfLife{
	// rows and cols for the game
    private int rows;
    private int cols;
    private Cell grid[][];
    ArrayList<LifeCommand> commands;
    ArrayList<LifeObserver> observers;
    
    
    public GameOfLife(int rows, int cols) { //constructor
    	//saves instance variables
    	this.rows = rows;
    	this.cols = cols;
    	observers = new ArrayList<LifeObserver>();
    	
    }
    
    private void setupGrid(Cell[][] g) { //This method either creates a new grid if there is none or changes cells based on what calls it
		// Setup the grid if empty
			g = new Cell[rows][cols];
	        for (int i = 0; i < rows; i++) {
	        	for (int j = 0; j < cols; j++) {
	        		g[i][j] = new Cell();
	        		
	            }
	        }
	        this.grid = g;
	        for (int i = 0; i < rows; i++) { //does this for every cell in the grid using i and j as coordinates
	        	for (int j = 0; j < cols; j++) {
	        		int x = 0; //x and y are declared to be the coordinates of neighboring cells
	                int y = i - 1;
	                if (y < 0) {
	                    y = rows - 1;
	                }
	                for (int rCt = 1; rCt <= 3; rCt++) { //each side has three neighbors so it goes through a 3^2 nested loop
	                    x = j - 1;
	                    if (x < 0) {
	                        x = cols - 1;
	                    }
	                    for (int cCt = 1; cCt <= 3; cCt++) { //Second part of the loop
	                        if (x != j || y != i) {
	                            grid[i][j].addNeighbor(grid[y][x]);; //adds the neighbors to the cell
	                        }
	                        x = (x + 1) % cols;
	                    }
	                    y = (y + 1) % rows;
	                }
	        		
	            }
	        }
	    
	}
    
    public void advance() {
    	//This code populates the cell neighbors list
		ArrayList<LifeCommand> commands = new ArrayList<LifeCommand>();
        int n = 0;
        for (int i = 0; i < rows; i++) { //traverses each element in grid again to update the new grid
            for (int j = 0; j < cols; j++) {
                n = grid[i][j].nbrAliveNeighbors(); //calls the cell function nbrAliveNeighbors() to determine alive neighbors
                if (grid[i][j].isAlive()) {
                    if (!(n == 2 || n == 3)) { //cells with 2 or 3 alive neighbors perform in a certain way
                    	commands.add(new DieCommand(grid[i][j]));
                    }
                } else {
                    if (n == 3) { //Cells with exactly 3 neighbors that arent alive perform a certain way
                    	commands.add(new LiveCommand(grid[i][j]));
                    }
                }
            }
        }
        for(LifeCommand c: commands) {
        	c.execute();
        }
        for(LifeObserver o: observers) {
        	o.update();
        }
    }
    
    public void attach(LifeObserver o) {
    	observers.add(o);
    	
    	this.setupGrid(grid);
    	
    	for(LifeObserver l : observers) {
    		l.update();
    	}
    }
    
    public void detach(LifeObserver o) {
    	observers.remove(o);
    }
    
    public int getRows() { //getter for rows
    	return rows;
    }
    
    public int getCols() { //getter for columns
    	return cols;
    }
    
    public Cell getCell(int row, int col) { //getter for cell at a specific row and column
    	return grid[row][col];
    }
}
