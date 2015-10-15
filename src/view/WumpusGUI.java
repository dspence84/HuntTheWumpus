package view;

import java.awt.Color;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import model.Direction;
import model.Game;
import model.GameMap;
import model.GameMapFactory;
import model.GameMessage;
import model.GameOverReason;
import model.Obstacle;

public class WumpusGUI extends JFrame implements Observer {
	
	private final int GRID_SIZE = 10;
	private boolean[][] visited;
	private Game game;
	private TextView textPanel;
	private JTabbedPane tabPane;
	private JPanel controlPanel;
	private JTextArea controls;
	
	public static void main(String[] args) {
		new WumpusGUI().setVisible(true);
	}
	
	public WumpusGUI() {

		visited = new boolean[GRID_SIZE][GRID_SIZE];
		
		resetGame();
		registerListeners();
		layoutGUI();

		
	}
	
	private void resetGame() {
		GameMapFactory mf = new GameMapFactory(new Obstacle[GRID_SIZE][GRID_SIZE], new Random(), GRID_SIZE, 10, 13);
		mf.setupMap();
		game = new Game(GRID_SIZE, mf.getGameMap(), visited, mf.getHunterPosition());
	}
	
	public void layoutGUI() {
		
		setLayout(null);
		this.setSize(750,500);
		this.setLocation(50,50);

		TextView textPanel = new TextView(game);	
		
		
		ImageView imagePanel = new ImageView(game);
		
		game.addObserver(this);
		game.addObserver(textPanel);
		game.addObserver(imagePanel);
				
		this.controlPanel = new JPanel();
		controlPanel.setLocation(15, 50);
		controlPanel.setSize(250, 200);
		//controlPanel.setBackground(Color.BLACK);
		add(controlPanel);
		
		this.controls = new JTextArea();
		controls.setEditable(false);
		controls.setFocusable(false);
		controls.setText("Up Arrow:\tMove North\n"
				       + "Down Arrow:\tMove South\n"
				       + "Left Arrow:\tMove West\n"
				       + "Right Arrow:\tMove East\n"
				       + "\n\n"
				       + "W:\tShoot Arrow North\n"
				       + "S:\tShoot Arrow South\n"
				       + "A:\tShoot Arrow West\n"
				       + "D:\tShoot Arrow East");
		controlPanel.add(controls);
		
		this.tabPane = new JTabbedPane();
		tabPane.setLocation(280, 30);
		tabPane.setSize(420,400);
		tabPane.setFocusable(false);
		tabPane.addTab("Image View", imagePanel);
		tabPane.addTab("Text View", textPanel);		
		add(tabPane);
		
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
			
			
			if (ke.getKeyCode() == KeyEvent.VK_W)
				game.shootArrow(Direction.North);
			if (ke.getKeyCode() == KeyEvent.VK_S)
				game.shootArrow(Direction.South);
			if (ke.getKeyCode() == KeyEvent.VK_A)
				game.shootArrow(Direction.West);
			if (ke.getKeyCode() == KeyEvent.VK_D)
				game.shootArrow(Direction.East);
			
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
					break;
					
				case ArrowHitWumpus:
					JOptionPane.showMessageDialog(null, "Your arrow flies true and hits the Wumpus right between the eyes."
							+ "He falls over dead and the day is done.  Congratulations!");
					break;
					
				case ArrowHitHunter:
					JOptionPane.showMessageDialog(null, "You completely miss the Wumpus hitting yourself in the back of head!");
					break;
					
			default:
				break;
			}
						
			
		}
		
		
	}
}
