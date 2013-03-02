package ember.engine.graphics;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class EmberCanvas extends Canvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Component canvas;
	private BufferStrategy bs;
	
	public void paint(Graphics g){
		EmberGraphics.handleResize();
		//canvas.paint(g);//TODO DB
		//System.out.println("Canvas paint");
	}
	public final void update(Graphics g) {
		//canvas.update(g);
		//System.out.println("Canvas update");
	}
	
	
	public void drawConsole(GraphicsToolkit tk){//TODO a global model for toolkits 
		
		
	}
	
	public Graphics getBufGraphics(){
		return bs.getDrawGraphics();
	}
	
	public void flip(){
		bs.show();
	}
	
	public void setupBuffering(){
		this.createBufferStrategy(2);
		bs = getBufferStrategy();
	}
	public EmberCanvas(Component c){
		canvas = c;
		
	}
		
		
}