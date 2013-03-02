package ember.engine.graphics;

import java.awt.Canvas;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;

import ember.client.Client;
import ember.engine.graphics.sw.SWToolkit;
import ember.engine.input.ResizeHandler;
/**
 * A piece of code which handles basic graphics work.
 * @author charlie
 *
 */
public class EmberGraphics {
	
	/**
	 * An object representing the physical display.
	 */
	public static Display display;
	
	/**
	 * The canvas to which the game is drawn via buffer.
	 */
	public static Canvas gameCanvas;
	
	/**
	 * Used when fullscreen is entered.
	 */
	public static Frame fullscreenFrame;
	
	/**
	 * The interface to the hardware. Will be extended further to 3D
	 */
	public static GraphicsToolkit currentToolkit;
	
	
	private static boolean resizing;

	public static boolean isFullscreen;

	public static int windowmode;
	
	public static Graphics getTarget(){
		while (true){
			//Only return when resizing finishes.
			if (!resizing) {
				//return currentToolkit.getBuffer().getGraphics();
				return currentToolkit.getCanvas().getBufGraphics();
			}
	}
	}
	public static void initialise(){
		resizing = false;
		isFullscreen = false;
		
		gameCanvas = new EmberCanvas(Client.getInstance());
		gameCanvas.setSize(800, 600);
		currentToolkit = new SWToolkit(gameCanvas);


		
		try {
			display = new Display();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Client.getInstance().kh.bind();
		
		if (Client.getInstance().runningInFrame) 
		{
			Client.getInstance().windowedFrame.setVisible(true);
			Client.getInstance().windowedFrame.add(gameCanvas);
			Client.getInstance().windowedFrame.validate();
		}
		else 
		{
			Client.getInstance().applet.add(gameCanvas);
		}

		((EmberCanvas)gameCanvas).setupBuffering();
		handleResize();
		//goFullscreen();
		
	}

	public static void goFullscreen(){
		if(!isFullscreen){
			if (Client.getInstance().runningInFrame){
				Client.getInstance().windowedFrame.setVisible(false);
				Client.getInstance().windowedFrame.removeAll();
			}
			else {
				Client.getInstance().applet.remove(gameCanvas);
			}
	
			
			fullscreenFrame = display.makeFullscreenFrame();
			//fsframe.addComponentListener(new ResizeHandler());
			display.goFullscreen(1024, 768, 60, 32, fullscreenFrame);
			fullscreenFrame.setAlwaysOnTop(true);
			fullscreenFrame.add(gameCanvas);
			gameCanvas.setSize(fullscreenFrame.getSize());
			gameCanvas.setVisible(true);
			
			fullscreenFrame.addComponentListener(new ResizeHandler());
			//gameCanvas.setLocation(100,100);
			//gameCanvas.getParent().validate();
			
			handleResize();
			
			gameCanvas.requestFocus();
			isFullscreen = true;
			}
		
	}
	public static void leaveFullscreen(){
		if(isFullscreen){
			fullscreenFrame.remove(gameCanvas);
			
	
			fullscreenFrame.setEnabled(false);
			fullscreenFrame.dispose();
			
			fullscreenFrame = null;
			isFullscreen = false;
			
			if (Client.getInstance().runningInFrame){
				
				gameCanvas.setSize(Client.getInstance().windowedFrame.getSize());
				Client.getInstance().windowedFrame.setVisible(true);
				Client.getInstance().windowedFrame.add(gameCanvas);
				Client.getInstance().windowedFrame.addComponentListener(new ResizeHandler());
			}
			else {
				
				gameCanvas.setSize(Client.getInstance().getSize());
				Client.getInstance().applet.add(gameCanvas);
				Client.getInstance().applet.validate();
				Client.getInstance().applet.addComponentListener(new ResizeHandler());
			}
			
			handleResize();
			gameCanvas.getParent().validate();
			gameCanvas.requestFocus();
		}
	}
	
	public static void handleResize(){
		resizing = true;
		
		System.out.println("Resizing. New canvas dim: " + gameCanvas.getSize().getWidth()+","+gameCanvas.getSize().getHeight());
		currentToolkit.handleResize();
		//TODO this - reeinit canvas and shits
		
		resizing = false;
	}
	
	public static Container getPresentContainer(){
		if (isFullscreen){
			return fullscreenFrame;
		}
		if (Client.getInstance().runningAsApplet){
			return Client.getInstance().applet;
		}
		return Client.getInstance().windowedFrame;
	}
	
	public static void doFlip() {
		currentToolkit.flip();
	}
}
