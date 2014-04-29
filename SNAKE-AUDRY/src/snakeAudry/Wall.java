package snakeAudry;

import java.util.Random;

public class Wall  {
	
	//dont' know if it's ok to instantiate a variable up here...  for FANCY WALL
	//private LinkedList<Point> allWallPoints = new LinkedList<Point>();
	private int blockX; //This is the square number (not pixel)
	private int blockY;
	private int blockX2; //This is the square number (not pixel)
	private int blockY2;

	public static boolean wallsOn = true;
	


	public Wall(Snake s){
		//Kibble needs to know where the snake is, so it does not create a kibble in the snake
		//Pick a random location for kibble, check if it is in the snake
		//If in snake, try again
		
		makeWall(s);
	}
	
	//if user presses s, the speed will change to the other state
	public static void wallState() {	
		if (wallsOn == true) {
			wallsOn = false;
		} else {
			wallsOn = true;
		}
	}
	
	
	protected void makeWall(Snake s){		
		Random rng = new Random();
		
		boolean blockInSnake = true;
		while (blockInSnake == true && wallsOn == true) {
			//Generate random block location
			
			/* FANCY WALLS
			while (s.getKibbleCounter%5 == 0)
			for (int i=0; i<(int)s.getKibbleCounter()/5; i++) {
				blockX = rng.nextInt(SnakeGame.xSquares);  //find random within window
				blockY = rng.nextInt(SnakeGame.ySquares);
				blockInSnake = s.isSnakeWallSegment(blockX, blockY);
				Point p = new Point(blockX, blockY); 
				getAllWallPoints().add(p);
			}
			*/		
					
			blockX = rng.nextInt(SnakeGame.xSquares);  //find random within window
			blockY = rng.nextInt(SnakeGame.ySquares);
			blockX2 = rng.nextInt(SnakeGame.xSquares);  //find random within window
			blockY2 = rng.nextInt(SnakeGame.ySquares);
			blockInSnake = s.isSnakeWallSegment(blockX, blockY, blockX2, blockY2); //check where snake is
					
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
	
	
	public int getBlockX2() {
		return blockX2;
	}

	public int getBlockY2() {
		return blockY2;
	}	

	public static boolean isWallsOn() {
		return wallsOn;
	}

	public static void setWallsOn(boolean wallsOn) {
		Wall.wallsOn = wallsOn;
	}
	
	/* for FANCY WALL
	public LinkedList<Point> getAllWallPoints() {
		return allWallPoints;
	}

	public void setAllWallPoints(LinkedList<Point> allWallPoints) {
		this.allWallPoints = allWallPoints;
	}
	*/
}
