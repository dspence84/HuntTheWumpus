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
import model.GameMessage;
import model.GameOverReason;
import model.Obstacle;

public class GameTest {

	private int ubPits = 8;
	private int lbPits = 8;
	private final int GRID_SIZE = 10;

	/**
	 * test for player movement and wrap around functionality
	 * 
	 */
	@Test
	public void testMovePlayer() {
		// set up game
		Point hunterPosition = new Point(0, 0);
		GameMapFactory mf = new GameMapFactory(new Obstacle[GRID_SIZE][GRID_SIZE], new Random(), GRID_SIZE, lbPits,
				ubPits);
		mf.fillEmpty();
		boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];

		// default
		Game game = new Game(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		assertEquals(false, game.getGameOver());
		// move east
		game.movePlayer(Direction.East);
		assertFalse(game.getPlayerPosition().equals(hunterPosition));
		hunterPosition = new Point(1, 0);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		assertEquals(false, game.getGameOver());
		// move west
		game.movePlayer(Direction.West);
		assertFalse(game.getPlayerPosition().equals(hunterPosition));
		hunterPosition = new Point(0, 0);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		assertEquals(false, game.getGameOver());
		// move north
		game.movePlayer(Direction.North);
		assertFalse(game.getPlayerPosition().equals(hunterPosition));
		hunterPosition = new Point(0, 1);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		assertEquals(false, game.getGameOver());

		// move south twice to also check y axis wrap
		game.movePlayer(Direction.South);
		game.movePlayer(Direction.South);
		assertFalse(game.getPlayerPosition().equals(hunterPosition));
		hunterPosition = new Point(0, 9);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		assertEquals(false, game.getGameOver());
		// test default switch for no move
		game.movePlayer(Direction.None);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		assertEquals(false, game.getGameOver());
		// test get last position
		hunterPosition = new Point(0, 9);
		game.movePlayer(Direction.North);
		assertTrue(game.getPlayerPositionLast().equals(hunterPosition));
		assertEquals(false, game.getGameOver());
		// move west again to check x axis wrap
		game.movePlayer(Direction.West);
		assertTrue(game.getPlayerPosition().equals(new Point(9, 0)));
		assertEquals(false, game.getGameOver());
		//end game
		game.shootArrow(Direction.North);
		assertEquals(true, game.getGameOver());

	}

	/**
	 * Test for game reset function and that gameover acts correctly
	 **/
	@Test
	public void testResetGameGameOver() {
		// set up
		Point hunterPosition = new Point(0, 0);
		GameMapFactory mf = new GameMapFactory(new Obstacle[GRID_SIZE][GRID_SIZE], new Random(), GRID_SIZE, lbPits,
				ubPits);
		mf.fillEmpty();
		boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];
		Game game = new Game(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);

