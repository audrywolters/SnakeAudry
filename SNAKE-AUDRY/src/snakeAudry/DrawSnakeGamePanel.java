/*
 * Basic functionality written by Clara James
 * Edited and enhanced by Audry Wolters
 */


package snakeAudry;

import java.awt.*;
import java.util.LinkedList;

import javax.swing.JPanel;

/** This class responsible for displaying the graphics, so the snake, grid, kibble, 
 * instruction text and high score
 * 
 * @author Clara
 *
 */
@SuppressWarnings("serial")
public class DrawSnakeGamePanel extends JPanel {

	private static int gameStage = SnakeGame.BEFORE_GAME;  //use this to figure out what to paint
	private Snake snake;
	private Kibble kibble;
	private Wall wall;
	private Score score;

	//construct
	DrawSnakeGamePanel (Snake s, Kibble k, Wall wl, Score sc) {
		this.snake = s;
		this.kibble = k;
		this.wall = wl;
		this.score = sc;		
		//set the background color to black
		setBackground(Color.BLACK);

	}

	//a library class that makes sure window is that size
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(SnakeGame.xPixelMaxDimension, SnakeGame.yPixelMaxDimension);
	}


	private void displayInstructions(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawString("Welcome to SNAKE 2!", 125, 150);
		g.drawString("Press ANY key to begin!", 125, 200);	
		
		g.drawString("Press Q to quit the game", 125, 275);	
		g.drawString("Press W to change warp walls (on/off)", 125, 300);
		g.drawString("Press S to change speed (fast/slow)", 125, 325);
		g.drawString("Press M to change mazes (on/off)", 125, 350);
		g.drawString("Press Z to change screen size (big/small)", 125, 375);
	}


	//library class that creates the graphics
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);       

		/* Where are we at in the game? 4 phases.. 
		 * 1. Before game starts
		 * 2. During game
		 * 3. Game lost aka game over
		 * 4. or, game won
		 */

		gameStage = SnakeGame.getGameStage();

		switch (gameStage) {
		case 1: {
			displayInstructions(g);
			break;
		} 
		case 2 : {
			displayGame(g);
			break;
		}
		case 3: {
			displayGameOver(g);
			break;
		}
		case 4: {
			displayGameWon(g);
			break;
		}
		}       
	}


	//game win screen
	private void displayGameWon(Graphics g) {
		g.clearRect(100,100,350,350);
		g.drawString("YOU WON SNAKE!!!", 150, 150);
	}


	//game over screen
	private void displayGameOver(Graphics g) {
		g.setColor(Color.BLACK);

		//size of the screen
		g.clearRect(0,0,SnakeGame.xPixelMaxDimension,SnakeGame.yPixelMaxDimension); //clear the screen
		g.drawString("GAME OVER", 125, 100);  //string to print and where

		//scores
		String textScore = score.getStringScore();
		String textHighScore = score.getStringHighScore();
		String newHighScore = score.newHighScore();

		g.drawString("SCORE = " + textScore, 125, 125);

		g.drawString("HIGH SCORE = " + textHighScore, 125, 175);
		g.drawString(newHighScore, 125, 200);

		g.drawString("Press ANY key to play again", 125, 225);
		
		g.drawString("Press Q to quit the game", 125, 275);	
		g.drawString("Press W to change warp walls (on/off)", 125, 300);
		g.drawString("Press S to change speed (fast/slow)", 125, 325);
		g.drawString("Press M to change mazez (on/off)", 125, 350);
		g.drawString("Press Z to change screen size (big/small)", 125, 375);

	}


	private void displayGame(Graphics g) {
		//displayGameGrid(g);
		displaySnake(g);
		displayKibble(g);	
		displayWall(g);
	}


	public void displaySettings(Graphics g) {
		g.drawString("Wow", 150, 275);
	}


	private void displayKibble(Graphics g) {
		//Draw the kibble in white
		g.setColor(Color.WHITE);

		//get position of kibble, multiply it by the square size
		int x = kibble.getBlockX() * SnakeGame.squareSize;
		int y = kibble.getBlockY() * SnakeGame.squareSize;

		//fill it up! with above data
		g.fillRect(x, y, SnakeGame.squareSize, SnakeGame.squareSize);

	}


	private void displayWall(Graphics g) {
		//draw wall in black
		Color graige = new Color(109,102,95);
		g.setColor(graige);

		//get position of wall and multiply it by square size (to make as big as grid square)
		if (Wall.wallsOn) {
			int x = wall.getBlockX() * SnakeGame.squareSize;
			int y = wall.getBlockY() * SnakeGame.squareSize;
			int x2 = wall.getBlockX2() * SnakeGame.squareSize;
			int y2 = wall.getBlockY2() * SnakeGame.squareSize;

			g.fillRect(x, y, SnakeGame.squareSize, SnakeGame.squareSize);
			g.fillRect(x2, y2, SnakeGame.squareSize, SnakeGame.squareSize);
		}
		
		
		/* FANCY WALL**********
			for (Point p : wall.getAllWallPoints()) {
				g.fillRect((int) p.getX(), (int) p.getY(), SnakeGame.squareSize, SnakeGame.squareSize);
			}
		 
		LinkedList<Wall> = new LinkedList<Wall)();
		Wall[] allWalls = new Wall[snake.getKibbleCounter()];
		
		
		for (int i=0; i<snake.getKibbleCounter(); i++) {
		//(if kibbleCounter %5 == 0 - multiple of 5 - in here or wall or snakeGame?)
			//find position of wall point
			int x = wall.getBlockX() * SnakeGame.squareSize;
			int y = wall.getBlockY() * SnakeGame.squareSize;
			//make square of them
			g.fillRect(x, y, SnakeGame.squareSize, SnakeGame.squareSize);
		}

		//for debugging
		int testX = wall.getBlockX();
		int testY = wall.getBlockY();
		int testX2 = wall.getBlockX2();
		int testY2 = wall.getBlockY2();
		int testSquare = SnakeGame.squareSize;
		 */

	}


	private void displaySnake(Graphics g) {

		//Rainbow Snake code inspired by Clara James
		//colors of the rainbow
		Color red = new Color(211, 106, 106);
		Color red2 = new Color(214, 125, 105);
		Color org = new Color(222, 155, 111);
		Color org2 = new Color(219, 173, 117);
		Color yel = new Color(228, 191, 109);
		Color yel2 = new Color(229, 224, 126);
		Color grn = new Color(194, 196, 124);
		Color grn2 = new Color(175, 204, 147);
		Color blu = new Color(149, 191, 167);
		Color blu2 = new Color(131, 174, 178);
		Color ind = new Color(114, 149, 175);
		Color ind2 = new Color(109, 122, 158);
		Color vio = new Color(153, 120, 150);
		Color vio2 = new Color(175, 117, 134);

		//array of colors
		Color[] rainbow = { red, red2, org, org2, yel, yel2, grn, grn2, blu, blu2, ind, ind2, vio, vio2 };	
		//snake segment coords
		LinkedList<Point> coords = snake.segmentsToDraw();	
		//length of rainbow colors
		int totalColors = rainbow.length;
		//start at 0 color (red)
		int startColor = 0;

		//for each coord, draw in specific color
		for (Point p : coords) {
			g.setColor(rainbow[startColor]);
			startColor = (startColor + 1) % totalColors;
			g.fillRect((int) p.getX(), (int) p.getY(), SnakeGame.squareSize, SnakeGame.squareSize);
		}

		//turned off for rainbow snake
		/*
		//Draw head in white
		g.setColor(Color.WHITE);
		Point head = coordinates.pop();  //get the first set of coords (the head) make gray
		g.fillRect((int)head.getX(), (int)head.getY(), SnakeGame.squareSize, SnakeGame.squareSize);

		//Draw rest of snake in red
		g.setColor(Color.RED);
		for (Point p : coordinates) {  //for each coord, make it black
			g.fillRect((int)p.getX(), (int)p.getY(), SnakeGame.squareSize, SnakeGame.squareSize);
		}
		 */
	}




	/* NO GRID FOR NEW UI
	private void displayGameGrid(Graphics g) {
		int maxX = SnakeGame.xPixelMaxDimension;
		int maxY= SnakeGame.yPixelMaxDimension;
		int squareSize = SnakeGame.squareSize;

		//clear the screen
		g.clearRect(0, 0, maxX, maxY);

		g.setColor(Color.BLACK);

		/*
		//Draw grid - horizontal lines
		for (int y=0; y <= maxY ; y+= squareSize){			
			g.drawLine(0, y, maxX, y);
		}
		//Draw grid - vertical lines
		for (int x=0; x <= maxX ; x+= squareSize){			
			g.drawLine(x, 0, x, maxY);
		}
	}
	 */
}

