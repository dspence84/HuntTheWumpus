package model;

public class GameMessage {
	private Obstacle obstacle;
	private boolean gameOver;
	private GameOverReason reason;
	
	public GameMessage(Obstacle obstacle, boolean gameOver, GameOverReason reason) {
		this.obstacle = obstacle;
		this.gameOver = gameOver;
		this.reason = reason;
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
}
