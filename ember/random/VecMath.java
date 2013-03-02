import ember.engine.maths.Vector2f;


public class VecMath {

	public static void main (String[] args){
			Vector2f vec = new Vector2f(9,9);
			Vector2f vec2 = new Vector2f(9,9);
			
			System.out.println(vec.add(vec2).getMagnitude());
			
	}
}
