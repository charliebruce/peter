package ember.client.game;

/**
 * A point in GameSpace - X,Y only. Height is an offset from ground and is separate.
 * This refers to a grid square, and not a pixel-by-pixel space.
 * @author charlie
 *
 */
public class Location {

	public Location(int i, int j) {
		x=i;
		y=j;
	}

	public int x,y;
	
	
}
