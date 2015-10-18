/*Daniel Spence
 * Joshua Adams
 * 
 * runs the game and all of its rules
 */

package model;
 
import java.awt.Point;
import java.util.Observable;

public class Game extends Observable {
	
	private Point newPlayerPosition;
	private Point oldPlayerPosition;
	private int gridSize;
	private GameMap map;
	private boolean[][] visited;
	private boolean gameOver = true;
	private GameOverReason reason = GameOverReason.Default;
	private boolean arrowShot;
	
	/**
	 * initialize a new game
	 * @param gridSize
	 * @param map
	 * @param visited
	 * @param hunterStartingPosition
	 */
	public Game(int gridSize, GameMap map, boolean[][] visited, Point hunterStartingPosition) {
		this.gridSize = gridSize;
		this.map = map;
		this.visited = visited;
		this.newPlayerPosition = hunterStartingPosition;
		this.oldPlayerPosition = hunterStartingPosition;
		
		visited[hunterStartingPosition.x][hunterStartingPosition.y] = true;
		gameOver = false;
	}
	
	/**
	 * returns the gridSize of the current game
	 * @return
	 */
	public int getGridSize() {
		return gridSize;
	}
	
	/**
	 * moves a player and determines any consequences of such
	 * @param direction
	 */
	public void movePlayer(Direction direction) {
		if(gameOver == false) {
			
			oldPlayerPosition = newPlayerPosition;
			
			int plusX;
			int plusY;
			
			switch(direction) {
			case North:
				plusX = 0;
				plusY = 1;
				break;
			case South:
				plusX = 0;
				plusY = -1;
				break;
			case East:
				plusX = 1;
				plusY = 0;
				break;
			case West:
				plusX = -1;
				plusY = 0;
				break;
			default:
				plusX = 0;
				plusY = 0;
			}		
					
			System.out.println("moved");
			
			newPlayerPosition = calculateWrap(oldPlayerPosition, plusX, plusY);
			
			visited[newPlayerPosition.x][newPlayerPosition.y] = true;
			
			checkRules(direction);
		}
		
	}
	
	/**
	 * resets the game with a new grid size and map.
	 * @param gridSize
	 * @param map
	 * @param visited
	 * @param hunterStartingPosition
	 */
	public void resetGame(int gridSize, GameMap map, boolean[][] visited, Point hunterStartingPosition) {
		this.gridSize = gridSize;
		this.map = map;
		this.visited = visited;
		this.newPlayerPosition = hunterStartingPosition;
		this.arrowShot = false;
		
		visited[hunterStartingPosition.x][hunterStartingPosition.y] = true;
		gameOver = false;
		
		GameMessage gm = new GameMessage(Obstacle.Empty, this.gameOver, GameOverReason.Reset, Direction.None);
		setChanged();
		notifyObservers(gm);
		
	}
	
	/**
	 * gets the new player position if a move was performed
	 * @return
	 */
	public Point getPlayerPosition() {
		return newPlayerPosition;
	}
	
	/**
	 * gets the old player position if a move was performed
	 * @return
	 */
	public Point getPlayerPositionLast() {
		return oldPlayerPosition;
	}
	
	/**
	 * shoots an arrow and determines a hit or miss, consequence is game over
	 * @param direction
	 * @return
	 */
	public boolean shootArrow(Direction direction) {
		setArrowShot(true);
		if(gameOver) {
			return false;
		}
		
		int playerX = newPlayerPosition.x;
		int playerY = newPlayerPosition.y;
		boolean win = false;
		
		switch(direction) {
		case East:
		case West:
			for(int x = 0; x < gridSize; x++) {
				if(map.whatIsHere(new Point(x, playerY)) == Obstacle.Wumpus) {
					reason = GameOverReason.ArrowHitWumpus;
					win = true;
					break;
				}				
			}
		break;
		case North:
		case South:
			for(int y = 0; y < gridSize; y++) {
				if(map.whatIsHere(new Point(playerX, y)) == Obstacle.Wumpus) {
					reason = GameOverReason.ArrowHitWumpus;
					win = true;
					break;
				}
			}
		break;
		default:
			break;
			
		}
		
		if(win == false) {
			reason = GameOverReason.ArrowHitHunter;
		}
		gameOver = true;
		
		checkRules(direction);
		return win;
	}		
	
	/**
	 * get the squares that have been visited by the player
	 * @return
	 */
	public boolean[][] getVisited() {
		return visited;
	}
	
	/**
	 * wraps to the game map to find out what is at a grid square
	 * @param p
	 * @return
	 */
	public Obstacle whatIsHere(Point p) {
		return map.whatIsHere(p);
	}
	
	/**
	 * simulates all squares visited in the case of a game over
	 */
	private void visitAll() {
		for(int x = 0; x < gridSize; x++) {
			for(int y = 0; y < gridSize; y++) {
				visited[x][y] = true;
			}
		}
	}
	
	/**
	 * checks the consequences of a move or an arrow shoot
	 * @param direction
	 */
	private void checkRules(Direction direction) {
		Obstacle obstacle = map.whatIsHere(newPlayerPosition);
		
		switch(obstacle) {
		case Wumpus:
			reason = GameOverReason.Wumpus;
			gameOver = true;
			break;
		case Pit:
			reason = GameOverReason.Pit;
			gameOver = true;
			break;		
		default:
			break;
		}
		
		if(gameOver == true) {
			visitAll();
		}
		
		GameMessage gm = new GameMessage(obstacle, this.gameOver, reason, direction);
		setChanged();
		notifyObservers(gm);
	}
	
	/**
	 * calculates the proper wrapped position when a player moves
	 * @param point
	 * @param plusX
	 * @param plusY
	 * @return
	 */
	private Point calculateWrap(Point point, int plusX, int plusY) {
		Point p = new Point(point.x, point.y);

		p.x = (p.x + plusX) % (gridSize);
		p.y = (p.y + plusY) % (gridSize);

		if (p.x < 0) {
			p.x = p.x + gridSize;
		}
		if (p.y < 0) {
			p.y = p.y + gridSize;
		}

		return p;
	}

	/**
	 * gets the game over condition
	 * @return
	 */
	public boolean getGameOver() {
		// TODO Auto-generated method stub
		return gameOver;
	}

	public boolean arrowShot() {
		return arrowShot;
	}

	public void setArrowShot(boolean arrowShot) {
		this.arrowShot = arrowShot;
	}
}


// control everything --
// move player
// send messages
// control the arrow


// Game
// Point newPlayerPostion
// Point oldPlayerPosition
// 
//
// movePlayer(Direction)
// shootArrow(Direction)
// private checkRules()  sends messages to observers
