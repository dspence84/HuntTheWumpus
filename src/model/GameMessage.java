/*Daniel Spence
 * Joshua Adams
 * 
 * holds information for sending messages to the GUI
 */

package model;

/**
 * purpose is to send messages the the GUI's attached to the game
 *
 */
public class GameMessage {
	private Obstacle obstacle;
	private boolean gameOver;
	private GameOverReason reason;
	private Direction direction;
	
	/**
	 * Method: GameMessage
	 * creates a new game message
	 * @param obstacle the obstacle the player moved onto
	 * @param gameOver the gameOver condition
	 * @param reason the reason why a gameOver, if there was one, occured, otherwise GameOverReason.None
	 * @param direction the direction in which a player moved
	 * @return none
	 */
	public GameMessage(Obstacle obstacle, boolean gameOver, GameOverReason reason, Direction direction) {
		this.obstacle = obstacle;
		this.gameOver = gameOver;
		this.reason = reason;
		this.direction = direction;
	}
	
	
	/*-----------------------------------------
	 * GETTERS
	 * ----------------------------------------*/
	
	public Obstacle getObstacle() {
		return obstacle;
	}
	
	public boolean getGameOver() {
		return gameOver;
	}
	
	public GameOverReason getGameOverReason() {
		return reason;
	}
	
	public Direction getDirection() {
		return direction;
	}
}
