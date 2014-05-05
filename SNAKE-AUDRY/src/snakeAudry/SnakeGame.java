/*
 * Basic functionality written by Clara James
 * Edited and enhanced by Audry Wolters
 */

package snakeAudry;

import java.util.Timer;
import javax.swing.*;


public class SnakeGame {
	
	/////GLOBALS/////
	
	//GUI SQUARES
	public static int xPixelMaxDimension = 500;  //Pixels in window. 501 to have 50-pixel squares plus 1 to draw a border on last square
	public static int yPixelMaxDimension = 500;
	public final static int squareSize = 25;  //pixels in each square
	public static int xSquares;  //how many squares will exist horizontally
	public static int ySquares;	 //how many squares vertically (decided in initialize game by dividing maxDimension by squareSize)
	//GUI stuff
	static JFrame snakeFrame;  //this is a library function that just makes a GUI window
	static DrawSnakeGamePanel snakePanel;  //the View Class!

	//GAME OBJECTS
	protected static Snake snake;
	protected static Kibble kibble;
	protected static Wall wall;
	protected static Score score;

	//GAME STATE
	//instead of the values so you are clear what you are setting. Easy to forget what number is Game over vs. game won
	//Using constant names instead makes it easier to keep it straight. Refer to these variables 
	//using statements such as SnakeGame.GAME_OVER 
	static final int BEFORE_GAME = 1;
	static final int DURING_GAME = 2;
	static final int GAME_OVER = 3;
	static final int GAME_WON = 4;   //The values are not important. The important thing is to use the constants 
	//initial state is before_game
	private static int gameStage = BEFORE_GAME;  //use this to figure out what should be happening. 
	//Other classes like Snake and DrawSnakeGamePanel will need to query this, and change it's value

	//SETTINGS
	protected static boolean speedFast = true;
	protected static boolean sizeBig = false;
	
	//GAME SPEED
	protected static long clockInterval = 100; //default speed is fast
	//Every time the clock ticks, the snake moves
	//This is the time between clock ticks, in milliseconds
	//1000 milliseconds = 1 second.

	
	//Framework for this class adapted from the Java Swing Tutorial, FrameDemo and Custom Painting Demo. You should find them useful too.
	//http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/FrameDemoProject/src/components/FrameDemo.java
	//http://docs.oracle.com/javase/tutorial/uiswing/painting/step2.html

	
	/////MAIN/////
	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initializeGame();
				createAndShowGUI();
			}
		});
	}
	
	
	//SET UP GAME
	private static void initializeGame() {
		//set up score, snake and first kibble, wall
		xSquares = xPixelMaxDimension / squareSize;  //these are global variables
		ySquares = yPixelMaxDimension / squareSize;

		snake = new Snake(xSquares, ySquares, squareSize);  //the snake
		wall = new Wall(snake);
		kibble = new Kibble(snake, wall); 
		score = new Score();
		gameStage = BEFORE_GAME;  
		
	}	
	
	
	//Create and set up the window.
	private static void createAndShowGUI() {
		snakeFrame = new JFrame();
		snakeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //how to close the window (library)

		snakeFrame.setSize(xPixelMaxDimension, yPixelMaxDimension);  //size
		snakeFrame.setUndecorated(true); //hide title bar
		snakeFrame.setVisible(true);
		snakeFrame.setResizable(false);

		snakePanel = new DrawSnakeGamePanel(snake, kibble, wall, score);  //what the GUI looks like, send it the game objects
		snakePanel.setFocusable(true);
		snakePanel.requestFocusInWindow(); //required to give this component the focus so it can generate KeyEvents

		snakeFrame.add(snakePanel);  //send the jFrame the GUI attributes
		snakePanel.addKeyListener(new GameControls(snake));  //the listener for key presses

		setGameStage(BEFORE_GAME);

		snakeFrame.setVisible(true);
	}

	
	//set or reset the game
	protected static void newGame() {
		Timer timer = new Timer();
		//things that happen within a clock tick
		GameClock clockTick = new GameClock(snake, kibble, wall, score, snakePanel);  
		//arguments are snake: task to complete, 0: delay before a task,  clockInterval: time inbetween the tasks
		timer.scheduleAtFixedRate(clockTick, clockInterval , clockInterval);   
	}
	
	
	//change speed
	public static void speedState() {	
		if (speedFast == true) {  //if fast
			clockInterval = 500;  //make slow
			//update to rest of game
			Timer timer = new Timer();
			GameClock clockTick = new GameClock(snake, kibble, wall, score, snakePanel);
			timer.scheduleAtFixedRate(clockTick, clockInterval , clockInterval);
			speedFast = false;

		} else if (speedFast == false) {  //if slow
			clockInterval = 100;  //make fast
			//update to rest of game
			Timer timer = new Timer();
			GameClock clockTick = new GameClock(snake, kibble, wall, score, snakePanel);
			timer.scheduleAtFixedRate(clockTick, clockInterval , clockInterval);
			speedFast = true;

		}
	}
	
	
	//change size of screen
	public static void sizeState() {
		if (sizeBig == true) { 		
			//if big
			xPixelMaxDimension = 500; 	//make small
			yPixelMaxDimension = 500;
			//need to tell rest of game that the variable has changed
			snakeFrame.setSize(xPixelMaxDimension, yPixelMaxDimension);
			snake.setMaxX(xPixelMaxDimension / squareSize);
			snake.setMaxY(yPixelMaxDimension / squareSize);
			snake.setSnakeSquares(new int[xPixelMaxDimension / squareSize][yPixelMaxDimension / squareSize]);
			xSquares = xPixelMaxDimension / squareSize;  
			ySquares = yPixelMaxDimension / squareSize;
			sizeBig = false;  //set to Not big

			
		} else if (sizeBig == false) {  ///if small
			xPixelMaxDimension = 750;	//make big
			yPixelMaxDimension = 750;
			//need to tell rest of game that the variable has changed
			snakeFrame.setSize(xPixelMaxDimension, yPixelMaxDimension);
			snake.setMaxX(xPixelMaxDimension / squareSize);
			snake.setMaxY(yPixelMaxDimension / squareSize);
			snake.setSnakeSquares(new int[xPixelMaxDimension / squareSize][yPixelMaxDimension / squareSize]);
			xSquares = xPixelMaxDimension / squareSize;  
			ySquares = yPixelMaxDimension / squareSize;
			sizeBig = true;		//set to big

		}
		
	}
	
	
	//get the stage
	public static int getGameStage() {
		return gameStage;
	}
	
	//what to do when game ends
	public static boolean gameEnded() {
		if (gameStage == GAME_OVER || gameStage == GAME_WON) {
			return true;
		}
		return false;
	}

	//set the stage
	public static void setGameStage(int gameStage) {
		SnakeGame.gameStage = gameStage;
	}

	
}