		// shoot arrow north then reset game
		game.shootArrow(Direction.North);
		assertEquals(true, game.getGameOver());
		game.resetGame(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		// shoot arrow east then reset game
		game.shootArrow(Direction.East);
		assertEquals(true, game.getGameOver());
		game.resetGame(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		// shoot arrow south then reset game
		game.shootArrow(Direction.South);
		assertEquals(true, game.getGameOver());
		game.resetGame(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		// shoot arrow west then reset game
		game.shootArrow(Direction.West);
		assertEquals(true, game.getGameOver());
		game.resetGame(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));

		// move player north then reset game
		game = new Game(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		game.movePlayer(Direction.North);
		assertEquals(false, game.getGameOver());
		game.resetGame(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		// move player east then reset game
		game = new Game(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		game.movePlayer(Direction.East);
		assertEquals(false, game.getGameOver());
		game.resetGame(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		// move player South then reset game
		game = new Game(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		game.movePlayer(Direction.South);
		assertEquals(false, game.getGameOver());
		game.resetGame(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));
		// move player west then reset game
		game = new Game(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		game.movePlayer(Direction.West);
		assertEquals(false, game.getGameOver());
		game.resetGame(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		assertTrue(game.getPlayerPosition().equals(hunterPosition));

		// player fall into pit
		game = new Game(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		Obstacle[][] obstacle = new Obstacle[2][2];
		obstacle[1][1] = Obstacle.Pit;
		GameMap map = new GameMap(obstacle);
		game = new Game(2, map, new boolean[2][2], new Point(1, 0));
		assertFalse(game.getGameOver() == true);
		assertEquals(false, game.getGameOver());
		game.movePlayer(Direction.North);
		assertTrue(game.getGameOver() == true);
		assertEquals(true, game.getGameOver());

		// player eaten by Wumpus
		game = new Game(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		obstacle = new Obstacle[2][2];
		obstacle[1][1] = Obstacle.Wumpus;
		map = new GameMap(obstacle);
		game = new Game(2, map, new boolean[2][2], new Point(1, 0));
		assertFalse(game.getGameOver() == true);
		assertEquals(false, game.getGameOver());
		game.movePlayer(Direction.North);
		assertTrue(game.getGameOver() == true);
		assertEquals(true, game.getGameOver());

		// test that player cannot move when game is over
		game.resetGame(2, map, new boolean[2][2], new Point(1, 0));
		assertFalse(game.getGameOver() == true);
		assertEquals(false, game.getGameOver());
		game.movePlayer(Direction.North);
		assertEquals(true, game.getGameOver());
		assertFalse(game.getPlayerPosition().equals(new Point(0, 0)));
		game.resetGame(2, map, new boolean[2][2], new Point(1, 0));
		assertEquals(false, game.getGameOver());
		

	}

	/**
	 * Test that shooting arrow ends game by eitherhitting wumpus or hitting
	 * hunter
	 */
	@Test
	public void testShootArrowand() {
		// set up
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
		// arrow kills hunter fired north 
		assertFalse(game.arrowShot());
		game.shootArrow(Direction.North);
		assertTrue(game.arrowShot());
		assertTrue(game.getGameOver() == true);
		
		// arrow shoots wumpus fired north
		game.resetGame(2, map, new boolean[2][2], new Point(0, 0));
		assertFalse(game.arrowShot());
		game.movePlayer(Direction.East);
		assertFalse(game.arrowShot());
		assertTrue(game.shootArrow(Direction.North));
		assertTrue(game.arrowShot());
		assertTrue(game.getGameOver() == true);

		// arrow shoots hunter fired east
		game.resetGame(2, map, new boolean[2][2], new Point(0, 0));
		assertFalse(game.arrowShot());
		game.shootArrow(Direction.East);
		assertTrue(game.arrowShot());
		assertTrue(game.getGameOver() == true);
		
		// arrow shoots wumpus fired east
		game.resetGame(2, map, new boolean[2][2], new Point(0, 0));
		assertFalse(game.arrowShot());
		game.movePlayer(Direction.North);
		assertTrue(game.shootArrow(Direction.East));
		assertTrue(game.arrowShot());
		assertTrue(game.getGameOver() == true);

		// arrow shoots hunter fired south
		game.resetGame(2, map, new boolean[2][2], new Point(0, 0));
		assertFalse(game.arrowShot());
		game.shootArrow(Direction.South);
		assertTrue(game.arrowShot());
		assertTrue(game.getGameOver() == true);
		
		// arrow shoots wumpus fired south
		game.resetGame(2, map, new boolean[2][2], new Point(0, 0));
		assertFalse(game.arrowShot());
		game.movePlayer(Direction.East);
		assertFalse(game.arrowShot());
		assertTrue(game.shootArrow(Direction.South));
		assertTrue(game.arrowShot());
		assertTrue(game.getGameOver() == true);

		// arrow shoots hunter fired west
		game.resetGame(2, map, new boolean[2][2], new Point(0, 0));
		assertFalse(game.arrowShot());
		game.shootArrow(Direction.West);
		assertTrue(game.arrowShot());
		assertTrue(game.getGameOver() == true);
		
		
		// arrow shoots wumpus fired east
		game.resetGame(2, map, new boolean[2][2], new Point(0, 0));
		game.movePlayer(Direction.North);
		assertFalse(game.arrowShot());
		assertTrue(game.shootArrow(Direction.West));
		assertTrue(game.arrowShot());
		assertTrue(game.getGameOver() == true);

		// player eaten cannot shoot arrow test
		game.resetGame(2, map, new boolean[2][2], new Point(0, 0));
		game.movePlayer(Direction.East);
		game.movePlayer(Direction.North);
		assertFalse(game.arrowShot());
		assertTrue(game.getGameOver() == true);
		assertFalse(game.shootArrow(Direction.North));

		// hunter kills wumpus with wrap around shooting north
		obstacle[1][1] = Obstacle.Empty;
		obstacle[1][0] = Obstacle.Wumpus;
		game.resetGame(2, map, new boolean[2][2], new Point(1, 1));
		assertTrue(game.shootArrow(Direction.North));

		// kills wumpus shooting east wrap around
		obstacle[1][0] = Obstacle.Empty;
		obstacle[0][0] = Obstacle.Wumpus;
		game.resetGame(2, map, new boolean[2][2], new Point(1, 0));
		assertTrue(game.shootArrow(Direction.East));

		// kills wumpus shooting south wrap around
		obstacle[1][1] = Obstacle.Wumpus;
		obstacle[0][0] = Obstacle.Empty;
		game.resetGame(2, map, new boolean[2][2], new Point(1, 0));
		assertTrue(game.shootArrow(Direction.South));

		// kills wumpus shooting west wrap around
		game.resetGame(2, map, new boolean[2][2], new Point(0, 1));
		assertTrue(game.shootArrow(Direction.West));

		// reset obstacle
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				obstacle[i][j] = Obstacle.Empty;
			}
		}

		// hunter kills self shooting north wrap around
		game = new Game(2, map, new boolean[2][2], new Point(0, 0));
		game.shootArrow(Direction.North);
		assertTrue(game.getGameOver() == true);
		// hunter kills self shooting east wrap around
		game = new Game(2, map, new boolean[2][2], new Point(0, 0));
		game.shootArrow(Direction.East);
		assertTrue(game.getGameOver() == true);
		// hunter kills self shooting south wrap around
		game = new Game(2, map, new boolean[2][2], new Point(0, 0));
		game.shootArrow(Direction.South);
		assertTrue(game.getGameOver() == true);
		// hunter kills self shooting west wrap around
		game = new Game(2, map, new boolean[2][2], new Point(0, 0));
		game.shootArrow(Direction.West);
		assertTrue(game.getGameOver() == true);
		// no shoot
		game = new Game(2, map, new boolean[2][2], new Point(0, 0));
		game.shootArrow(Direction.None);
		assertTrue(game.getGameOver() == true);

	}

	/**
	 * test what is in the space and returning get visited also tests get grid
	 * size
	 */
	@Test
	public void testGetVisitedWhatIsHere() {
		// set up
		Point hunterPosition = new Point(0, 0);
		GameMapFactory mf = new GameMapFactory(new Obstacle[GRID_SIZE][GRID_SIZE], new Random(), GRID_SIZE, lbPits,
				ubPits);
		mf.fillEmpty();
		boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];
		Game game = new Game(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
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
		// test wumpus blood slime and pit
		game = new Game(2, map, new boolean[2][2], new Point(0, 0));
		assertTrue(game.whatIsHere(new Point(1, 1)) == Obstacle.Wumpus);
		assertFalse(game.whatIsHere(new Point(0, 1)) == Obstacle.Wumpus);
		assertTrue(game.whatIsHere(new Point(0, 0)) == Obstacle.Blood);
		assertFalse(game.whatIsHere(new Point(0, 1)) == Obstacle.Blood);
		assertTrue(game.whatIsHere(new Point(1, 0)) == Obstacle.Slime);
		assertFalse(game.whatIsHere(new Point(1, 1)) == Obstacle.Slime);
		assertTrue(game.whatIsHere(new Point(0, 1)) == Obstacle.Pit);
		assertFalse(game.whatIsHere(new Point(1, 1)) == Obstacle.Pit);

		// test Hunter Goop and EMpty
		obstacle[1][1] = Obstacle.Hunter;
		obstacle[0][0] = Obstacle.Goop;
		obstacle[1][0] = Obstacle.Empty;
		assertTrue(game.whatIsHere(new Point(1, 1)) == Obstacle.Hunter);
		assertFalse(game.whatIsHere(new Point(0, 1)) == Obstacle.Hunter);
		assertTrue(game.whatIsHere(new Point(0, 0)) == Obstacle.Goop);
		assertFalse(game.whatIsHere(new Point(1, 0)) == Obstacle.Goop);
		assertTrue(game.whatIsHere(new Point(1, 0)) == Obstacle.Empty);
		assertFalse(game.whatIsHere(new Point(1, 1)) == Obstacle.Empty);
		// test grid size
		assertEquals(2, game.getGridSize());

	}

	/**
	 * test the Game messages
	 */
	@Test
	public void testGameMessage() {
		// set up
		Point hunterPosition = new Point(0, 0);
		GameMapFactory mf = new GameMapFactory(new Obstacle[GRID_SIZE][GRID_SIZE], new Random(), GRID_SIZE, lbPits,
				ubPits);
		mf.fillEmpty();
		boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];
		Game game = new Game(GRID_SIZE, mf.getGameMap(), visited, hunterPosition);
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				assertTrue(visited[i][j] == game.getVisited()[i][j]);
			}
		}
		GameOverReason reason = GameOverReason.Pit;

		// test the obstacles
		boolean gameOver = true;
	
		GameMessage message = new GameMessage(Obstacle.Wumpus, gameOver, reason, Direction.East);
		assertEquals(true, message.getGameOver());
		assertTrue(message.getObstacle() == Obstacle.Wumpus);
		assertFalse(message.getObstacle() == Obstacle.Blood);
		message = new GameMessage(Obstacle.Blood, gameOver, reason, Direction.East);
		assertEquals(true, message.getGameOver());
		assertTrue(message.getObstacle() == Obstacle.Blood);
		assertFalse(message.getObstacle() == Obstacle.Wumpus);
		message = new GameMessage(Obstacle.Slime, gameOver, reason, Direction.East);
		assertEquals(true, message.getGameOver());
		assertTrue(message.getObstacle() == Obstacle.Slime);
		assertFalse(message.getObstacle() == Obstacle.Blood);
		message = new GameMessage(Obstacle.Goop, gameOver, reason, Direction.East);
		assertEquals(true, message.getGameOver());
		assertTrue(message.getObstacle() == Obstacle.Goop);
		assertFalse(message.getObstacle() == Obstacle.Slime);
		message = new GameMessage(Obstacle.Hunter, gameOver, reason, Direction.East);
		assertEquals(true, message.getGameOver());
		assertTrue(message.getObstacle() == Obstacle.Hunter);
		assertFalse(message.getObstacle() == Obstacle.Goop);
		message = new GameMessage(Obstacle.Pit, gameOver, reason, Direction.East);
		assertEquals(true, message.getGameOver());
		assertTrue(message.getObstacle() == Obstacle.Pit);
		assertFalse(message.getObstacle() == Obstacle.Hunter);
		message = new GameMessage(Obstacle.Empty, gameOver, reason, Direction.East);
		assertEquals(true, message.getGameOver());
		assertTrue(message.getObstacle() == Obstacle.Empty);
		assertFalse(message.getObstacle() == Obstacle.Pit);

		// test game over
		message = new GameMessage(Obstacle.Slime, gameOver, reason, Direction.East);
		assertEquals(true, message.getGameOver());
		gameOver = false;
		message = new GameMessage(Obstacle.Slime, gameOver, reason, Direction.East);
		assertEquals(false, message.getGameOver());

		// test game over reason
		gameOver = true;
		message = new GameMessage(Obstacle.Pit, gameOver, reason, Direction.East);
		assertEquals(true, message.getGameOver());
		assertEquals(GameOverReason.Pit, message.getGameOverReason());
		reason = GameOverReason.Wumpus;
		message = new GameMessage(Obstacle.Pit, gameOver, reason, Direction.East);
		assertEquals(true, message.getGameOver());
		assertEquals(GameOverReason.Wumpus, message.getGameOverReason());
		reason = GameOverReason.ArrowHitHunter;
		message = new GameMessage(Obstacle.Pit, gameOver, reason, Direction.East);
		assertEquals(true, message.getGameOver());
		assertEquals(GameOverReason.ArrowHitHunter, message.getGameOverReason());
		reason = GameOverReason.ArrowHitWumpus;
		message = new GameMessage(Obstacle.Pit, gameOver, reason, Direction.East);
		assertEquals(true, message.getGameOver());
		assertEquals(GameOverReason.ArrowHitWumpus, message.getGameOverReason());
		reason = GameOverReason.Default;
		message = new GameMessage(Obstacle.Pit, gameOver, reason, Direction.East);
		assertEquals(true, message.getGameOver());
		assertEquals(GameOverReason.Default, message.getGameOverReason());
		reason = GameOverReason.Reset;
		message = new GameMessage(Obstacle.Pit, gameOver, reason, Direction.East);
		assertEquals(true, message.getGameOver());
		assertEquals(GameOverReason.Reset, message.getGameOverReason());

		// test direction
		message = new GameMessage(Obstacle.Pit, gameOver, reason, Direction.East);
		assertEquals(true, message.getGameOver());
		assertEquals(Direction.East, message.getDirection());
		message = new GameMessage(Obstacle.Pit, gameOver, reason, Direction.West);
		assertEquals(true, message.getGameOver());
		assertEquals(Direction.West, message.getDirection());
		message = new GameMessage(Obstacle.Pit, gameOver, reason, Direction.North);
		assertEquals(true, message.getGameOver());
		assertEquals(Direction.North, message.getDirection());
		message = new GameMessage(Obstacle.Pit, gameOver, reason, Direction.South);
		assertEquals(true, message.getGameOver());
		assertEquals(Direction.South, message.getDirection());

	}

}
