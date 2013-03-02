import ember.engine.maths.Matrix2f;
import ember.engine.maths.Vector2f;


public class MatrixMaths {

	public static void main (String[] args){

		Matrix2f a = new Matrix2f(3,4,4,5);
		Matrix2f b = new Matrix2f(-4,8,-2,5);
		Matrix2f c = new Matrix2f(3,-1,-6,2);
		Matrix2f d = new Matrix2f(3,-2,4,-3);
		
		Matrix2f inva = a.invert();
		Matrix2f invb = b.invert();
		Matrix2f invc = c.invert();
		Matrix2f invd = d.invert();
		
		Matrix2f cd = d.multiply(c);
		
		System.out.println("cd is:");
		System.out.println(cd);
		
		System.out.println("invcd is:");
		System.out.println(cd.invert());
		
		
		Matrix2f p = new Matrix2f(3,7,2,4);
		Vector2f oneone = new Vector2f(1,1);
		
		System.out.println(p.apply(oneone));
		
		System.out.println(p.invert());
		

		
		
		
	}
}
