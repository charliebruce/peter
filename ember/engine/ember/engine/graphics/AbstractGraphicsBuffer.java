package ember.engine.graphics;

import java.awt.Canvas;
import java.awt.Graphics;

public abstract class AbstractGraphicsBuffer {

	int width;
	int height;
	int[] pixels;
	
	abstract void init(Canvas canvas);

	abstract void clip(int x, int y, int width, int height, Graphics g);

	public abstract void draw(int i, int j, Graphics g);
	
	abstract Graphics getGraphics();

}
