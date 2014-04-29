package snakeAudry;

import java.awt.Point;
import java.util.*;

public class Snake {

	final int DIRECTION_UP = 0;
	final int DIRECTION_DOWN = 1;
	final int DIRECTION_LEFT = 2;
	final int DIRECTION_RIGHT = 3;  //These are completely arbitrary numbers. 

	private boolean hitEdge = false;
	private boolean ateTail = false;
	private boolean hitWall1 = false;
	private boolean hitWall2 = false;


	private int snakeSquares[][];  //represents all of the squares on the screen
	//NOT pixels!
	//A 0 means there is no part of the snake in this square
	//A non-zero number means part of the snake is in the square
	//The head of the snake is 1, rest of segments are numbered in order

	private int currentHeading;  //Direction snake is going in, or direction user is telling snake to go
	private int lastHeading;    //Last confirmed movement of snake. See moveSnake method

	private int snakeSize;   //size of snake - how many segments?

	private int growthIncrement = 1; //how many squares the snake grows after it eats a kibble
	private int justAteMustGrowThisMuch = 0; //
	private int kibbleCounter = 0;

	private int maxX, maxY, squareSize;  //

	private int snakeHeadX, snakeHeadY; //store coordinates of head - first segment

	//setting for warp walls
	private static boolean warpOn = false;


	///CONSTRUCTOR///
	public Snake(int maxX, int maxY, int squareSize) {
		this.maxX = maxX;
		this.maxY = maxY;
		this.squareSize = squareSize;
		//Create and fill snakeSquares with 0s 
		setSnakeSquares(new int[maxX][maxY]);
		fillSnakeSquaresWithZeros();
		createStartSnake();

	}


	//TODO Why fill with zeros?
	private void fillSnakeSquaresWithZeros() {
		for (int x = 0; x < this.maxX; x++){
			for (int y = 0 ; y < this.maxY ; y++) {
				getSnakeSquares()[x][y] = 0;
			}
		}
	}


	protected void createStartSnake() {
		//snake starts as 3 horizontal squares in the center of the screen, moving left
		int screenXCenter = (int) maxX/2;  //Cast just in case we have an odd number
		int screenYCenter = (int) maxY/2;  //Cast just in case we have an odd number

		getSnakeSquares()[screenXCenter][screenYCenter] = 1;
		getSnakeSquares()[screenXCenter+1][screenYCenter] = 2;
		getSnakeSquares()[screenXCenter+2][screenYCenter] = 3;

		snakeHeadX = screenXCenter;
		snakeHeadY = screenYCenter;

		snakeSize = 3;

		currentHeading = DIRECTION_LEFT;
		lastHeading = DIRECTION_LEFT;

		justAteMustGrowThisMuch = 0;
	}



	///search for snake
	public LinkedList<Point> segmentsToDraw() {
		//Return a list of the actual x and y coordinates of the top left of each snake segment
		//Useful for the Panel class to draw the snake
		LinkedList<Point> segmentCoordinates = new LinkedList<Point>();  //point is a library segment that stores (x,y) coords


		//for the size of the snake...
		for (int segment = 1 ; segment <= snakeSize ; segment++ ) {
			//search array for each segment number
			for (int x = 0 ; x < maxX ; x++) {  //for each x coord...

				for (int y = 0 ; y < maxY ; y++) {  //for each y coord...

					//if the x,y (counter) == current coord (i.e. a snake piece)
					//a snake piece because it is holds int 1,2,3 or etc in each coord

					if (getSnakeSquares()[x][y] == segment){  
						//make a Point for this segment's coordinates and add to list
						Point p = new Point(x * squareSize , y * squareSize); 
						segmentCoordinates.add(p);  

					}
				}

			}
		}

		return segmentCoordinates;

	}


	//don't eat your neck!
	public void snakeUp() {
		if (currentHeading == DIRECTION_UP || currentHeading == DIRECTION_DOWN) { return; }
		currentHeading = DIRECTION_UP;
	}	
	public void snakeDown() {
		if (currentHeading == DIRECTION_DOWN || currentHeading == DIRECTION_UP) { return; }
		currentHeading = DIRECTION_DOWN;
	}	
	public void snakeLeft() {
		if (currentHeading == DIRECTION_LEFT || currentHeading == DIRECTION_RIGHT) { return; }
		currentHeading = DIRECTION_LEFT;
	}
	public void snakeRight() {
		if (currentHeading == DIRECTION_RIGHT || currentHeading == DIRECTION_LEFT) { return; }
		currentHeading = DIRECTION_RIGHT;
	}


