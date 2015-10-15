package view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import model.Game;
import model.GameMessage;
import model.Obstacle;

public class TextView extends JPanel implements Observer {
	
	private JTextArea textView;
	private Game game;
	private String gameString;
	
	public TextView(Game game) {
		this.game = game;
		textView = new JTextArea();
		textView.setEditable(false);
		textView.setFocusable(false);
		
		add(textView);
		
		textView.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		textView.setText(buildMap());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		
	}
	
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
	
	@Override
	public void update(Observable game, Object gameMessage) {		
		
		textView.setText(buildMap());
		
		GameMessage gm = (GameMessage) gameMessage;
		
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
