import java.awt.Container;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Driver extends JFrame{
	
	SceneGraph sg;
	boolean rotationFlag;
	int movementFlag;		//0 for body, 1 for segment1, 2 for segment 2, 3 for segment 3
	boolean collisionFlag;
	
	protected KeyListener movementKeyListener = new KeyListener(){

		@Override
		public void keyPressed(KeyEvent k) {
			if(k.getKeyChar() == 'r' || k.getKeyChar() == 'R'){
				rotationFlag = true;
				movementFlag = 0;
			}else if(k.getKeyChar() == 'm' || k.getKeyChar() == 'M'){
				rotationFlag = false;
			}else if(KeyEvent.getKeyText(k.getKeyCode()).equals("Up")){
				if(rotationFlag){
				}else{
					if(!sg.robot.collides() ){
						sg.robot.moveUp();
					}else{
						Toolkit.getDefaultToolkit().beep();
						JOptionPane.showMessageDialog(null, "Your robot collided with space junk and it exploded! Try again");
						sg.robot = new Robot();
					}
					repaint();
				}
			}else if(KeyEvent.getKeyText(k.getKeyCode()).equals("Down")){
				if(rotationFlag){
				}else{
					if(!sg.robot.collides()){
						sg.robot.moveDown();
					}else{
						Toolkit.getDefaultToolkit().beep();
						JOptionPane.showMessageDialog(null, "Your robot collided with space junk and it exploded! Try again");
						sg.robot = new Robot();
					}
					repaint();
				}
			}else if(KeyEvent.getKeyText(k.getKeyCode()).equals("Left")){
				if(!sg.robot.collides()){
					if(!rotationFlag){
							sg.robot.moveLeft();
					}else{
						if(movementFlag == 0){
							sg.robot.rotateLeft();
						}else{
							sg.robot.rotateSegLeft(movementFlag);
						}
					}
				}else{
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, "Your robot collided with space junk and it exploded! Try again");
					sg.robot = new Robot();
				}
				
				repaint();
			}else if(KeyEvent.getKeyText(k.getKeyCode()).equals("Right")){
				if(!sg.robot.collides()){
				if(!rotationFlag){
					sg.robot.moveRight();
				}else{
					if(movementFlag == 0){
						sg.robot.rotateRight();
					}else{
						sg.robot.rotateSegRight(movementFlag);
					}
				}
				}else{
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, "Your robot collided with space junk and it exploded! Try again");
					sg.robot = new Robot();
				}
				repaint();
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	protected MouseListener mouseListener = new MouseListener(){

		@Override
		public void mouseClicked(MouseEvent e) {
	
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
			// To offset the menu
			Point p = e.getPoint();
			p.x = p.x - 9;
			p.y = p.y - 30;
			
			// TODO Auto-generated method stub
			if (sg.robot.b.contains(p)){
				rotationFlag = false;
				movementFlag = 0;
			}else if(sg.robot.a.contains(p)){
				if((sg.robot.a.isClaw(p))){
					sg.robot.moveClaw();
					repaint();
				}else{
					movementFlag = sg.robot.a.segmentNumber(p);
					rotationFlag = true;
				}
			}else{
				if(movementFlag != 0){
					if(e.getButton() == e.BUTTON3){
						if(!sg.robot.collides()){
						sg.robot.rotateSegLeft(movementFlag);
						}else{
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "Your robot collided with space junk and it exploded! Try again");
							sg.robot = new Robot();
						}
						repaint();
					}else if(e.getButton() == e.BUTTON1){
						if(!sg.robot.collides()){
							sg.robot.rotateSegRight(movementFlag);
						}else{
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "Your robot collided with space junk and it exploded! Try again");
							sg.robot = new Robot();
						}
						repaint();
					}
				}
			}
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			sg.robot.closeClaw();
			repaint();
			
		}
		
	};
	
	protected MouseWheelListener mouseWheelListener = new MouseWheelListener(){

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			// TODO Auto-generated method stub
			if(e.getWheelRotation() < 0 ){
				if(movementFlag == 0){
					sg.robot.rotateLeft();
				}else{
					sg.robot.rotateSegLeft(movementFlag);
				}
			}else if(e.getWheelRotation() > 0){
				if(movementFlag == 0){
					sg.robot.rotateRight();
				}else{
					sg.robot.rotateSegRight(movementFlag);
				}
			}
			repaint();
		}
	};
	
	/**
	Constructor for the Driver.  Sets the title, the size, displays the window, handles
	window closing, and calls 2 methods to do layout and activate listeners.
	*/
	public Driver(){
		rotationFlag = false;
		collisionFlag = false;
		movementFlag = 0;
		this.setTitle("Robot Training");
		this.setSize(800, 500);
		Container c = this.getContentPane();
		sg = new SceneGraph();
		c.add(sg);
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		activateListeners();
	}
	
	
	public void activateListeners(){
		this.addKeyListener(movementKeyListener);
		this.addMouseListener(mouseListener);
		this.addMouseWheelListener(mouseWheelListener);
	}
	
	/**
	Instantiate and show GUI in a thread-safe manner.
	*/
	public static void main (String args[]){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Driver driver = new Driver();
			}
		});
	}
	
	
}