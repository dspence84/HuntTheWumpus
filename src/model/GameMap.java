/*Daniel Spence
 * Joshua Adams
 * 
 * A simple object to hold the gameMap information
 */

package model;

import java.awt.Point;

/**
 * simple class to hold the game map for a game 
 *
 */
public class GameMap {
	private Obstacle[][] theMap;
	
	/**
	 * Method: GameMap
	 * constructor allows a static map to be entered for testing
	 * @param map
	 * Returns: None
	 */
	public GameMap(Obstacle[][] map) {
		this.theMap = map;
	}

	/**
	 * Method: whatIsHere
	 * determines what is at a specific location on the map
	 * @param p
	 * @return Obstacle
	 */
	public Obstacle whatIsHere(Point p) {
		return theMap[p.x][p.y];
	}
	
}
