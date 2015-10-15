package view;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Direction;
import model.Game;
import model.GameMap;
import model.GameMapFactory;
import model.GameMessage;
import model.GameOverReason;
import model.Obstacle;

public class WumpusGUI extends JFrame implements Observer {
	
	private final int GRID_SIZE = 15;
	private boolean[][] visited;
	private Game game;
	private TextView textPanel;
	
	public static void main(String[] args) {
		new WumpusGUI().setVisible(true);
	}
	
	public WumpusGUI() {

		visited = new boolean[GRID_SIZE][GRID_SIZE];
		
		
		
		registerListeners();
		
		resetGame();
		
		TextView textPanel = new TextView(game);	
		game.addObserver(this);
		game.addObserver(textPanel);
		
		add(textPanel);
		
		
		layoutGUI();		
		
	}
	
	private void resetGame() {
		GameMapFactory mf = new GameMapFactory(new Obstacle[GRID_SIZE][GRID_SIZE], new Random(), GRID_SIZE, 10, 13);
		mf.setupMap();
		game = new Game(GRID_SIZE, mf.getGameMap(), visited, mf.getHunterPosition());
	}
	
	public void layoutGUI() {
		this.setSize(500,500);
		this.setLocation(50,50);
		
	}
	
	public void registerListeners() {
		this.addKeyListener(new ArrowKeyListener());
	}
	
	private class ArrowKeyListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent ke) {

			
			if (ke.getKeyCode() == KeyEvent.VK_UP)
				game.movePlayer(Direction.North);
	
			if (ke.getKeyCode() == KeyEvent.VK_DOWN)
				game.movePlayer(Direction.South);
	
			if (ke.getKeyCode() == KeyEvent.VK_LEFT)
				game.movePlayer(Direction.West);
	
			if (ke.getKeyCode() == KeyEvent.VK_RIGHT)
				game.movePlayer(Direction.East);	
				
			}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		
	}

	@Override
	public void update(Observable game, Object gameMessage) {
		GameMessage gm = (GameMessage) gameMessage;
		
		if(gm.getGameOver() == true) {
			switch(gm.getGameOverReason()) {
				case Wumpus:
					JOptionPane.showMessageDialog(null, "While you ponder over whether it was a good idea to go"
							+ " toe to toe with the Wumpus or not, The Wumpus is busy tearing your head off!");
					break;
				case Pit:
					JOptionPane.showMessageDialog(null,  "You fall to your doom in a surprisingly deep pit.");
			default:
				break;
			}
			
			this.resetGame();
			
		}
		
		
	}
}
