/*Daniel Spence
 * Joshua Adams
 * 
 * creates and specifies a game map
 */

package model;

import java.awt.Point;
import java.util.Random;

/**
 * this class allows testing of all the game map creation logic
 *
 */
public class GameMapFactory {
	private int gridSize;
	private Obstacle[][] theMap;
	private int lowerBoundPits;
	private int upperBoundPits;
	private Random r;
	private Point hunter;

	/**
	 * Method: GameMapFactor
	 * Creates a new game map
	 * 
	 * @param gridSize
	 *            the amount of spaces on the game board
	 * @param lowerBoundPits
	 *            lower bound to the random number of pits
	 * @param upperBoundPits
	 *            upper bound to the random number of pits
	 * Returns: none
	 */
	public GameMapFactory(Obstacle[][] map, Random r, int gridSize, int lowerBoundPits, int upperBoundPits) {
		this.r = r;
		this.gridSize = gridSize;
		this.lowerBoundPits = lowerBoundPits;
		this.upperBoundPits = upperBoundPits;
		theMap = map;		
	}
	
	
	/**
	 * GETTER
	 * Method: getGridSize
	 * returns the grid size of the map being created
	 * @return gridSize(int)
	 */
	public int getGridSize() {
		return gridSize;
	}
	
	/**
	 * GETTER
	 * Method: getHunterPosition
	 * returns the hunter position if one is placed
	 * @return hunter
	 * 			a Point
	 */
	public Point getHunterPosition() {
		return hunter;
	}
	/**
	 * GETTER
	 * Method: getMap
	 * return the actual Obstacle map
	 * @return theMap(Obstacle)
	 */
	public Obstacle[][] getMap() {
		return theMap;
	}

	/**
	 * GETTER
	 * Method: getGameMap
	 * @param none
	 * @return GameMap
	 * 			a game map object of the map that is being created
	 */
	public GameMap getGameMap() {
		return new GameMap(theMap);
	}
	
	/**
	 * SETTER
	 * Method: setupMap
	 * runs all the algorithms needed for creating a game map according to the rules specified
	 * @param none
	 * @return theMap
	 * 			an Obstacle[][]
	 */
	public Obstacle[][] setupMap() {
		// first fill the map with empty spaces
		fillEmpty();
		
		// then place the pits
		Point[] pits = placePits();

		// place the slimes
		placeSlimes(pits);

		// place the wumpus and the blood/goop
		placeWumpusAndBlood();

		// finally, place the hunter
		placeHunter();		

		return theMap;
	}
	
