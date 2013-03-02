package ember.engine.graphics;

import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class Display {

	   public GraphicsDevice currentGraphicsDevice;
	   public DisplayMode theDisplayMode;
	    
	    public DisplayMode[] getDisplayModes() {
	    	return currentGraphicsDevice.getDisplayModes();
	    }
	    
	    public void makeFrameFullscreen(Frame arg1) {
	    	try {
	    	    currentGraphicsDevice.setFullScreenWindow(arg1);
	    	} catch (RuntimeException runtimeexception) {
	    	    
	    	}
	    }
	        
		  public void goFullscreen(int targetWidth, int targetHeight, int refresh, int targetBitDepth, Frame fullScreenFrame) 
		  {
			  theDisplayMode = currentGraphicsDevice.getDisplayMode();

			  if (theDisplayMode == null){
				  throw new NullPointerException();
			}
			
		    fullScreenFrame.setUndecorated(true);
		    fullScreenFrame.enableInputMethods(false);
		    makeFrameFullscreen(fullScreenFrame);
		    
		    if (0 == refresh) {
		    	int refreshRate = theDisplayMode.getRefreshRate();
		    	DisplayMode[] displaymodes = currentGraphicsDevice.getDisplayModes();
		    	boolean bool = false;
		    	
		    	for (int i = 0; i<displaymodes.length; i++) {
		    		if (targetWidth == displaymodes[i].getWidth()
		    		&& targetHeight == displaymodes[i].getHeight()
		    		&& targetBitDepth == displaymodes[i].getBitDepth()) 
		    			{
		    				int potentialRefreshRate = displaymodes[i].getRefreshRate();
		    				if (!bool || Math.abs(-refreshRate + potentialRefreshRate) < Math.abs(-refreshRate + refresh)) {
		    					refresh = potentialRefreshRate;
		    					bool = true;
		    				}
		    		}
			if (!bool)
			{
			    refresh = refreshRate;
		    }
	    }
	    currentGraphicsDevice.setDisplayMode(new DisplayMode(targetWidth, targetHeight, targetBitDepth, refresh));
		}
		    EmberGraphics.isFullscreen = true;
	}
	   
	public Display() throws Exception {
		
		GraphicsEnvironment graphicsenvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		currentGraphicsDevice = graphicsenvironment.getDefaultScreenDevice();
		
		if (!currentGraphicsDevice.isFullScreenSupported()) {
		    GraphicsDevice[] screendevices = graphicsenvironment.getScreenDevices();
		    
		    for (int i = 0; i<screendevices.length; i++) {
		    	
		    	GraphicsDevice currentDevice = screendevices[i];
				if (currentDevice !=null && currentDevice.isFullScreenSupported()) {
				    currentGraphicsDevice = currentDevice;
				    return;
				}
		    }
		    throw new Exception();
		}
	}
	
	
	public Frame makeFullscreenFrame(){
		Frame fullscreenFrame = (new Frame("Fullscreen"));
	    fullscreenFrame.setResizable(false);
	    fullscreenFrame.setAlwaysOnTop(true);//TODO is this ideal?
	   
	    return fullscreenFrame;
	}
	
}
