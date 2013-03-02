package ember.client;

import ember.client.game.Location;
import ember.client.game.ScreenSpaceBox;
import ember.client.game.ScreenSpaceRegion;
import ember.client.game.entity.Entity;
import ember.engine.maths.Vector2f;

public class Misc {

	public static Vector2f screenToLocationVec = new Vector2f(10,50);//Should be based on map!
	
	public static Vector2f locationSquareIncrementX = new Vector2f(8,8);

	public static Vector2f locationSquareIncrementY = new Vector2f(8,-8);


	public static ScreenSpaceBox getForEntity(Entity e){
		

		Vector2f locationBase = locationToScreen(e.position);
		int z = e.height;
		int width = e.getSprite().getWidth();
		int height = e.getSprite().getHeight();
		
		//Calculated screen-space coordinates - this is the screen-space, absolute center of the grid square.
		int cx=(int) locationBase.vec[0];
		int cy=(int) locationBase.vec[1];
		
		
		//Adjustment for animation (the server doesn't care, he just does square by square, but the client does).....
		
		return new ScreenSpaceBox(cx,cy,width,height);	
	}

	/**
	 * Returns the absolute screen-space vector of the location.
	 * This does NOT account for animation since I don't know the entity's situation...
	 * @param l
	 * @return
	 */
	public static Vector2f locationToScreen(Location l){
		Vector2f incX;
		Vector2f incY;
		
		//Take the vector between each square and apply to the location given
		incX = locationSquareIncrementX.multiply(l.x);
		incY = locationSquareIncrementY.multiply(l.y);
		
		//System.out.println("VecCal - Location is "+l.x+","+l.y+" so adjusts by "+incX.toString()+","+incY.toString());
		
		//Incorporate the world to screen constant
		return screenToLocationVec.add(incX.add(incY));
	}
	
	
	
	/**
	 * Return true if the ObjectBoundBox (screen-space) is at all located inside the ScreenRegion (screenspace)
	 * @param s
	 * @param b
	 * @return
	 */
	public static boolean containsAtAll(ScreenSpaceRegion s, ScreenSpaceBox b) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Returns true if the ObjectBoundBox is entirely inside the ScreenRegion
	 * @param s
	 * @param b
	 * @return
	 */
	public static boolean containsEntirely(ScreenSpaceRegion s, ScreenSpaceBox b) {
		// TODO Auto-generated method stub
		return false;
	}
}