	/**
	 * Method: fillEmpty
	 * fill a map with empty squares
	 * @param none
	 * @return none
	 */
	public void fillEmpty() {
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				theMap[x][y] = Obstacle.Empty;
			}
		}
	}

	/**
	 * Method: calculateWrap
	 * the purpose is to add an x and y to a point on the map and calculate the
	 * wrap around for you
	 * @param point
	 * 			A Point
	 * @param plusX
	 * 			an int
	 * @param plusY
	 * 			an int
	 * @return p
	 * 			a Point
	 */	
	public Point calculateWrap(Point point, int plusX, int plusY) {
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

	/**Method: getUniquePoint
	 * gets a unique point on a current map, one that is not occupied by anything
	 * @param none
	 * @return p 
	 * 			a Point
	 */
	public Point getUniquePoint() {
		Point p = new Point();
		int count = 0;
		do {
			p.x = generateRandom(0, gridSize - 1);
			p.y = generateRandom(0, gridSize - 1);
			count++;
		} while (theMap[p.x][p.y] != Obstacle.Empty && count < gridSize * gridSize);

		return p;
	}

	/**
	 * Method: placePits
	 * 
	 * places pits according to specified rules
	 * @param none
	 * @return pits
	 * 			Point[]
	 */
	public Point[] placePits() {
		int numPits = generateRandom(lowerBoundPits, upperBoundPits);
		Point[] pits = new Point[numPits];
		for (int i = 0; i < numPits; i++) {
			pits[i] = getUniquePoint();
			theMap[pits[i].x][pits[i].y] = Obstacle.Pit;
		}

		return pits;
	}

	/**
	 * Method: placeSlimes
	 * places the slimes according to specified rules
	 * @param pits
	 * 			a Point[]
	 * @return none
	 */
	public void placeSlimes(Point[] pits) {
		for (Point pit : pits) {
			Point slime;
			// place a slime pit north
			slime = calculateWrap(pit, 0, 1);
			if (whatIsHere(slime) == Obstacle.Empty) {
				theMap[slime.x][slime.y] = Obstacle.Slime;
			}
			// place slime south
			slime = calculateWrap(pit, 0, -1);
			if (whatIsHere(slime) == Obstacle.Empty) {
				theMap[slime.x][slime.y] = Obstacle.Slime;
			}
			// place slime east
			slime = calculateWrap(pit, 1, 0);
			if (whatIsHere(slime) == Obstacle.Empty) {
				theMap[slime.x][slime.y] = Obstacle.Slime;
			}
			// place slime west
			slime = calculateWrap(pit, -1, 0);
			if (whatIsHere(slime) == Obstacle.Empty) {
				theMap[slime.x][slime.y] = Obstacle.Slime;
			}
		}
	}

	/**
	 * Method: whatIsHere
	 * determines what is at a specific point on the map
	 * @param p the point object that specified where on the map
	 * @return theMap
	 * 			Obstacle[][]
	 */
	public Obstacle whatIsHere(Point p) {
		return theMap[p.x][p.y];
	}

	/**
	 * Method: placeWumpusAndBlood
	 * places the wumpus at a unique location and places his corrosponding blood
	 * @return wumpus
	 * 			Point
	 */
	public Point placeWumpusAndBlood() {
		Point wumpus = getUniquePoint();

		// place blood near the wumpus
		for (int x = -2; x <= 2; x++) {
			placeBlood(calculateWrap(wumpus, x, 0));
		}
		for (int y = -2; y <= 2; y++) {
			placeBlood(calculateWrap(wumpus, 0, y));
		}
		// and the corners near the wumpus
		placeBlood(calculateWrap(wumpus, -1, -1));
		placeBlood(calculateWrap(wumpus, 1, -1));
		placeBlood(calculateWrap(wumpus, -1, 1));
		placeBlood(calculateWrap(wumpus, 1, 1));

		// finally place the wumpus
		theMap[wumpus.x][wumpus.y] = Obstacle.Wumpus;

		return wumpus;

	}

	/**
	 * Method: printMap
	 * a debug function to print the map
	 * @param none
	 * @return none
	 */
	public void printMap() {
		for (int y = 0; y < gridSize; y++) {
			for (int x = 0; x < gridSize; x++) {
				System.out.print("[" + whatIsHere(new Point(x, y)) + "] ");
			}
			System.out.println();
		}
	}

	/**
	 * Method: placeBlood
	 * places a blood square at the given point
	 * @param blood a point object that specifies where to place the blood
	 * @return none
	 */
	public void placeBlood(Point blood) {
		if (whatIsHere(blood) == Obstacle.Empty) {
			theMap[blood.x][blood.y] = Obstacle.Blood;
		} else if (whatIsHere(blood) == Obstacle.Slime) {
			theMap[blood.x][blood.y] = Obstacle.Goop;
		} // do not overwrite pits...
	}

	/**
	 * Method: placeHunter
	 * places the hunter at a unique location, sets the hunter point field,
	 * get that with getHunterPosition()
	 * @param none
	 * @return hunter 
	 * 			the Point where the hunter was placed
	 */
	public Point placeHunter() {
		Point hunter = getUniquePoint();
		//theMap[hunter.x][hunter.y] = Obstacle.Hunter;
		
		this.hunter = hunter;
		
		return hunter;
	}
	
	/**
	 * Method: generateRandom
	 * generates a random int between lowerBound and upperBound
	 * @param lowerBound
	 * 			int
	 * @param upperBound
	 * 			int
	 * @return r
	 * 			int
	 */
	public int generateRandom(int lowerBound, int upperBound) {
		return r.nextInt(upperBound - lowerBound + 1) + lowerBound;		
	}
}
