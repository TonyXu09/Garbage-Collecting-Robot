import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import javax.sound.sampled.Line;
import javax.swing.JComponent;


public class Robot extends JComponent{
	
	Body b;
	Arm a;
	boolean clawStatus; //0 is open, 1 is closed
	
	public Point rotationPoint;
	
	class Body extends JComponent{
		Rectangle recBody;
		Arc2D.Double arcBody;
		double rotation, deltax, deltay;
		AffineTransform currentTransform;
		
		//Constructor
		public Body(Rectangle rec, Arc2D.Double arc){
			currentTransform = new AffineTransform();
			deltax = 0;
			deltay = 0;
			recBody = rec;
			arcBody = arc;
			rotation = 0;
		}
		
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			AffineTransform saved = g2d.getTransform();
			g2d.rotate(Math.toRadians(rotation), rotationPoint.x, rotationPoint.y);
			g2d.translate(deltax, deltay);
			g.drawArc((int)arcBody.x, (int)arcBody.y, (int)arcBody.width, (int)arcBody.height, (int)arcBody.start, (int)arcBody.extent);
			g.drawRect(recBody.x, recBody.y, recBody.width, recBody.height);
			
			currentTransform = g2d.getTransform();
			g2d.setTransform(saved);
		}
		
		public void moveLeft(){
			deltax--;
		}
		
		public void moveRight(){
			deltax++;
		}
		
		public void moveUp(){
			deltay--;
		}
		
		public void moveDown(){
			deltay++;
		}
		
		public void rotateLeft(){
			rotation--;
		}
		
		public void rotateRight(){
			rotation++;
		}
		
		//Test for collision
		public boolean collides(Line2D.Double l){
			Shape result1 = new Rectangle();
			Shape result2 = new Arc2D.Double();
		
			result1 = currentTransform.createTransformedShape(recBody);
			result2 = currentTransform.createTransformedShape(arcBody);

			return (result1.getBounds().intersectsLine(l) || result2.getBounds().intersectsLine(l));
		}
		
		public boolean contains(Point p){
			Point result = new Point();
			try {
				currentTransform.inverseTransform(p, result);
			} catch (NoninvertibleTransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return (recBody.contains(result) || (arcBody.contains(result)));
		}
	}
	
	class Arm extends JComponent{
		
		AffineTransform currentTransform1;
		AffineTransform currentTransform2;
		AffineTransform currentTransform3;
		Polygon seg1, seg2, seg3;
		Arc2D.Double joint1, joint2, joint3;
		Rectangle claw1;
		Rectangle claw2;
		double rotation, deltax, deltay;
		double seg1Rot, seg2Rot, seg3Rot;
		
		public Arm(Polygon r1, Polygon r2, Polygon r3, Arc2D.Double a1, Arc2D.Double a2, Arc2D.Double a3, Rectangle c1, Rectangle c2){
			currentTransform1 = new AffineTransform();
			currentTransform2 = new AffineTransform();
			currentTransform3 = new AffineTransform();
			claw1 = c1;
			claw2 = c2;
			seg1Rot = 0;
			seg2Rot = 0;
			seg3Rot = 0;
			seg1 = r1;
			seg2 = r2;
			seg3 = r3;
			joint1 = a1;
			joint2 = a2;
			joint3 = a3;
		}

		public void moveLeft(){
			deltax--;
		}
		
		public void moveRight(){
			deltax++;
		}
		
		public void moveUp(){
			deltay--;
		}
		
		public void moveDown(){
			deltay++;
		}
		
		public void rotateLeft(){
			rotation--;
		}
		
		public void rotateRight(){
			rotation++;
		}
		
