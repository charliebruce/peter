package magma.client.graphics.opengl;

public class Vector {

	// res = a cross b;
	public static final float[] crossProduct3(float []a, float[] b) {
		float[]res = new float[3];
	    res[0] = a[1] * b[2]  -  b[1] * a[2];
	    res[1] = a[2] * b[0]  -  b[2] * a[0];
	    res[2] = a[0] * b[1]  -  b[0] * a[1];
	    return res;
	}
	 
	// Normalize a vec3
	public static final float[] normalize3(float[] a) {
	 
	    float mag = (float) Math.sqrt(a[0] * a[0]  +  a[1] * a[1]  +  a[2] * a[2]);
	 
	    return new float[]{a[0]/mag,a[1]/mag,a[2]/mag};
	}

	public static final float[] multByMatrix4(float[] v, float[] m) {
		float[] res = new float[4];
		res[0]=v[0]*m[0]+v[1]*m[4]+v[2]*m[8]+v[3]*m[12];
		res[1]=v[0]*m[1]+v[1]*m[5]+v[2]*m[9]+v[3]*m[13];
		res[2]=v[0]*m[2]+v[1]*m[6]+v[2]*m[10]+v[3]*m[14];
		res[3]=v[0]*m[3]+v[1]*m[7]+v[2]*m[11]+v[3]*m[14];
		
		return res;
	}
}
