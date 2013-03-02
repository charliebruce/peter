

import ember.engine.maths.Matrix2f;
import ember.engine.maths.Vector2f;

/**
 * Test maths
 * @author charlie
 *
 */
public class Maths2DTest {

	public static Vector2f nullVec2f = new Vector2f(0,0);
	
	public static void main(String[] args){
		
		System.out.println("Vector Maths Test");
		
		Vector2f test1 = new Vector2f (1,1);
		
		System.out.println("Test1 is equal to nullVec? " + test1.sameAs(nullVec2f));
		System.out.println("SelfEqual? " + test1.sameAs(test1));
		
		System.out.println("Magnitude of Test1 = " + test1.getMagnitude());
		System.out.println("Direction of test1 = " + test1.getDirection());
		
		Matrix2f in = new Matrix2f(45,22,39,43);
		Matrix2f inverse  = in.invert();
		
		
		Vector2f initial = new Vector2f (38,23);
		Vector2f transformed = in.apply(initial);
		Vector2f out = inverse.apply(transformed);
		System.out.println(in.toString());
		System.out.println(inverse.toString());
		
		System.out.println("Testing equality of inversion. " + out.sameAs(initial) +" because "+ initial.toString() + " becomes " + out.toString());
	}
}
