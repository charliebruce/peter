package magma.client.graphics.opengl;

import importers.Model;
import importers.ObjConvert;

import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

public class GLExperimentation {


	static float[] identity4 = new float[]{1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
	
	static float[] identity3 = new float[]{1,0,0,0,1,0,0,0,1};
	
	public static int loadCubeIntoAVbo(GL2 gl){
		
		
		


		int[] target = new int[1];
		gl.glGenBuffers(1, target, 0);
		
		//Winding a cube such that it can be drawn as triangles - triangle strip this bitch later? 
		
		//DEFECTIVE!!!!!
        float[] data = new float[]{0,0,0,0,1,0,1,0,0/*123*/,0,1,0,1,0,0,1,1,0/*234*/,1,0,0,1,1,0,1,0,1/*345*/,1,1,0,1,0,1,1,1,1/*456*/,1,0,1,1,1,1,0,0,1/*567*/,1,1,1,0,0,1,0,1,1/*678*/,0,0,1,0,1,1,0,0,0/*781*/,0,1,1,0,0,0,0,1,0/*812*//*Going up*/,0,1,0,0,1,1,1,1,1/*286?*/,0,1,0,1,1,0,1,1,1/*246?*/,1,0,0,1,0,1,0,0,1/*357?*/,1,0,0,0,0,0,0,0,1/*317*/};
        
        //Correct according to System.out.println("Sizeof data is "+data.length);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, target[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, data.length*sizeof("float"),FloatBuffer.wrap(data), GL.GL_STATIC_DRAW);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
		
        gl.glEnable(GL.GL_ARRAY_BUFFER);
        
       return target[0];
       
	}
	//Emulates C sizeof function 
	//Put in quotes though ie sizeof("float")*10;
	public static int sizeof(String type){
		if (type.equals("float"))return 4;
		return 0;
	}

	public static float[] rotationx(long rot) {
		double ang =Math.toRadians(rot);
		//System.out.println("ROT:"+ang+"FROM"+rot);
		return new float[]{1,0,0,0,0,(float) Math.cos(ang),(float) Math.sin(ang),0,0,(float) (-1*Math.sin(ang)),(float) Math.cos(ang),0,0,0,0,1};
	}
	public static float[] rotationz(long rot) {
		double ang =Math.toRadians(rot);
		//System.out.println("ROT:"+ang+"FROM"+rot);
		return new float[]{(float) Math.cos(ang),(float) Math.sin(ang),0,0,(float) -Math.sin(ang),(float) Math.cos(ang),0,0,0,0,1,0,0,0,1};
	}
	public static float[] rotationy(long rot) {
		double ang =Math.toRadians(rot);
		//System.out.println("ROT:"+ang+"FROM"+rot);
		return new float[]{(float) Math.cos(ang),0,(float) ((float)-1*Math.sin(ang)),0,0,1,0,0,(float) Math.sin(ang),0,(float) Math.cos(ang),0,0,0,0,1};
	}



	
}
