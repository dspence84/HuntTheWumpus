package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.Game;
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
		panelSizeInPixels = 600;
		this.setBackground(Color.BLACK);
		
		
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		Point playerPosition = game.getPlayerPosition();
		
		boolean[][] visited = game.getVisited();
		
		// Draw background image 100 times
		for (int y = 0; y < panelSizeInPixels; y += gridSquareSizeInPixels())
			for (int x = 0; x < panelSizeInPixels; x += gridSquareSizeInPixels()) {
				if(visited[pixelToGrid(x)][pixelToGrid(y)] == true) {	
					g2.drawImage(tile,  x + 25, panelSizeInPixels - y, gridSquareSizeInPixels(), gridSquareSizeInPixels(), null);
					g2.drawImage(whichImage(pixelToGrid(x), pixelToGrid(y)), x + 25, panelSizeInPixels - y, gridSquareSizeInPixels(), gridSquareSizeInPixels(), null);
				}
			}

		//System.out.println(X + " " + Y);
		g2.drawImage(player, gridToPixel(playerPosition.x) + 25, panelSizeInPixels - gridToPixel(playerPosition.y), 
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
	public void update(Observable o, Object arg) {
		repaint();
		
	}

}
