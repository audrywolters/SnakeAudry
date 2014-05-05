/*
 * Basic functionality written by Clara James
 * Edited and enhanced by Audry Wolters
 */


package snakeAudry;

import java.util.TimerTask;

public class GameClock extends TimerTask {

	Snake snake;
	Kibble kibble;
	Wall wall;
	Score score;
	DrawSnakeGamePanel gamePanel;
		
	//construct
	public GameClock(Snake snake, Kibble kibble, Wall wall, Score score, DrawSnakeGamePanel gamePanel){
		this.snake = snake;
		this.kibble = kibble;
		this.wall = wall;
		this.score = score;
		this.gamePanel = gamePanel;
	}
	
	@Override
	//what happens in a clock tick
	public void run() {
			
		int stage = SnakeGame.getGameStage();

		switch (stage) {
			case SnakeGame.BEFORE_GAME: {
				//don't do anything, waiting for user to press a key to start
				break;
			}
			case SnakeGame.DURING_GAME: {
				//
				snake.moveSnake(wall);
				if (snake.didEatKibble(kibble) == true) {		
					//tell kibble to update
					//make sure not in snake or wall
					kibble.moveBlock(snake);
					kibble.moveBlockForWall(wall);

					Score.increaseScore();
				}
				break;
			}
			case SnakeGame.GAME_OVER: {
				this.cancel();		//Stop the Timer	
				break;	
			}
			case SnakeGame.GAME_WON: {
				this.cancel();   //stop timer
				break;
			}
			
		
		}
				
		gamePanel.repaint();		//In every circumstance, must update screen
		
	}
}
