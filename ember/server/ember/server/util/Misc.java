package ember.server.util;

/**
 * Weird and wonderful, and just useful stuff
 */
public class Misc {

	/**
	 * Integer representation of the direction when the given difference in coords happens.
	 * This is when the original position is subtracted from the new (target).
	 * @param diffX
	 * @param diffY
	 * @return
	 */
	public static int direction(int diffX, int diffY) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * The given direction between the two points.
	 * @param x
	 * @param y
	 * @param tgtX
	 * @param tgtY
	 * @return
	 */
	public static int direction(int x, int y, int tgtX, int tgtY) {
		return direction(tgtX-x, tgtY-y);
	}

	//Randoms, constants, methods - whatever.


}
