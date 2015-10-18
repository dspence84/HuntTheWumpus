package test;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.Random;

import org.junit.Test;

import model.Direction;
import model.Game;
import model.GameMap;
import model.GameMapFactory;
import model.Obstacle;

public class GameTest {

	private int ubPits = 8;
	private int lbPits = 8;
	private final int GRID_SIZE = 10;

	@Test
	public void testMovePlayer() {
		Point hunterPosition = new Point(0, 0);
		GameMapFactory mf = new GameMapFactory(new Obstacle[GRID_SIZE][GRID_SIZE], new Random(), GRID_SIZE, lbPits,
				ubPits);
		mf.fillEmpty();
		Point p = mf.calculateWrap(new Point(0, 0), -1, -1);
		boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];
		Game game = new Game(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		game.movePlayer(Direction.East);
		assertFalse(game.getPlayerPosition().equals(hunterPosition));
		hunterPosition = new Point(1, 0);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		game.movePlayer(Direction.West);
		assertFalse(game.getPlayerPosition().equals(hunterPosition));
		hunterPosition = new Point(0, 0);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		game.movePlayer(Direction.North);
		assertFalse(game.getPlayerPosition().equals(hunterPosition));
		hunterPosition = new Point(0, 1);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		game.movePlayer(Direction.South);
		game.movePlayer(Direction.South);
		assertFalse(game.getPlayerPosition().equals(hunterPosition));
		hunterPosition = new Point(0, 9);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		game.movePlayer(Direction.None);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		hunterPosition = new Point(0, 9);
		game.movePlayer(Direction.North);
		assertTrue(game.getPlayerPositionLast().equals(hunterPosition));
		game.movePlayer(Direction.West);
		assertTrue(game.getPlayerPosition().equals(new Point(9,0)));
		

	}

	@Test
	public void testResetGameGameOver() {
		Point hunterPosition = new Point(0, 0);
		GameMapFactory mf = new GameMapFactory(new Obstacle[GRID_SIZE][GRID_SIZE], new Random(), GRID_SIZE, lbPits,
				ubPits);
		mf.fillEmpty();
		boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];
		Game game = new Game(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		game.shootArrow(Direction.East);
		game.resetGame(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		game = new Game(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		game.movePlayer(Direction.East);
		game.resetGame(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		game = new Game(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		Obstacle[][] obstacle = new Obstacle[2][2];
		obstacle[1][1] = Obstacle.Pit;
		GameMap map = new GameMap(obstacle);
		game = new Game(2, map, new boolean[2][2], new Point(1, 0));
		assertFalse(game.getGameOver() == true);
		game.movePlayer(Direction.North);
		assertTrue(game.getGameOver() == true);
		game.resetGame(2, map, new boolean[2][2], new Point(1, 0));
		assertFalse(game.getGameOver() == true);
		game.movePlayer(Direction.North);
		assertFalse(game.getPlayerPosition().equals(new Point(0,0)));
		

	}

	@Test
	public void testShootArrowand() {
		GameMapFactory mf = new GameMapFactory(new Obstacle[2][2], new Random(), 2, 0, 0);
		mf.fillEmpty();
		boolean[][] visited = new boolean[2][2]; 
		Obstacle[][] obstacle = new Obstacle[2][2];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				obstacle[i][j] = Obstacle.Empty;
			}
		}
		obstacle[1][1] = Obstacle.Wumpus;
		GameMap map = new GameMap(obstacle);
		Game game = new Game(2, map, new boolean[2][2], new Point(0, 0));
		game.shootArrow(Direction.North);
		assertTrue(game.getGameOver() == true);
		game.resetGame(2, map, new boolean[2][2], new Point(0, 0));
		game.movePlayer(Direction.East);
		assertTrue(game.shootArrow(Direction.North));
		assertTrue(game.getGameOver() == true);
		game.resetGame(2, map, new boolean[2][2], new Point(0, 0));
		game.movePlayer(Direction.East);
		game.movePlayer(Direction.North);
		assertTrue(game.getGameOver() == true);
		assertFalse(game.shootArrow(Direction.North));
		game.resetGame(2, map, new boolean[2][2], new Point(0, 0));
		game.movePlayer(Direction.East);
		assertFalse(game.shootArrow(Direction.West));
		game.resetGame(2, map, new boolean[2][2], new Point(0, 0));
		game.movePlayer(Direction.North);
		assertTrue(game.shootArrow(Direction.West));
		game.resetGame(2, map, new boolean[2][2], new Point(0, 0));
		game.movePlayer(Direction.North);
		assertTrue(game.shootArrow(Direction.East));
		game = new Game(2, map, new boolean[2][2], new Point(0, 0));
		game.shootArrow(Direction.South);
		assertTrue(game.getGameOver() == true);
		game = new Game(2, map, new boolean[2][2], new Point(0, 0));
		game.shootArrow(Direction.None);
		assertTrue(game.getGameOver() == true);

	}

	@Test
	public void testGetVisitedWhatIsHere() {
		Point hunterPosition = new Point(0, 0);
		GameMapFactory mf = new GameMapFactory(new Obstacle[GRID_SIZE][GRID_SIZE], new Random(), GRID_SIZE, lbPits,
				ubPits);
		mf.fillEmpty();
		boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];
		Game game = new Game(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		for(int i = 0; i < GRID_SIZE; i++){
			for(int j = 0; j < GRID_SIZE; j++){
				assertTrue(visited[i][j] == game.getVisited()[i][j]);
			}
		}
		visited = new boolean[2][2];
		Obstacle[][] obstacle = new Obstacle[2][2];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				obstacle[i][j] = Obstacle.Empty;
			}
		}
		obstacle[1][1] = Obstacle.Wumpus;
		GameMap map = new GameMap(obstacle);
		game = new Game(2, map, new boolean[2][2], new Point(0, 0));
		assertTrue(game.whatIsHere(new Point(1,1)) == Obstacle.Wumpus);
		assertEquals(2, game.getGridSize());
		
		
	}

}
