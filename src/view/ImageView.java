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
	private int tics = 25;
	private Direction direction;
	
	private Timer animationTimer;
	
	private Game game;
	
	public ImageView(Game game) {
		
		this.game = game;
		
		
		
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
	
	private class AnimationTimerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(tic > tics) {
				animationTimer.stop();
				X = gridToPixel(game.getPlayerPosition().x) + 50;
				Y = panelSizeInPixels - gridToPixel(game.getPlayerPosition().y);
				tic = 0;
				
			} else {
			
				tic++;
				
				switch(direction) {
				case North:
					Y -= gridSquareSizeInPixels() / (double) tics;
					break;
				case South:
					Y += gridSquareSizeInPixels() / (double) tics;
					break;
				case East:
					X += gridSquareSizeInPixels() / (double) tics;
					break;
				case West:
					X -= gridSquareSizeInPixels() / (double) tics;
					break;
				default:
					break;

			}
						
			}
			
			repaint();
		
		}
	}
	
	private void drawWithAnimation(Direction direction) {
		
		X = gridToPixel(game.getPlayerPositionLast().x) + 50;
		Y = panelSizeInPixels - gridToPixel(game.getPlayerPositionLast().y);
		this.direction = direction;
		
		animationTimer.start();
		
		
	}
	
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

		//System.out.println(X + " " + Y);
		g2.drawImage(player, X, Y , 
				gridSquareSizeInPixels(), gridSquareSizeInPixels(), null);
	}
	
	private int gridSquareSizeInPixels() {
		return (int) Math.ceil( (double) panelSizeInPixels / gridSize );
	}
	
	private int gridToPixel(int x) {
		return gridSquareSizeInPixels() * x;
	}
	
	private int pixelToGrid(int pixel) {
		return (pixel / gridSquareSizeInPixels());
	}
	
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
	
	@Override
	public void update(Observable game, Object gameMessage) {
		GameMessage gm = (GameMessage) gameMessage;
		if(gm.getGameOverReason() == GameOverReason.Reset) {
			gridSize = ((Game) game).getGridSize();
		}
		
		
		drawWithAnimation(gm.getDirection());
		
	}

}
