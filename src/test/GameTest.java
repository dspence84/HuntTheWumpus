/*Daniel Spence
 * Joshua Adams
 * 
 * Tests for Game Class
 * coverage = 100%
 */

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

	/**test for player movement and wrap around functionality
	 * 
	 */
	@Test
	public void testMovePlayer() {
		//set up game
		Point hunterPosition = new Point(0, 0);
		GameMapFactory mf = new GameMapFactory(new Obstacle[GRID_SIZE][GRID_SIZE], new Random(), GRID_SIZE, lbPits,
				ubPits);
		mf.fillEmpty();
		boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];
		
		//default
		Game game = new Game(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		//move east
		game.movePlayer(Direction.East);
		assertFalse(game.getPlayerPosition().equals(hunterPosition));
		hunterPosition = new Point(1, 0);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		//move west
		game.movePlayer(Direction.West);
		assertFalse(game.getPlayerPosition().equals(hunterPosition));
		hunterPosition = new Point(0, 0);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		//move north
		game.movePlayer(Direction.North);
		assertFalse(game.getPlayerPosition().equals(hunterPosition));
		hunterPosition = new Point(0, 1);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		
		//move south twice to also check y axis wrap
		game.movePlayer(Direction.South);
		game.movePlayer(Direction.South);
		assertFalse(game.getPlayerPosition().equals(hunterPosition));
		hunterPosition = new Point(0, 9);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		//test default switch for no move
		game.movePlayer(Direction.None);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		//test get last position
		hunterPosition = new Point(0, 9);
		game.movePlayer(Direction.North);
		assertTrue(game.getPlayerPositionLast().equals(hunterPosition));
		//move west again to check x axis wrap
		game.movePlayer(Direction.West);
		assertTrue(game.getPlayerPosition().equals(new Point(9,0)));
		
 
	}

	/**
	 * Test for game reset function and that gameover acts correctly
	 **/
	@Test
	public void testResetGameGameOver() {
		//set up
		Point hunterPosition = new Point(0, 0);
		GameMapFactory mf = new GameMapFactory(new Obstacle[GRID_SIZE][GRID_SIZE], new Random(), GRID_SIZE, lbPits,
				ubPits);
		mf.fillEmpty();
		boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];
		Game game = new Game(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		
		//shoot arrow east then reset game
		game.shootArrow(Direction.East);
		game.resetGame(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		
		//move player eastthen reset game
		game = new Game(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		game.movePlayer(Direction.East);
		game.resetGame(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		
		//player fall into pit
		game = new Game(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		Obstacle[][] obstacle = new Obstacle[2][2];
		obstacle[1][1] = Obstacle.Pit;
		GameMap map = new GameMap(obstacle);
		game = new Game(2, map, new boolean[2][2], new Point(1, 0));
		assertFalse(game.getGameOver() == true);
		game.movePlayer(Direction.North);
		assertTrue(game.getGameOver() == true);
		
		//test that player cannot move when game is over
		game.resetGame(2, map, new boolean[2][2], new Point(1, 0));
		assertFalse(game.getGameOver() == true);
		game.movePlayer(Direction.North);
		assertFalse(game.getPlayerPosition().equals(new Point(0,0)));
		

	}

	/**
	 * Test that shooting arrow ends game by eitherhitting wumpus
	 * or hitting hunter
	 */
	@Test
	public void testShootArrowand() {
		//set up
		GameMapFactory mf = new GameMapFactory(new Obstacle[2][2], new Random(), 2, 0, 0);
		mf.fillEmpty();
		Obstacle[][] obstacle = new Obstacle[2][2];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				obstacle[i][j] = Obstacle.Empty;
			}
		}
		obstacle[1][1] = Obstacle.Wumpus;
		GameMap map = new GameMap(obstacle);
		Game game = new Game(2, map, new boolean[2][2], new Point(0, 0));
		//arrow kills hunter fired north
		game.shootArrow(Direction.North);
		assertTrue(game.getGameOver() == true);
		//arrow shoots wumpus fired north
		game.resetGame(2, map, new boolean[2][2], new Point(0, 0));
		game.movePlayer(Direction.East);
		assertTrue(game.shootArrow(Direction.North));
		assertTrue(game.getGameOver() == true);
		//player eaten cannot shoot arrow test
		game.resetGame(2, map, new boolean[2][2], new Point(0, 0));
		game.movePlayer(Direction.East);
		game.movePlayer(Direction.North);
		assertTrue(game.getGameOver() == true);
		assertFalse(game.shootArrow(Direction.North));
		//hunter shoots west and kills himself
		game.resetGame(2, map, new boolean[2][2], new Point(0, 0));
		game.movePlayer(Direction.East);
		assertFalse(game.shootArrow(Direction.West));
		//hunter kills wumpus with wrap around shooting west
		game.resetGame(2, map, new boolean[2][2], new Point(0, 0));
		game.movePlayer(Direction.North);
		assertTrue(game.shootArrow(Direction.West));
		//kills wumpus shooting east
		game.resetGame(2, map, new boolean[2][2], new Point(0, 0));
		game.movePlayer(Direction.North);
		assertTrue(game.shootArrow(Direction.East));
		//hunter kills self shooting south wrap around
		game = new Game(2, map, new boolean[2][2], new Point(0, 0));
		game.shootArrow(Direction.South);
		assertTrue(game.getGameOver() == true);
		//no shoot
		game = new Game(2, map, new boolean[2][2], new Point(0, 0));
		game.shootArrow(Direction.None);
		assertTrue(game.getGameOver() == true);

	}

	/**
	 * test what is in the space and returning get visited
	 * also tests get grid size
	 */
	@Test
	public void testGetVisitedWhatIsHere() {
		//set up
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
		obstacle[0][0] = Obstacle.Blood;
		obstacle[0][1] = Obstacle.Pit;
		obstacle[1][0] = Obstacle.Slime;
		GameMap map = new GameMap(obstacle);
		//test wumpus blood slime and pit
		game = new Game(2, map, new boolean[2][2], new Point(0, 0));
		assertTrue(game.whatIsHere(new Point(1,1)) == Obstacle.Wumpus);
		assertTrue(game.whatIsHere(new Point(0,0)) == Obstacle.Blood);
		assertTrue(game.whatIsHere(new Point(1,0)) == Obstacle.Slime);
		assertTrue(game.whatIsHere(new Point(0,1)) == Obstacle.Pit);
		//test Hunter Goop and EMpty
		obstacle[1][1] = Obstacle.Hunter;
		obstacle[0][0] = Obstacle.Goop;
		obstacle[1][0] = Obstacle.Empty;
		assertTrue(game.whatIsHere(new Point(1,1)) == Obstacle.Hunter);
		assertTrue(game.whatIsHere(new Point(0,0)) == Obstacle.Goop);
		assertTrue(game.whatIsHere(new Point(1,0)) == Obstacle.Empty);
		//test grid size
		assertEquals(2, game.getGridSize());
		
		
	}

}