		// Return the segment number
		public int segmentNumber(Point p){
			Point result1 = new Point();
			Point result2 = new Point();
			Point result3 = new Point();
			try {
				currentTransform1.inverseTransform(p, result1);
			} catch (NoninvertibleTransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				currentTransform2.inverseTransform(p, result2);
			} catch (NoninvertibleTransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				currentTransform3.inverseTransform(p, result3);
			} catch (NoninvertibleTransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if ( seg1.contains(result1) || joint1.contains(result1)){
				return 1;
			}else if ( seg2.contains(result2) || joint2.contains(result2)){
				return 2;
			}else if ( seg3.contains(result3) || joint3.contains(result3)){
				return 3;
			}else{
				return -1;
			}
		}
		
		public void rotateSegLeft(int segNum){
			switch(segNum){
			case 1:
				seg1Rot--;
				break;
			case 2:
				seg2Rot--;
				break;
			case 3:
				seg3Rot--;
				break;
			default:
				System.out.println("Point coordination error");
				break;
			}
		}
		
		public void rotateSegRight(int segNum){
			switch(segNum){
			case 1:
				seg1Rot++;
				break;
			case 2:
				seg2Rot++;
				break;
			case 3:
				seg3Rot++;
				break;
			default:
				System.out.println("Point coordination error");
				break;
			}
		}
		
		public boolean isClaw(Point p){
			Point result = new Point();
			try {
				currentTransform3.inverseTransform(p, result);
			} catch (NoninvertibleTransformException e) {
				System.out.println("can't do transform");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return ( claw1.contains(result) || claw2.contains(result));
		}
		public void moveClaw(boolean status){
			//open claw
			if(status){
				claw1.x = claw1.x - claw1.width;
				claw2.x = claw2.x + claw2.width;
			}else{
				claw1.x = claw1.x + claw1.width;
				claw2.x = claw2.x - claw2.width;
			}
		}
		
		public boolean contains(Point p){
			Point result1 = new Point();
			Point result2 = new Point();
			Point result3 = new Point();
			try {
				currentTransform1.inverseTransform(p, result1);
			} catch (NoninvertibleTransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				currentTransform2.inverseTransform(p, result2);
			} catch (NoninvertibleTransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				currentTransform3.inverseTransform(p, result3);
			} catch (NoninvertibleTransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ( seg1.contains(result1) || seg2.contains(result2) || seg3.contains(result3) || 
					joint1.contains(result1) || joint2.contains(result2) || joint3.contains(result3)
					|| claw1.contains(result3) || claw2.contains(result3));
		}
		
		public boolean intersectsLine(Line2D.Double l){
			Shape result1 = new Rectangle();
			Shape result2 = new Rectangle();
			result1 = currentTransform3.createTransformedShape(claw1);
			result2 = currentTransform3.createTransformedShape(claw2);
			return (result1.getBounds().intersectsLine(l) || result2.getBounds().intersectsLine(l));
		}
		
		//Test for collision
		public boolean collides(Line2D.Double l){
			Shape result1 = new Rectangle();
			Shape result2 = new Rectangle();
			Shape result3 = new Rectangle();
			Shape result4 = new Arc2D.Double();
			Shape result5 = new Arc2D.Double();
			Shape result6 = new Arc2D.Double();
			
			result1 = currentTransform1.createTransformedShape(seg1);
			result2 = currentTransform2.createTransformedShape(seg2);
			result3 = currentTransform3.createTransformedShape(seg3);
			result4 = currentTransform1.createTransformedShape(joint1);
			result5 = currentTransform2.createTransformedShape(joint2);
			result6 = currentTransform3.createTransformedShape(joint3);
			
			return (result1.getBounds().intersectsLine(l) || result2.getBounds().intersectsLine(l)
					|| result3.getBounds().intersectsLine(l) || result4.getBounds().intersectsLine(l) ||
					result5.getBounds().intersectsLine(l) || result6.getBounds().intersectsLine(l));
		}
		
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			AffineTransform saved = g2d.getTransform();
			g2d.rotate(Math.toRadians(rotation), rotationPoint.x, rotationPoint.y);
			g2d.translate(deltax, deltay);
			
			g2d.rotate(Math.toRadians(seg1Rot), joint1.getCenterX(), joint1.getCenterY());
			g.drawPolygon(seg1);
			g.drawArc((int)joint1.x, (int)joint1.y, (int)joint1.width, (int)joint1.height, (int)joint1.start, (int)joint1.extent);
			
			currentTransform1 = g2d.getTransform();
			
			g2d.rotate(Math.toRadians(seg2Rot), joint2.getCenterX(), joint2.getCenterY());
			g.drawPolygon(seg2);
			g.drawArc((int)joint2.x, (int)joint2.y, (int)joint2.width, (int)joint2.height, (int)joint2.start, (int)joint2.extent);
			
			currentTransform2 = g2d.getTransform();
			
			g2d.rotate(Math.toRadians(seg3Rot), joint3.getCenterX(), joint3.getCenterY());
			g.drawPolygon(seg3);
			g.drawArc((int)joint3.x, (int)joint3.y, (int)joint3.width, (int)joint3.height, (int)joint3.start, (int)joint3.extent);
			
			g2d.draw(claw1);
			g2d.draw(claw2);
			
			currentTransform3 = g2d.getTransform();
			g2d.setTransform(saved);
		}
	}
	
	public Robot(){
		clawStatus = true;
		b = new Body(new Rectangle(355, 100, 100, 150), new Arc2D.Double(355, 60, 100, 80, 0, 180, 0));
		rotationPoint = new Point((2*b.recBody.x+b.recBody.width)/2, (2*b.recBody.y+b.recBody.height)/2);
		
		Polygon seg1 = new Polygon();
		Polygon seg2 = new Polygon();
		Polygon seg3 = new Polygon();
		Arc2D.Double joint1 = new Arc2D.Double(348, 120, 15, 15, 0, 360, 0);
		Arc2D.Double joint2 = new Arc2D.Double(288, 122, 15, 15, 0, 360, 0);
		Arc2D.Double joint3 = new Arc2D.Double(260, 150, 15, 15, 0, 360, 0);
		
		seg1.addPoint(290, 115);
		seg1.addPoint(300, 140);
		seg1.addPoint(355, 140);
		seg1.addPoint(355, 115);
		
		seg2.addPoint(290, 115);
		seg2.addPoint(300, 140);
		seg2.addPoint(278, 160);
		seg2.addPoint(255, 150);
		
		seg3.addPoint(255, 150);
		seg3.addPoint(278, 160);
		seg3.addPoint(278, 210);
		seg3.addPoint(255, 210);

		a = new Arm( seg1, seg2, seg3, joint1, joint2, joint3, new Rectangle(255, 210, 8, 20), new Rectangle(270, 210, 8, 20));
		

	}
	
	public void moveLeft(){
		rotationPoint.x--;
		a.moveLeft();
		b.moveLeft();
	}
	
	public void moveRight(){
		rotationPoint.x++;
		a.moveRight();
		b.moveRight();
	}
	
	public void moveUp(){
		rotationPoint.y--;
		a.moveUp();
		b.moveUp();
	}
	
	public void moveDown(){
		rotationPoint.y++;
		a.moveDown();
		b.moveDown();
	}

	public void rotateLeft(){
		a.rotateLeft();
		b.rotateLeft();
	}
	
	public void rotateRight(){
		a.rotateRight();
		b.rotateRight();
	}
	
	public void moveClaw(){
		clawStatus = !clawStatus;
		a.moveClaw(clawStatus);
		for ( int i = SceneGraph.junk.size() - 1; i >= 0; i--){
			if(a.intersectsLine(SceneGraph.junk.get(i))){
				SceneGraph.junk.remove(i);
			}
		}
	}
	
	public void closeClaw(){
		if(!clawStatus){
			a.moveClaw(true);
			clawStatus = !clawStatus;
		}
	}
	
	public void rotateSegLeft(int segNumber){
		a.rotateSegLeft(segNumber);
		Point p;
		if(segNumber == 1){
			p = new Point((int)a.joint1.getCenterX(), (int)a.joint1.getCenterY());
		}else if( segNumber == 2){
			p = new Point((int)a.joint2.getCenterX(), (int)a.joint2.getCenterY());
		}else if ( segNumber == 3){
			p = new Point((int)a.joint3.getCenterX(), (int)a.joint3.getCenterY());
		}else{
			p = rotationPoint;
		}
	}
	
	public void rotateSegRight(int segNumber){
		a.rotateSegRight(segNumber);
		Point p;
		if(segNumber == 1){
			p = new Point((int)a.joint1.getCenterX(), (int)a.joint1.getCenterY());
		}else if( segNumber == 2){
			p = new Point((int)a.joint2.getCenterX(), (int)a.joint2.getCenterY());
		}else if ( segNumber == 3){
			p = new Point((int)a.joint3.getCenterX(), (int)a.joint3.getCenterY());
		}else{
			p = rotationPoint;
		}

	}
	
	public boolean collides(){
		for ( int i = SceneGraph.junk.size() - 1; i >= 0; i--){
			if(a.collides(SceneGraph.junk.get(i)) || b.collides(SceneGraph.junk.get(i))){
				System.out.println("COLLIDED");
				return true;
			}
		}
		return false;
	}
	public void paint(Graphics g) {
		a.paint(g);
		b.paint(g);
	}
}
