package magma.client.input;

import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import magma.client.ClientStrapper;
import magma.logger.Log;

import com.jogamp.newt.ScreenMode;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.event.ScreenModeListener;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.event.WindowListener;
import com.jogamp.newt.event.WindowUpdateEvent;

public class GeneralListener implements WindowListener, KeyListener, MouseListener, ScreenModeListener, java.awt.event.KeyListener, java.awt.event.MouseListener, MouseWheelListener, MouseMotionListener {

	@Override
	public void mouseClicked(MouseEvent arg0) {
		Log.debug("Mouse Clicked "+arg0.getButton() +" at "+arg0.getX()+","+arg0.getY());
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
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
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		//Log.debug("Mouse Moved");
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		Log.debug("Mouse pressed");
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		KeyBinding.keyPressed(arg0.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		KeyBinding.keyReleased(arg0.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDestroyNotify(WindowEvent arg0) {
		Log.debug("WDN");
	}

	@Override
	public void windowDestroyed(WindowEvent arg0) {
		Log.debug("WD");
		//My window has been baleeted!
		ClientStrapper.notifyWindowDestroyed();
	}

	@Override
	public void windowGainedFocus(WindowEvent arg0) {
		Log.debug("WGF");
	}

	@Override
	public void windowLostFocus(WindowEvent arg0) {
		Log.debug("WLF");
	}

	@Override
	public void windowMoved(WindowEvent arg0) {
		Log.debug("WMov");
	}

	@Override
	public void windowRepaint(WindowUpdateEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowResized(WindowEvent arg0) {
		Log.debug("WRes");
	}

	@Override
	public void screenModeChangeNotify(ScreenMode arg0) {
		Log.debug("sMCN");
	}

	@Override
	public void screenModeChanged(ScreenMode arg0, boolean arg1) {
		Log.debug("SMC");
	}

	@Override
	public void keyPressed(java.awt.event.KeyEvent arg0) {
		KeyBinding.keyPressed(arg0.getKeyCode());
	}

	@Override
	public void keyReleased(java.awt.event.KeyEvent arg0) {
		KeyBinding.keyReleased(arg0.getKeyCode());
	}

	@Override
	public void keyTyped(java.awt.event.KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(java.awt.event.MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(java.awt.event.MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(java.awt.event.MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(java.awt.event.MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(java.awt.event.MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(java.awt.event.MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
