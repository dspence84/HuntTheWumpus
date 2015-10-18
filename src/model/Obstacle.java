/*Daniel Spence
 * Joshua Adams
 * 
 * Holds the different obstacles possible for the game
 */

package model;

public enum Obstacle {
	Empty(" "), Blood("B"), Pit("P"), Slime("S"), Goop("G"), Hunter("O"), Wumpus("W");
	
	private String obstacleString;
	
	private Obstacle(String item) {
		obstacleString = item;
	}
	
	@Override 
	public String toString() {
		return obstacleString;
	}
}


