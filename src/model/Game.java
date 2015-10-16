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
	
	public Game(int gridSize, GameMap map, boolean[][] visited, Point hunterStartingPosition) {
		this.gridSize = gridSize;
		this.map = map;
		this.visited = visited;
		this.newPlayerPosition = hunterStartingPosition;
		
		visited[hunterStartingPosition.x][hunterStartingPosition.y] = true;
		gameOver = false;
	}
	
	public int getGridSize() {
		return gridSize;
	}
	
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
			
			checkRules();
		}
		
	}
	
	public void resetGame(int gridSize, GameMap map, boolean[][] visited, Point hunterStartingPosition) {
		this.gridSize = gridSize;
		this.map = map;
		this.visited = visited;
		this.newPlayerPosition = hunterStartingPosition;
		
		visited[hunterStartingPosition.x][hunterStartingPosition.y] = true;
		gameOver = false;	
		
		GameMessage gm = new GameMessage(Obstacle.Empty, this.gameOver, reason);
		setChanged();
		notifyObservers(gm);
		
	}

	public Point getPlayerPosition() {
		return newPlayerPosition;
	}
	
	public boolean shootArrow(Direction direction) {
		
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
			
		}
		
		if(win == false) {
			reason = GameOverReason.ArrowHitHunter;
		}
		gameOver = true;
		
		checkRules();
		return win;
	}		
	
	public boolean[][] getVisited() {
		return visited;
	}
	
	public Obstacle whatIsHere(Point p) {
		return map.whatIsHere(p);
	}
	
	private void visitAll() {
		for(int x = 0; x < gridSize; x++) {
			for(int y = 0; y < gridSize; y++) {
				visited[x][y] = true;
			}
		}
	}
	
	private void checkRules() {
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
		}
		
		if(gameOver == true) {
			visitAll();
		}
		
		GameMessage gm = new GameMessage(obstacle, this.gameOver, reason);
		setChanged();
		notifyObservers(gm);
	}
	
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
