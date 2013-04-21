import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.util.ArrayList;

import javax.swing.JComponent;

public class SceneGraph extends JComponent {

	public static ArrayList<Line2D.Double> junk;
	public Robot robot;
	
	public SceneGraph(){
		junk = new ArrayList<Line2D.Double>();
		junk.add(new Line2D.Double(65, 165, 105, 230));
		junk.add(new Line2D.Double(185, 145, 205, 90));
		junk.add(new Line2D.Double(210, 295, 250, 380));
		junk.add(new Line2D.Double(350, 345, 430, 380));
		junk.add(new Line2D.Double(435, 30, 540, 25));
		junk.add(new Line2D.Double(495, 320, 525, 230));
		junk.add(new Line2D.Double(630, 120, 675, 200));
		junk.add(new Line2D.Double(645, 355, 700, 290));
		
		robot = new Robot();
	}

	public void paint(Graphics g) {
		paintCanvas(g);
		paintRobot(g);
	}
	
	private void paintCanvas(Graphics g){
		g.drawRect (0, 0, 750, 400);
		for ( int i = 0; i < junk.size(); i++){
			g.drawLine((int)junk.get(i).x1, (int)junk.get(i).y1, (int)junk.get(i).x2, (int)junk.get(i).y2);
		}
	}
	
	private void paintRobot(Graphics g){
		robot.paint(g);
	}
}