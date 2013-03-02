package ember.client.game;

import ember.client.Misc;
import ember.engine.graphics.EmberGraphics;
import ember.engine.maths.Vector2f;

public class Viewport extends ScreenSpaceRegion {

	public static int width, height; //The dimensions of the "window on the world"
	
	public static Vector2f screenPoint; //The absolute position (screen-space) of the top-left of the viewpoint.
	
	public void initialise(){
		resize();
		screenPoint = new Vector2f();
	}
	
	public void resize(){
		width = EmberGraphics.gameCanvas.getWidth();
		height = EmberGraphics.gameCanvas.getHeight();
	}
	
	/**
	 * If the specified area is at all visible from the viewport then this returns true.
	 * @param b
	 * @return
	 */
	public boolean containsAtAll(ScreenSpaceBox b){
		return Misc.containsAtAll(this,b);
	}
	
	/**
	 * If the specified area is entirely visible from the viewport then this returns true.
	 * @param b
	 * @return
	 */
	public boolean containsEntirely(ScreenSpaceBox b){
		return Misc.containsEntirely(this, b);
	}
}
