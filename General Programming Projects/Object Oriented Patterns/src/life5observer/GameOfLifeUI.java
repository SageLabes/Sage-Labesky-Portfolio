package life5observer;

import java.awt.Color;

import edu.du.dudraw.Draw;

public class GameOfLifeUI extends LifeObserver{
	// width and height in pixels
	private int width;
    private int height;
	private Draw window;
	private GameOfLife game;
	
	public GameOfLifeUI(GameOfLife game, String title, int width, int height) {
		super(game);
    	// Save the instance variables
		this.width = width;
		this.height = height;
		this.game = game;
		
		// Setup the DuDraw board
        window = new Draw(title);
        window.setCanvasSize(width, height);
        window.setXscale(0, width);
		window.setYscale(0, height);
       
		// Add the mouse/key listeners
        window.addListener(this);
        
        game.attach(this);
	}

	private void drawGrid() {
        
    	window.setPenColor(Color.black);
 		
        int cellWidth = width / game.getCols();
        int cellHeight = height / game.getRows();
     
        for (int i = 0; i <= game.getRows(); i++) {
        	window.line(0, i * cellHeight, this.width, i * cellHeight);
        }
        
        for (int i = 0; i <= game.getCols(); i++) {
        	window.line(i * cellWidth, 0, i * cellWidth, this.height);
        }
    }
	
	private void drawLives() {
    	int cellWidth = width / game.getCols();
        int cellHeight = height / game.getRows();
        
    	window.setPenColor(Color.red);
        for (int i = 0; i < game.getRows(); i++) {
        	for (int j = 0; j < game.getCols(); j++) {
        		if (game.getCell(i, j).isAlive()) {
        			// This is the center and half width/height
        			window.filledRectangle((j * cellWidth)+(cellWidth/2), (i * cellHeight)+(cellHeight/2), cellWidth/2, cellHeight/2);
                }
            }
        }
    }
	
	@Override
	public void update() {
		// Redraw the entire board
		window.clear(Color.white);  // Clear in white
	 	drawGrid();
	 	drawLives();
	}

	@Override
	public void keyPressed(int key) {
		if (key==32) {
			game.advance();
		}
		
	}

	@Override
	public void keyReleased(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(char arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(double arg0, double arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(double arg0, double arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(double x, double y) {
		// This is the toggle of grid locations
		int cellWidth = width / game.getCols();
        int cellHeight = height / game.getRows();
        
        int cellLocRow = (int)(y / cellHeight);
        int cellLocCol = (int)(x / cellWidth);
        
        if(game.getCell(cellLocRow, cellLocCol).isAlive()) {
        	game.getCell(cellLocRow, cellLocCol).die();
        } else {
        	game.getCell(cellLocRow, cellLocCol).live();
        }

		update();   
	}

	@Override
	public void mouseReleased(double arg0, double arg1) {
		// TODO Auto-generated method stub
		
	}

}
