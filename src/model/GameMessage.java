package model;

public class GameMessage {
	private Obstacle obstacle;
	private boolean gameOver;
	private GameOverReason reason;
	private Direction direction;
	
	public GameMessage(Obstacle obstacle, boolean gameOver, GameOverReason reason, Direction direction) {
		this.obstacle = obstacle;
		this.gameOver = gameOver;
		this.reason = reason;
		this.direction = direction;
	}
	
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
