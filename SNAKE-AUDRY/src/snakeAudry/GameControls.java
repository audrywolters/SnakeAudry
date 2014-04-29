package snakeAudry;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//import java.awt.*;

public class GameControls implements KeyListener {
	
	Snake snake;
	
	//construct
	GameControls(Snake s){
		this.snake = s;
	}
	
	
	public void keyPressed(KeyEvent ev) {
		//keyPressed events are for catching events like function keys, enter, arrow keys
		//We want to listen for arrow keys to move snake
		//Has to id if user pressed arrow key, and if so, send info to snake object

		//is game running? No? tell the game to draw grid, start, etc.
		
		//Get the component which generated this event
		//Hopefully, a DrawSnakeGamePanel object.
		//It would be a good idea to catch a ClassCastException here. //TODO add class cast exception
		
		DrawSnakeGamePanel panel = (DrawSnakeGamePanel)ev.getComponent();

		if (SnakeGame.getGameStage() == SnakeGame.BEFORE_GAME) {
			//Start the game
			SnakeGame.setGameStage(SnakeGame.DURING_GAME);  //set state to game on
			SnakeGame.newGame();  //set clock and clocktick
			panel.repaint(); 
			return;
		}
		
		
		if (SnakeGame.getGameStage() == SnakeGame.GAME_OVER) {
			snake.reset();
			Score.resetScore();
			
			//Need to start the timer and start the game again
			SnakeGame.newGame();  //reset timer
			SnakeGame.setGameStage(SnakeGame.DURING_GAME);  //set state to game on
			panel.repaint();
			return;
		}

		
		//respond to arrow key presses
		if (ev.getKeyCode() == KeyEvent.VK_DOWN) {
			snake.snakeDown();
		}
		if (ev.getKeyCode() == KeyEvent.VK_UP) {
			snake.snakeUp();
		}
		if (ev.getKeyCode() == KeyEvent.VK_LEFT) {
			snake.snakeLeft();
		}
		if (ev.getKeyCode() == KeyEvent.VK_RIGHT) {
			snake.snakeRight();
		}

	}


	@Override
	public void keyReleased(KeyEvent ev) {
		//We don't care about keyReleased events, but are required to implement this method anyway.		
	}


	@Override
	public void keyTyped(KeyEvent ev) {
		//keyTyped events are for user typing letters on the keyboard, anything that makes a character 

		
		char keyPressed = ev.getKeyChar();
		char q = 'q'; //quit
		char Q = 'Q';		
		char w = 'w'; //warp walls
		char W = 'W';
		char s = 's'; //speed
		char S = 'S';
		char m = 'm'; //mazes (wall)
		char M = 'M';
		char z = 'z'; //screen size
		char Z = 'Z';
		
		if(keyPressed == q || keyPressed == Q){
			System.exit(0);    //quit if user presses the q key.
		}
		
		if (keyPressed == w || keyPressed == W) {
			Snake.warpState();
		}
		
		if (keyPressed == s || keyPressed == S) {
			SnakeGame.speedState();
		}
		
		if (keyPressed == m || keyPressed == M) {
			Wall.wallState();
		}
		
		if (keyPressed == z || keyPressed == Z) {
			SnakeGame.sizeState();
		}

		
		
	}

}
