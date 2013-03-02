package ember.engine.input;

import java.awt.Canvas;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import ember.engine.graphics.EmberGraphics;
import ember.util.Logger;

public class InputHandler implements KeyListener, FocusListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private Canvas canvas;
	
	public Point mouseLocation = new Point(0,0);
	
	public InputHandler(Canvas gameCanvas) {
		canvas = gameCanvas;
	}
	public void bind(){
		canvas.addKeyListener(this);
		canvas.addFocusListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addMouseWheelListener(this);

	}
	public void unbind() {
		canvas.removeKeyListener(this);
		canvas.removeFocusListener(this);
		canvas.removeMouseListener(this);
		canvas.removeMouseMotionListener(this);
		canvas.removeMouseWheelListener(this);

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		Logger.getInstance().info("Key Pressed: "+arg0.getKeyCode());
		if (arg0.getKeyCode() == 27){
			EmberGraphics.leaveFullscreen();
		}
		if (arg0.getKeyCode() == 10){
			EmberGraphics.goFullscreen();
		}
		if (arg0.getKeyCode() == 192){
			
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		Logger.getInstance().info("KeyReleased");
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		Logger.getInstance().info("KeyTyped");
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		Logger.getInstance().info("FocusGained");
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		Logger.getInstance().info("FocusLost");
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		Logger.getInstance().info("mouseWheelMoved");
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		Logger.getInstance().info("mouseDragged");
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//TODO this
		//Logger.getInstance().info("mouseMoved");
		mouseLocation = arg0.getPoint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		Logger.getInstance().info("mouseClicked");
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		Logger.getInstance().info("mouseEntered");
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		Logger.getInstance().info("mouseEntered");
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		Logger.getInstance().info("mousePressed");
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		Logger.getInstance().info("mouseReleased");
	}


}
