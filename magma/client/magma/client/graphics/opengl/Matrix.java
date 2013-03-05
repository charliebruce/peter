package magma.client.graphics.opengl;

import magma.logger.Log;

public class Matrix {

	
	

	/**
	 * Matrix multiplication
	 * result = a*b
	 * @param a
	 * @param b
	 * @return
	 */
	static float[] multMatrix(float[] a, float[] b) {
	 
	    float[] res = new float[16];
	 
	    for (int i = 0; i < 4; ++i) {
	        for (int j = 0; j < 4; ++j) {
	            res[j*4 + i] = 0.0f;
	            for (int k = 0; k < 4; ++k) {
	                res[j*4 + i] += a[k*4 + i] * b[j*4 + k];
	            }
	        }
	    }
	    return res;
	 
	}
	
	/**
	 * Transpose of a 3x3 matrix
	 * @param input
	 * @return
	 */
	public static float[] transpose3(float[] input){
		return new float[]{input[0],input[3],input[6],input[1],input[4],input[7],input[2],input[5],input[8]};
	}
	/**
	 * Transpose of a 4x4 matrix
	 * @param input
	 * @return
	 */
	public static float[] transpose4(float[] input){
		return new float[]{input[0],input[4],input[8],input[12],input[1],input[5],input[9],input[13],input[2],input[6],input[10],input[14],input[3],input[7],input[11],input[15]};
	}
	/**
	 * Determinant of a 3x3 matrix
	 * @param in
	 * @return
	 */
	public static float det3(float[] in){
		return in[0]*in[4]*in[8]-in[0]*in[5]*in[7]-in[1]*in[3]*in[8]+in[1]*in[5]*in[6]+in[2]*in[3]*in[7]-in[2]*in[4]*in[6];
	}
	
	/*
	 * UNKNOWN but believed to be adjoint? adj(m)
	 */
	public static float[] adj3(float[] m){
		return new float[]{(m[8]*m[4]-m[5]*m[7]),-1.0f*(m[8]*m[1]-m[7]*m[2]),m[5]*m[1]-m[4]*m[2]/*3rd cell*/,
				-1.0f*(m[8]*m[3]-m[6]*m[5]),m[8]*m[0]-m[6]*m[2],-1.0f*(m[5]*m[0]-m[3]*m[2]),
				m[7]*m[3]-m[6]*m[4],-1.0f*(m[7]*m[0]-m[6]*m[1]),m[4]*m[0]-m[3]*m[1]};//from dr-lex.be/random/matrix_inv.html
	}
	public static float[] mult3float(float scalar,float[] matrix){
		float[] ret = new float[9];
		for(int i = 0; i<9;i++){
			ret[i]=scalar*matrix[i];
		}
		return ret;
	}
	/**
	 * Returns the top-left 3x3 submatrix from this 4x4 one
	 */
	public static float[] submatrix3from4(float[] m){
		return new float[]{m[0],m[1],m[2],m[4],m[5],m[6],m[8],m[9],m[10]};
	}
	/**
	 * The inverse of the given 3*3 matrix
	 * ie so that MM^-1=I
	 * @param in
	 * @return
	 */
	public static float[] inverse3(float[] in){
		float detm = det3(in);
		if(detm==0.0f){
			Log.err("We tried to invert a non-invertible matrix!");
			return GLExperimentation.identity4;
		}
		//float[] mt=transpose3(in);
		float[] adjm=adj3(in);
		return mult3float((1.0f/detm),adjm);//TODO thisis (1.0f/det)*(adj(m))
	}
	
	/**
	 * This matrix is the transpose of the inverse of the 3×3 upper left sub matrix from the modelview matrix.
	 * @param modelview
	 * @return
	 */
	public static float[] normalmatrix(float[] modelview){
		return transpose3(inverse3(submatrix3from4(modelview)));
	}
	
	
	
	
	
	
	
	
	
		
	/**
	 * Decommissioned but works fine to test.
	 * @param args
	 */
	public static void main(String[] args){
		//Test WORKS
		float[] matrix = new float[]{1,0,5,2,1,6,3,4,0};
		float[] inverse = inverse3(matrix);
		System.out.println(inverse[0]+","+inverse[1]+","+inverse[2]+","+inverse[3]+","+inverse[4]+","+inverse[5]+","+inverse[6]+","+inverse[7]+","+inverse[8]);
	}
	
}
