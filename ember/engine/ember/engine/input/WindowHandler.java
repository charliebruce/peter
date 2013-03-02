package ember.engine.input;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import ember.util.Logger;

public class WindowHandler implements WindowListener {
	/**
	 * Handle window events. These run in AWTEvent queue and should be short-lived.
	 */
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		//Window (re)gains focus
		Logger.getInstance().info("windowActivated");
	}


	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		Logger.getInstance().info("windowClosed");
	}


	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		//User hits Close
		Logger.getInstance().info("windowClosing");
		System.exit(0);
	}


	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		//Loss of focus
		Logger.getInstance().info("windowDeactivated");
	}


	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		Logger.getInstance().info("windowDeiconified");
	}


	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		//Minimised - also triggers loss of focus
		Logger.getInstance().info("windowIconified");
	}


	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		Logger.getInstance().info("windowOpened");
	}
	

	
	

}
