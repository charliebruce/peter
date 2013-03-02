package ember.engine.graphics;

import java.awt.Canvas;
import ember.engine.graphics.sw.SWToolkit;

public abstract class GraphicsToolkit {

	/**
	 * Create a new instance of a graphics toolkit.
	 * @param type
	 * @param target
	 * @return
	 */
	public GraphicsToolkit createToolkit(Canvas target){
		return new SWToolkit(target);
	}
	
	public abstract void flip();

	public abstract AbstractGraphicsBuffer getBuffer();

	public abstract void handleResize();

	public abstract Sprite createSprite(int i, int j);

	public abstract EmberCanvas getCanvas();
	
	
}
