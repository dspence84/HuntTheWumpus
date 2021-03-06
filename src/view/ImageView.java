/*Daniel Spence
 * Joshua Adams
 * 
 * gives the graphical view to the user by extending a JPanel
 */

package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.Direction;
import model.Game;
import model.GameMessage;
import model.GameOverReason;
import model.Obstacle;

@SuppressWarnings("serial")
public class ImageView extends JPanel implements Observer {

	private Image tile;
	private Image player;
	private Image wumpus;
	private Image slime;
	private Image slimePit;
	private Image blood;
	private Image goop;
	
	private int gridSize;
	private int panelSizeInPixels;
	
	private int X;
	private int Y;
	private int tic;
	private int tics = 20; 
	private Direction direction;
	
	private Timer animationTimer;
	
	private Game game;
	
	
	/**
	 * Method: ImageView
	 * the image view set up for HuntTheWumpus
	 * 
	 * @param game
	 * 			A Game
	 * @return none
	 */
	public ImageView(Game game) {
		
		this.game = game;

		//try catch for image files
		try {
			player = ImageIO.read(new File("./images/TheHunter.png"));
			tile = ImageIO.read(new File("./images/Ground.png"));
			wumpus = ImageIO.read(new File("./images/Wumpus.png"));
			slime = ImageIO.read(new File("./images/Slime.png"));
			slimePit = ImageIO.read(new File("./images/SlimePit.png"));
			blood = ImageIO.read(new File("./images/Blood.png"));
			goop = ImageIO.read(new File("./images/Goop.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		gridSize = game.getGridSize();
		panelSizeInPixels = 400;
		this.setBackground(Color.BLACK);
		
		X = gridToPixel(game.getPlayerPosition().x) + 50;
		Y = panelSizeInPixels - gridToPixel(game.getPlayerPosition().y);
		
		tic = 0;
		animationTimer = new Timer(20, new AnimationTimerListener());
		
		repaint();
	}
	
	/**
	 * timer listener for animation
	 *
	 */
	private class AnimationTimerListener implements ActionListener {

		/**
		 * Method: actionPerformed
		 * @param arg0
		 *        ActionEvent
		 * @return none
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(tic > tics || (game.arrowShot() == true)) {
				animationTimer.stop();
				X = gridToPixel(game.getPlayerPosition().x) + 50;
				Y = panelSizeInPixels - gridToPixel(game.getPlayerPosition().y);
				direction = Direction.None;
				tic = 0;
				
			} else {
			
				tic++; 
				
				switch(direction) {
				case North:
					Y -= gridSquareSizeInPixels() / tics;
					break;
				case South:
					Y += gridSquareSizeInPixels() / tics;
					break;
				case East:
					X += gridSquareSizeInPixels() / tics;
					break;
				case West:
					X -= gridSquareSizeInPixels() / tics;
					break;
				default:
					break;

				}					
			}

			repaint();
		
		}
	}
	
	/**
	 * Method: drawWithAnimation
	 * @param direction
	 * 			a Direction
	 * @return none
	 */
	private void drawWithAnimation(Direction direction) {
		
		X = gridToPixel(game.getPlayerPositionLast().x) + 50;
		Y = panelSizeInPixels - gridToPixel(game.getPlayerPositionLast().y);
		this.direction = direction;
		
		animationTimer.start();
		
		
	}
	
	/**
	 * Method paintComponent
	 * @param g
	 *        Graphics
	 * @return none
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		
		boolean[][] visited = game.getVisited();
		
		// Draw background image 100 times
		for (int y = 0; y < panelSizeInPixels; y += gridSquareSizeInPixels())
			for (int x = 0; x < panelSizeInPixels; x += gridSquareSizeInPixels()) {
				if(visited[pixelToGrid(x)][pixelToGrid(y)] == true) {	
					g2.drawImage(tile,  x + 50, panelSizeInPixels - y, gridSquareSizeInPixels(), gridSquareSizeInPixels(), null);
					g2.drawImage(whichImage(pixelToGrid(x), pixelToGrid(y)), x + 50, panelSizeInPixels - y, gridSquareSizeInPixels(), gridSquareSizeInPixels(), null);
				}
			}

		
		g2.drawImage(player, X, Y , 
				gridSquareSizeInPixels(), gridSquareSizeInPixels(), null);
	}
	
	/**
	 * 
	 * @return int
	 * @param none
	 */
	private int gridSquareSizeInPixels() {
		return (int) Math.ceil( (double) panelSizeInPixels / gridSize );
	}
	
	/**
	 * 
	 * @param x
	 * @return gridSquareSizeInPixels * x
	 *        an int
	 */
	private int gridToPixel(int x) {
		return gridSquareSizeInPixels() * x;
	}
	/**
	 * 
	 * @param pixel
	 *        an int
	 * @return pixel / gridSquareSizeInPixels()
	 *         an int
	 * 			
	 */
	private int pixelToGrid(int pixel) {
		return (pixel / gridSquareSizeInPixels());
	}
	
	/**
	 * 
	 * @param x
	 * 		an int
	 * @param y
	 * 		an int
	 * @return image
	 * 		an Image
	 */
	private Image whichImage(int x, int y) {
		Obstacle obstacle = game.whatIsHere(new Point(x,y));		
		Image image;
		
		switch(obstacle) {
		case Wumpus:
			image = wumpus;
			break;
		case Pit:
			image = slimePit;
			break;
		case Slime:
			image = slime;
			break;
		case Goop:
			image = goop;
			break;
		case Blood:
			image = blood;
			break;
		case Empty:
			image = tile;
			break;
		default:
			image = tile;
			break;
			
		}
		
		return image;
	}
	
	/**
	 * Method: update
	 * @param game
	 * 			Observable
	 * @param gameMessage
	 * 			Object
	 * @return none
	 * 
	 */
	@Override
	public void update(Observable game, Object gameMessage) {
		GameMessage gm = (GameMessage) gameMessage;
		if(gm.getGameOverReason() == GameOverReason.Reset) {
			gridSize = ((Game) game).getGridSize();
			X = gridToPixel( ((Game) game).getPlayerPosition().x) + 50;
			Y = panelSizeInPixels - gridToPixel( ((Game) game).getPlayerPosition().y);
		}
		
		if(gm.getDirection() != Direction.None)
			drawWithAnimation(gm.getDirection());
		else
			repaint();
		
	}

}