	//	public void	eatKibble(){
	//		//record how much snake needs to grow after eating food
	//		justAteMustGrowThisMuch += growthIncrement;
	//	}


	protected void moveSnake(Wall wall){
		//Called every clock tick

		//Must check that the direction snake is being sent in is not contrary to current heading
		//So if current heading is down, and snake is being sent up, then should ignore.
		//Without this code, if the snake is heading up, and the user presses left then down quickly, 
		//the snake will back into itself.
		if (currentHeading == DIRECTION_DOWN && lastHeading == DIRECTION_UP) {
			currentHeading = DIRECTION_UP; //keep going the same way
		}
		if (currentHeading == DIRECTION_UP && lastHeading == DIRECTION_DOWN) {
			currentHeading = DIRECTION_DOWN; //keep going the same way
		}
		if (currentHeading == DIRECTION_LEFT && lastHeading == DIRECTION_RIGHT) {
			currentHeading = DIRECTION_RIGHT; //keep going the same way
		}
		if (currentHeading == DIRECTION_RIGHT && lastHeading == DIRECTION_LEFT) {
			currentHeading = DIRECTION_LEFT; //keep going the same way
		}

		//Did you hit the wall, snake? 
		//Or eat your tail? Don't move. 

		if (hitEdge == true || ateTail == true) {
			SnakeGame.setGameStage(SnakeGame.GAME_OVER);
			return;
		}


		///WHERE HEAD WILL GO///
		if (currentHeading == DIRECTION_UP) {		
			//Subtract 1 from Y coordinate so head is one square up
			snakeHeadY-- ;
		}// else if (currentHeading == D)

		if (currentHeading == DIRECTION_DOWN) {		
			//Add 1 to Y coordinate so head is 1 square down
			snakeHeadY++ ;
		}		
		if (currentHeading == DIRECTION_LEFT) {		
			//Subtract 1 from X coordinate so head is 1 square to the left
			snakeHeadX -- ;
		}
		if (currentHeading == DIRECTION_RIGHT) {		
			//Add 1 to X coordinate so head is 1 square to the right
			snakeHeadX ++ ;
		}




		///GROWING THE BODY///

		//the default of this creates another snake segment
		//have to set it to NOT grow a piece if Didn't eat

		//Use snakeSquares array, and current heading, to move snake

		//Put a 1 in new snake head square
		//increase all other snake segments by 1
		//set tail to 0 if snake did not just eat
		//Otherwise leave tail as is until snake has grown the correct amount 

		//Find the head of the snake - snakeHeadX and snakeHeadY

		//Increase all snake segments by 1
		//All non-zero elements of array represent a snake segment

		//for the width of the window
		for (int x = 0 ; x < maxX ; x++) {
			//for the height of the window
			for (int y = 0 ; y < maxY ; y++){
				//if snake is NOT 0 (or not a blank square)
				if (getSnakeSquares()[x][y] != 0) {
					//increment all segements by 1
					getSnakeSquares()[x][y]++;
				}
			}

		}



		///WARP WALL///
		if (warpOn == false) {
			//if snakeHead coords are past the edges of window: game over
			if (snakeHeadX >= maxX || snakeHeadX < 0 || snakeHeadY >= maxY || snakeHeadY < 0 ) {
				hitEdge = true;	
				SnakeGame.setGameStage(SnakeGame.GAME_OVER);
				return;
			}

		}

		if (warpOn == true) {
			///move the head if it gets to edge
			//Inspired by Samson and Pedro
			//if head at leftest square, put head rightest square
			if (snakeHeadX < 0) {
				snakeHeadX = maxX - 1;
			} 
			//right to left
			if (snakeHeadX == maxX) {
				snakeHeadX = 0;
			}
			//top to bottom
			if (snakeHeadY < 0) {
				snakeHeadY = maxY - 1;
			}
			//bottom to top
			if (snakeHeadY == maxY) {
				snakeHeadY = 0;
			}
		}




		///HITTING MAZE WALL///
		//first wall
		if (Wall.wallsOn) {  //only matters if the maze walls are turned on
			if (snakeHeadX == wall.getBlockX() && snakeHeadY == wall.getBlockY()) {
				hitWall1 = true;
				SnakeGame.setGameStage(SnakeGame.GAME_OVER);
				return;
			}

			//second wall
			if (snakeHeadX == wall.getBlockX2() && snakeHeadY == wall.getBlockY2()) {
				hitWall2 = true;
				SnakeGame.setGameStage(SnakeGame.GAME_OVER);
				return;
			}
		}
		
		
		/* FANCY WALL ***************
		for (Point p : wall.getAllWallPoints()) {
			int wallX = (int) p.getX();
			int wallY = (int) p.getY();
			if (snakeHeadX == wallX && snakeHeadY == wallY) {
				hitWall1 = true;
				SnakeGame.setGameStage(SnakeGame.GAME_OVER);
				return;
			}
		}
		 */



		///EATING SELF///
		//head segment is not 1 at this point because it has already moved!
		if (getSnakeSquares()[snakeHeadX][snakeHeadY] != 0) {
			ateTail = true;
			SnakeGame.setGameStage(SnakeGame.GAME_OVER);
			return;
		}
		//Otherwise, game is still on. Add new head
		getSnakeSquares()[snakeHeadX][snakeHeadY] = 1; 




		///EATING KIBBLE///
		//If snake did not just eat, then remove tail segment
		//to keep snake the same length.
		//find highest number, which should now be the same as snakeSize+1, and set to 0	
		if (justAteMustGrowThisMuch == 0) {
			for (int x = 0 ; x < maxX ; x++) {
				for (int y = 0 ; y < maxY ; y++){
					if (getSnakeSquares()[x][y] == snakeSize+1) {
						getSnakeSquares()[x][y] = 0;

					}
				}
			}
		}
		else {
			//Snake has just eaten. leave tail as is.  Decrease justAte... variable by 1.
			justAteMustGrowThisMuch -- ;
			snakeSize ++;
		}

		lastHeading = currentHeading; //Update last confirmed heading

	}



