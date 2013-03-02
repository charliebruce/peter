package ember.client.game;

import ember.engine.maths.Vector2f;

/**
 * The screen-space absolute location of an object.
 * @author charlie
 *
 */
public class ScreenSpaceBox {

	public ScreenSpaceBox(Vector2f target, int width, int height) {
		x = (int) target.vec[0];
		y = (int) target.vec[1];
		w=width;
		h=height;
	}

	public ScreenSpaceBox(int cx, int cy, int width, int height) {
		x=cx;
		y=cy;
		w=width;
		h=height;
	}


	public int x,y,w,h;
	
	
}
