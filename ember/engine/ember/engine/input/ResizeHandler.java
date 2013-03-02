package ember.engine.input;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import ember.client.Client;
import ember.engine.graphics.EmberGraphics;
import ember.util.Logger;

public class ResizeHandler implements ComponentListener {

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		Logger.getInstance().info("Resized in CL " +arg0.getComponent().toString());
		if (!EmberGraphics.isFullscreen && Client.getInstance().runningAsApplet){
		EmberGraphics.gameCanvas.setSize(arg0.getComponent().getSize());
		}
		System.out.println("nw: " + arg0.getComponent().getSize().getWidth());
		EmberGraphics.handleResize();
		
		//TODO this handles only when resize is actually released.....
		
		
		//Client.getInstance().applet.validate();
		//Graphics.gameCanvas.getParent().validate();
		
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