	public boolean didEatKibble(Kibble kibble) {
		//Is this kibble in the snake? It should be in the same square as the snake's head
		if (kibble.getBlockX() == snakeHeadX && kibble.getBlockY() == snakeHeadY){
			justAteMustGrowThisMuch += growthIncrement;
			//kibbleCounter is used for wall creating
			kibbleCounter++;
			return true;
		}
		return false;
	}



	public boolean isSnakeSegment(int kibbleX, int kibbleY) {
		if (getSnakeSquares()[kibbleX][kibbleY] == 0) {
			return false;
		}
		return true;
	}



	//check to see if snake points are equal to wall points
	public boolean isSnakeWallSegment(int blockX, int blockY, int blockX2, int blockY2) {
		if (snakeHeadX == blockX || snakeHeadY == blockY) {
			return true;
		}		
		return false;
	}



	public String toString(){
		String textsnake = "";
		//This looks the wrong way around. Actually need to do it this way or snake is drawn flipped 90 degrees. 
		for (int y = 0 ; y < maxY ; y++) {
			for (int x = 0 ; x < maxX ; x++){
				textsnake = textsnake + getSnakeSquares()[x][y];
			}
			textsnake += "\n";
		}
		return textsnake;
	}

	public boolean wonGame() {
		//If all of the squares have snake segments in, the snake has eaten so much kibble 
		//that it has filled the screen. Win!
		for (int x = 0 ; x < maxX ; x++) {
			for (int y = 0 ; y < maxY ; y++){
				if (getSnakeSquares()[x][y] == 0) {
					//there is still empty space on the screen, so haven't won
					return false;
				}
			}
		}
		//But if we get here, the snake has filled the screen. win!
		SnakeGame.setGameStage(SnakeGame.GAME_WON);

		return true;
	}

	public void reset() {
		hitEdge = false;
		ateTail = false;
		fillSnakeSquaresWithZeros();
		createStartSnake();

	}

	public boolean isGameOver() {
		if (hitEdge == true || ateTail == true){
			SnakeGame.setGameStage(SnakeGame.GAME_OVER);
			return true;

		}
		return false;
	}

	//if warp on - turn off, it off - turn on
	public static void warpState() {
		if (warpOn == true) {
			warpOn = false;
		} else if (warpOn == false) {
			warpOn = true;
		}

	}

	public int getSnakeSize() {
		return snakeSize;
	}

	public int getMaxX() {
		return maxX;
	}


	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}


	public int getMaxY() {
		return maxY;
	}


	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	protected boolean didHitEdge(){
		return hitEdge;

	}

	protected boolean didEatTail(){
		return ateTail;
	}

	public boolean isHitWall() {
		return hitWall1;
	}

	public void setHitWall1(boolean hitWall) {
		this.hitWall1 = hitWall;
	}

	public boolean isHitWall2() {
		return hitWall2;
	}

	public void setHitWall2(boolean hitWall) {
		this.hitWall2 = hitWall;
	}

	public int getKibbleCounter() {
		return kibbleCounter;
	}


	public int[][] getSnakeSquares() {
		return snakeSquares;
	}


	public void setSnakeSquares(int snakeSquares[][]) {
		this.snakeSquares = snakeSquares;
	}



}


