/*Daniel Spence
 * Joshua Adams
 * 
 * gives the textview of the game model
 */

package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import model.Game;
import model.GameMessage;
import model.GameOverReason;
import model.Obstacle;

@SuppressWarnings({ "unused", "serial" })
public class TextView extends JPanel implements Observer {
	
	private JTextArea textView;
	private Game game; 
	private String gameString;
	private int gridSize;
	private final double FONT_RATIO = (25 * 10.0);
	
	/*----------------------
	 * constructor
	 *----------------------*/
	public TextView(Game game) {		
		this.game = game;
		gridSize = game.getGridSize();
		textView = new JTextArea();
		textView.setEditable(false);
		textView.setFocusable(false);
		
		add(textView);
		
		textView.setFont(new Font(Font.MONOSPACED, Font.PLAIN, getFontSize()));
		textView.setText(buildMap());
	}
	/*-----------------------------
	 * Getter
	 *----------------------------*/
	
	private int getFontSize() {
		return (int) (FONT_RATIO / gridSize); 
	}
	
	/**
	 * Method: paintComponent
	 * @param g
	 * 			Graphics
	 * @return none
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		
	}
	
	/**
	 * Method: buildMap
	 * builds the map in a string format
	 * @param none
	 * @return toString()
	 * 			String
	 */
	private String buildMap() {
		StringBuilder sb = new StringBuilder();
		Point playerPosition = game.getPlayerPosition();
		
		boolean[][] visited = game.getVisited();
		
		for(int y = visited.length-1; y >= 0; y--) {
			for(int x = 0; x < visited.length; x++) {
				sb.append("[");
				if(!visited[x][y]) {					
					sb.append("X");					
				} else if(x == playerPosition.x && y == playerPosition.y) {
					sb.append("O");
					
				}
				else {
					sb.append(game.whatIsHere(new Point(x,y)));					
				}
				sb.append("]");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	/**
	 * Method: update
	 * @param	game
	 * 			Observable
	 * @param	gameMessage
	 * 			Object
	 * @return none
	 */
	@Override
	public void update(Observable game, Object gameMessage) {		
		
		textView.setText(buildMap());
		
		GameMessage gm = (GameMessage) gameMessage;
		
		if(gm.getGameOverReason() == GameOverReason.Reset) {
			gridSize = ((Game) game).getGridSize();
			textView.setFont(new Font(Font.MONOSPACED, Font.PLAIN, getFontSize()));
		}
		
		Obstacle obstacle = gm.getObstacle();
		switch(obstacle) {
		case Blood:
			System.out.println("You smell something foul!");
			break;
		case Slime:
			System.out.println("You feel a draft.");
			break;
		case Goop:
			System.out.println("You smell something foul and feel a draft.");
		default:
			break;
		}
		
		
	}

}
