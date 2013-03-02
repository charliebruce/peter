package ember.engine.graphics.sw;

import java.awt.Canvas;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import ember.engine.graphics.AbstractGraphicsBuffer;
import ember.engine.graphics.EmberCanvas;
import ember.engine.graphics.GraphicsBuffer;
import ember.engine.graphics.GraphicsToolkit;
import ember.engine.graphics.Sprite;

public class SWToolkit extends GraphicsToolkit {
	
	private GraphicsBuffer graphicsBuffer;
	private EmberCanvas canvas;

	public SWToolkit(Canvas target) {
		canvas = (EmberCanvas) target;
		graphicsBuffer = new GraphicsBuffer();
		graphicsBuffer.init(canvas);
	}

	@Override
	public void flip() {
		// TODO Flip buffer to canvas

		canvas.flip();
		
		//TODO return to old buffer deal?
		
		
		
		/*if (canvas == null || graphicsBuffer == null) {
			throw new IllegalStateException("Cannot flip null object.");
		}
		try {
			
			java.awt.Graphics g = canvas.getBufGraphics();
			graphicsBuffer.draw(0, 0, g);
			canvas.flip();
			Toolkit.getDefaultToolkit().sync();
		}
		catch (Exception _ex) {
			canvas.repaint();
		}*/
	}

	@Override
	public AbstractGraphicsBuffer getBuffer() {
		return graphicsBuffer;
	}

	@Override
	public void handleResize() {
		graphicsBuffer.init(canvas);
	}

	@Override
	public Sprite createSprite(int i, int j) {
		//TODO make me accept pix
		//and transparent/std distinction
		return new TransparentSprite(this, null, i,j);
	}

	@Override
	public EmberCanvas getCanvas() {
		return canvas;
	}

}
