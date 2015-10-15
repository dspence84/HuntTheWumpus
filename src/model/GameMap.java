package model;

import java.awt.Point;

public class GameMap {
	private Obstacle[][] theMap;
	
	/**
	 * ctor allows a static map to be entered for testing
	 * @param map
	 */
	public GameMap(Obstacle[][] map) {
		this.theMap = map;
	}

	public Obstacle whatIsHere(Point p) {
		return theMap[p.x][p.y];
	}
	
}
