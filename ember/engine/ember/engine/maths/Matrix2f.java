package ember.engine.maths;

public class Matrix2f {

	/*
	 *  | a   b |
	 *  | c   d |
	 */
	
	private float a;
	private float b;
	private float c;
	private float d;
	
	public Matrix2f(float tl, float tr, float bl, float br){

		a = tl;
		b = tr;
		c = bl;
		d = br;
	}
	
	
	public Vector2f apply(Vector2f input){
		//Matrix application 2D position vector.
		
		float top = a * input.vec[0] + b * input.vec[1];
		float bottom = c * input.vec[0] + d * input.vec[1];
		
		return new Vector2f(top, bottom);
	}
	
	public Matrix2f invert(){
		return new Matrix2f(d,(b*-1),(c*-1),a).multiplyScalar(1/(a*d-b*c));
	}
	
	public Matrix2f multiplyParticularPartByScalar(int i, int num) {
		switch (i){
		case 1:
			return new Matrix2f(a*num,b,c,d);
		case 2:
			return new Matrix2f(a,b*num,c,d);
		case 3:
			return new Matrix2f(a,b,c*num,d);
		case 4:
			return new Matrix2f(a,b,c,d*num);
		}
		return null;
	}

	/**
	 * This is matrix multiplication AB where I am A, and "other" is B
	 * @param other
	 * @return
	 */
	public Matrix2f multiply(Matrix2f other) {
		return new Matrix2f((a*other.a)+(b*other.c),(a*other.b)+(b*other.d),(c*other.a)+(d*other.c),(c*other.b)+(d*other.d));
	}

	
	public Matrix2f multiplyScalar(float num){
		return new Matrix2f(a*num, b*num, c*num, d*num);
	}
	
	public boolean isIdentity(){
		if ((a==1)&&(b==0)&&(c==0)&&(d==1)) 
			return true;
		return false;
	}
	public String toString(){
		return " |"+a+" "+b+"|\n |"+c+ " "+d+"|";
	}
}
