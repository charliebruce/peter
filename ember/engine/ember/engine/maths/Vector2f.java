package ember.engine.maths;

import ember.util.Logger;


public class Vector2f {

	public float[] vec = new float[2];
	
	public Vector2f(){
		this.set(0,0);
	}
	public Vector2f(float i, float j){
		this.set(i,j);
	}
	public Vector2f(float[] i){
		this.set(i);
	}
	
	public void set(float i, float j) {
		vec[0] = i;
		vec[1] = j;
	}
	public void set(float[] i) {
		vec[0]=i[0];
		vec[1]=i[1];
		if (i.length>2){
			Logger.getInstance().warning("Vector set with too many numbers in the given array. Excess disregarded.");
		}
	}

	/**
	 * The following are calculated properties.
	 */
	
	
	public double getMagnitude() {
		return Math.sqrt(vec[0]*vec[0]+vec[1]*vec[1]);
	}

	public double getDirection() {
	//returns angles in degrees. TODO learn radians

		return 90-Math.toDegrees(Math.atan(vec[0]/vec[1]));
	}
	
	/**
	 * The following are comparison commands.
	 */
	
	/**
	 * Is the vector identical?
	 */
	public boolean sameAs(Vector2f other){
		return (
		other.vec[0] == vec[0] &&
		other.vec[1] == vec[1]
		);
	}
	
	/**
	 * Is the vector of the same direction?
	 * @param other
	 * @return
	 */
	public boolean sameDirection(Vector2f other){
		return false;//TODO bullcrap.
	}
	/**
	 * Is the vector of the same magnitude (but not necessarily direction)?
	 * @param other
	 * @return
	 */
	public boolean sameMagnitude(Vector2f other){
		return (other.getMagnitude() == getMagnitude());
	}
	

	/**
	 * The sum of this and the specified vector, as another vector. 
	 * @param v
	 * @return
	 */
	public Vector2f add(Vector2f v){
		//Returns a vector which is the sum of this vector and v
		return new Vector2f(v.vec[0]+vec[0], v.vec[1]+vec[1]);
	}
	
	public Vector2f multiply(int times){
		return new Vector2f(vec[0]*times, vec[1]*times);
	}
	
	
	public String toString(){
		return "[Vector2 "+vec[0]+","+vec[1]+"]";
	}
	
}
