package magma.client.graphics.opengl;

import javax.media.opengl.GL2;

import magma.client.graphics.Graphics;

public class GLFontRenderer {

	public void drawString(GL2 gl, String text, int x, int y, GLFont f){
		//Position to floating-point top-left
		float a = ((float)x*2.0f/(float)Graphics.camera.width)-1.0f;//Range -1 to 1
		float b = ((float)y*2.0f/(float)Graphics.camera.height)-1.0f;
		
		char[] t = text.toCharArray();
		int[] sumLengths = new int[t.length];
		sumLengths[0]=0;
		float[] vbodata = new float[8*t.length+8];//2n+2 points per character when triangle-stripping * 4 floats per point (xy,uv)
		for(int i = 0; i<t.length; i++){
			//For variable-width characters
			int len = f.getLength(t[i]);
			char current = t[i];
			if(i==0){	
				sumLengths[i]=len;
			}
			else{
				sumLengths[i] = sumLengths[i-1]+len;
			}
			//Start the polygon, it ranges from sumLengths[i-1]->sumLengths[i]
				/*Point 1*/
				vbodata[0]=a;
				vbodata[1]=b;
				vbodata[2]=f.texcoordu(t[i],false,false);
				vbodata[3]=f.texcoordv(t[i],false,false);
				/*Point 2*/
				vbodata[4]=a;
				vbodata[5]=b+((float)f.lineheight/(float)Graphics.camera.height);
				vbodata[6]=f.texcoordu(t[i], false, true);
				vbodata[7]=f.texcoordv(t[i], false, false);
			//Odd point (top)
			vbodata[8*i+8]=a+((float)sumLengths[i]/(float)Graphics.camera.width);
			vbodata[8*i+9]=b;
			
		
		
		
		}
	
	}
}
