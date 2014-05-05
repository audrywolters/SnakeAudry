/*
 * Basic functionality written by Clara James
 * Edited and enhanced by Audry Wolters
 */


package snakeAudry;

import java.util.Random;

public class Kibble {

	/** Identifies a random square to display a kibble
	 * Any square is ok, so long as it doesn't have any snake segments in it. 
	 * 
	 */

	private int blockX; //This is the square number (not pixel)
	private int blockY;  //This is the square number (not pixel)

	//construct
	public Kibble(Snake s, Wall w) {
		moveBlock(s);
		moveBlockForWall(w);
	}
	
	protected void moveBlock(Snake s){		
		Random rng = new Random();
		boolean blockInSnake = true;
		while (blockInSnake == true) {
			//Generate random block location
			blockX = rng.nextInt(SnakeGame.xSquares);  //find random within window
			blockY = rng.nextInt(SnakeGame.ySquares);
			blockInSnake = s.isSnakeSegment(blockX, blockY); //check where snake is
		}	
	}

	protected void moveBlockForWall (Wall w){		

		Random rng = new Random();
		boolean blockInKibble = true;
		while (blockInKibble == true) {
			//Generate random block location
			blockX = rng.nextInt(SnakeGame.xSquares);  //find random within window
			blockY = rng.nextInt(SnakeGame.ySquares);

			if (blockX == w.getBlockX() && blockY == w.getBlockY()) {
				blockInKibble = true;
			} else {
				blockInKibble = false;
			}
		}	
	}	



	public int getBlockX() {
		return blockX;
	}

	public int getBlockY() {
		return blockY;
	}

	public void setBlockX(int blockX) {
		this.blockX = blockX;
	}

	public void setBlockY(int blockY) {
		this.blockY = blockY;
	}
 
}

