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
	 * ctor allows a static map to be entered for testing
	 * @param map
	 */
	public GameMap(Obstacle[][] map) {
		this.theMap = map;
	}

	/**
	 * determines what is at a specific location on the map
	 * @param p
	 * @return
	 */
	public Obstacle whatIsHere(Point p) {
		return theMap[p.x][p.y];
	}
	
}
