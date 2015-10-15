package test;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.Random;

import org.junit.Test;

import model.GameMapFactory;
import model.Obstacle;

public class GameMapFactoryTest {

	private static final int GRID_SIZE = 20;	// test grid size
	private static final int lbPits = 5;		// there are <-----
	private static final int ubPits = 8;		// to <----- pits for this test	

	private GameMapFactory instantiateMF() {		
		return new GameMapFactory(new Obstacle[GRID_SIZE][GRID_SIZE], new Random(), GRID_SIZE, lbPits, ubPits);
	}
	
	@Test
	public void testFillEmpty() {		
		GameMapFactory mf = instantiateMF();
		mf.fillEmpty();

		Obstacle[][] map = mf.getMap();
		for (int x = 0; x < GRID_SIZE; x++) {
			for (int y = 0; y < GRID_SIZE; y++) {
				assertTrue(map[x][y] == Obstacle.Empty);
			}
		}
	}

	@Test
	public void testCalculateWrap() {
		Point p;

		GameMapFactory mf = instantiateMF();
		// test wrap for x and y at the same time going south and west
		p = mf.calculateWrap(new Point(0, 0), -1, -1);
		assertEquals(GRID_SIZE - 1, p.x);
		assertEquals(GRID_SIZE - 1, p.y);

		// test wrap for x going west
		p = mf.calculateWrap(new Point(0, 0), -1, 0);
		assertEquals(GRID_SIZE - 1, p.x);
		assertEquals(0, p.y);

		// test wrap for y going south
		p = mf.calculateWrap(new Point(0, 0), 0, -1);
		assertEquals(0, p.x);
		assertEquals(GRID_SIZE - 1, p.y);

		// test wrap for x and y at the same time going north and east
		p = mf.calculateWrap(new Point(GRID_SIZE - 1, GRID_SIZE - 1), 1, 1);
		assertEquals(0, p.x);
		assertEquals(0, p.y);

		// test wrap for y going north
		p = mf.calculateWrap(new Point(GRID_SIZE - 1, GRID_SIZE - 1), 0, 1);
		assertEquals(GRID_SIZE - 1, p.x);
		assertEquals(0, p.y);

		// test wrap for x going east
		p = mf.calculateWrap(new Point(GRID_SIZE - 1, GRID_SIZE - 1), 1, 0);
		assertEquals(0, p.x);
		assertEquals(GRID_SIZE - 1, p.y);
	}

	// test all the pits were placed in their correct spots
	@Test
	public void testPlacePitsAndSlimes() {
		GameMapFactory mf = instantiateMF();
		mf.fillEmpty();
		Point[] pits = mf.placePits();
		Obstacle[][] map = mf.getMap();

		for (Point pit : pits) {
			assertEquals(Obstacle.Pit, map[pit.x][pit.y]);
		}
		
		mf.placeSlimes(pits);		
		map = mf.getMap();
		
		for (Point pit : pits) {
			assertTrue(mf.whatIsHere(mf.calculateWrap(pit, 1, 0)) == Obstacle.Slime ||
					mf.whatIsHere(mf.calculateWrap(pit, 1, 0)) == Obstacle.Pit);
			assertTrue(mf.whatIsHere(mf.calculateWrap(pit, -1, 0)) == Obstacle.Slime ||
					mf.whatIsHere(mf.calculateWrap(pit, -1, 0)) == Obstacle.Pit);
			assertTrue(mf.whatIsHere(mf.calculateWrap(pit, 0, 1)) == Obstacle.Slime ||
					mf.whatIsHere(mf.calculateWrap(pit, 0, 1)) == Obstacle.Pit);
			assertTrue(mf.whatIsHere(mf.calculateWrap(pit, 0, -1)) == Obstacle.Slime ||
					mf.whatIsHere(mf.calculateWrap(pit, 0, -1)) == Obstacle.Pit);
		}
	}
	
	@Test
	public void testPlaceWumpusAndBlood() {
		GameMapFactory mf = instantiateMF();
		mf.fillEmpty();
		
		Point wumpus = mf.placeWumpusAndBlood();
		
		assertEquals(Obstacle.Wumpus, mf.whatIsHere(new Point(wumpus.x, wumpus.y)));
		
		assertEquals(Obstacle.Blood, mf.whatIsHere(mf.calculateWrap(wumpus, 1, 0)));
		assertEquals(Obstacle.Blood, mf.whatIsHere(mf.calculateWrap(wumpus, 2, 0)));
		assertEquals(Obstacle.Blood, mf.whatIsHere(mf.calculateWrap(wumpus, -1, 0)));
		assertEquals(Obstacle.Blood, mf.whatIsHere(mf.calculateWrap(wumpus, -2, 0)));
		assertEquals(Obstacle.Blood, mf.whatIsHere(mf.calculateWrap(wumpus, 0, 1)));
		assertEquals(Obstacle.Blood, mf.whatIsHere(mf.calculateWrap(wumpus, 0, 2)));
		assertEquals(Obstacle.Blood, mf.whatIsHere(mf.calculateWrap(wumpus, 0, -1)));
		assertEquals(Obstacle.Blood, mf.whatIsHere(mf.calculateWrap(wumpus, 0, -2)));
		assertEquals(Obstacle.Blood, mf.whatIsHere(mf.calculateWrap(wumpus, 1, 1)));
		assertEquals(Obstacle.Blood, mf.whatIsHere(mf.calculateWrap(wumpus, 1, -1)));
		assertEquals(Obstacle.Blood, mf.whatIsHere(mf.calculateWrap(wumpus, -1, 1)));
		assertEquals(Obstacle.Blood, mf.whatIsHere(mf.calculateWrap(wumpus, -1, -1)));

	}
	
	@Test
	public void testPlaceBlood() {
		GameMapFactory mf = instantiateMF();
		mf.fillEmpty();
		Obstacle[][] map = mf.getMap();
		
		mf.fillEmpty();
		mf.placeBlood(new Point(0,0));
		
		assertEquals(Obstacle.Blood, map[0][0]);
	}
	
	@Test
	public void testGenerateRandom() {
		Random r = new Random();
		GameMapFactory mf = new GameMapFactory(new Obstacle[GRID_SIZE][GRID_SIZE], r, GRID_SIZE, lbPits, ubPits);
		
		assertEquals(5, mf.generateRandom(5,5));
		
		int random = mf.generateRandom(3, 5);		
		assertTrue(random >= 3 && random <=5);
		
		// try 100 randoms and make sure they're within the bounds!
		for(int i = 0; i < 100; i++) {
			random = mf.generateRandom(0, 9);
			assertTrue(random >= 0 && random <= 9);
		}
		
		// by resetting the seed, we can see if we are actually getting what we want here
		r.setSeed(5L);
		int number = r.nextInt(10 - 4 + 1) + 4;
		r.setSeed(5L);
		assertEquals(number, mf.generateRandom(4, 10));
		
	}

	@Test
	public void testSetupMap() {
		GameMapFactory mf = instantiateMF();
		
		Obstacle[][] map = mf.setupMap();
		
		
	}
	
	@Test
	public void testPrint() {
		GameMapFactory mf = instantiateMF();
		
		Obstacle[][] map = mf.setupMap();
		mf.printMap();
	}
}
