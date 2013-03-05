package magma.client.graphics.opengl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2;

import magma.client.Client;
import magma.client.graphics.Graphics;
import magma.logger.Log;

public class GLUtils {


	public static int loadFullscreenVBO(GL2 gl){

		//gl.glUseProgram(test.getShaderProgram());
		int[] target = new int[1];
		gl.glGenBuffers(1, target, 0);

		float[] data = new float[]{-1,1,1,1,-1,-1,1,-1};
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, target[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, 32/*4points*2variables*4bpfloat*/,FloatBuffer.wrap(data), GL.GL_STATIC_DRAW);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);

		gl.glEnable(GL.GL_ARRAY_BUFFER);

		return target[0];

		//gl.glUseProgram(0);
	}
	public static int loadDebugVBO(GL2 gl){

		//gl.glUseProgram(test.getShaderProgram());
		int[] target = new int[1];
		gl.glGenBuffers(1, target, 0);

		//-1 to 1
		float startx=(float) 0;//0
		float starty=(float) 0;//0
		float endx=(float) 1;
		float endy=(float) -1;
		float[] data = new float[]{startx,endy,endx,endy,startx,starty,endx,starty};
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, target[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, 32/*4points*2variables*4bpfloat*/,FloatBuffer.wrap(data), GL.GL_STATIC_DRAW);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);

		gl.glEnable(GL.GL_ARRAY_BUFFER);

		return target[0];

		//gl.glUseProgram(0);
	}
	public static void capture(GL2 gl){
		//byte requires 4
		int num = 3;//Bytes per pixel
		Buffer target = IntBuffer.allocate(num/*Correct?*/*Graphics.camera.width*Graphics.camera.height);
		gl.glReadPixels(0, 0, Graphics.camera.width,Graphics.camera.height,GL.GL_RGB, GL2.GL_UNSIGNED_INT, target);//ORIG = rgba, unsigned byte
		int[] arr = (int[])target.array();
		
		Log.info("Len is "+arr.length);//This indicates 4bytesperpixel
		//This fails

		BufferedImage img = new BufferedImage(Graphics.camera.width, Graphics.camera.height, BufferedImage.TYPE_INT_RGB);//TODO type

		java.awt.Graphics g = img.getGraphics();
		
		for (int w=0;w<Graphics.camera.width;w++){
			for(int h = 0; h<Graphics.camera.height;h++){
				int startindex = num*(h*Graphics.camera.width+w);
				//System.out.println(Graphics.camera.height-h);
				//img.setRGB(w,Graphics.camera.height-h-1,/*16777215-*/(arr[startdex+2]));
				//img.setRGB(w,h,1,1,arr[startdex],arr[startdex+1],arr[startdex+2]);
				Color c = new Color(arr[startindex]/*,arr[startindex+1],arr[startindex+2]*/);//TODO sort colours
				
				g.setColor(c);
				
				g.drawRect(w, Graphics.camera.height-h-1, 1, 1);
			}
		}



		try {
			ImageIO.write(img, "png", new File(Client.basedir+"data/out/cap.png"));
			Log.info("Captured frame to data/out/cap.png");
			
		} catch (IOException e) {
			e.printStackTrace();
			Log.warn("Could not capture image to data/out/cap.png");
		}
	}

	public static int[] createFBO(int width, int height, GL2 gl){
		if (!gl.isExtensionAvailable("GL_EXT_framebuffer_object")) {Log.err("FBOs are not supported.");return null;}

		int[] ids = new int[2];
		gl.glGenTextures(2,ids,0);

		int fbotex = ids[0];
		gl.glBindTexture(GL.GL_TEXTURE_2D, fbotex);
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);//LINEAR?
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);//commenting these kills speed?
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
		//gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_GENERATE_MIPMAP, GL.GL_TRUE); // automatic mipmap

		//WORKS	LDR gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA8, width, height, 0, GL2.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);

		int type = (Graphics.current.hdr?GL2.GL_RGBA32F:GL.GL_RGBA8);//RGBA16F
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, type, width, height, 0, GL2.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);

		//Test upscaling
		//gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL2.GL_RGBA32F, width/2, height/2, 0, GL2.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
		//gl.glBindTexture(GL.GL_TEXTURE_2D, 0);

		int depthtex = ids[1];
		gl.glBindTexture(GL.GL_TEXTURE_2D, depthtex);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_DEPTH_TEXTURE_MODE, GL2.GL_INTENSITY);//when read as tex interpret as luminance. can be alpha
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_TEXTURE_COMPARE_MODE, GL2.GL_COMPARE_R_TO_TEXTURE);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_TEXTURE_COMPARE_FUNC, GL2.GL_LEQUAL);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_DEPTH_COMPONENT24, width, height, 0, GL2.GL_DEPTH_COMPONENT, GL.GL_UNSIGNED_BYTE, null);



		int[] array = new int[1];
		gl.glGenFramebuffers(1, array, 0);
		int fbo = array[0];
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fbo);

		Log.debug("Generated FBO "+ fbo + " with Colour "+fbotex+" and Depth "+depthtex);

		//gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL.GL_DEPTH_COMPONENT32, width, height);
		//gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER, dbo);

		//WARNING TODO FIXME Resizes repeatedly should reuse FB - that fucks shit up otherwise.



		gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, GL.GL_TEXTURE_2D, fbotex, 0 );
		gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_TEXTURE_2D, depthtex, 0 );



		int status = gl.glCheckFramebufferStatus(GL.GL_FRAMEBUFFER);
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);

		boolean fine = false;
		if (status==GL.GL_FRAMEBUFFER_COMPLETE){fine = true;}
		if(!fine){Log.err("FBO failed!");}

		int[] out = new int[]{fbotex,depthtex,fbo};
		return out;
	}

	public static int[] createFBO(int width, int height, int depth, GL2 gl){
		if (!gl.isExtensionAvailable("GL_EXT_framebuffer_object")) {Log.err("FBOs are not supported.");return null;}

		int[] ids = new int[1];
		gl.glGenTextures(1,ids,0);

		int fbotex = ids[0];
		gl.glBindTexture(GL.GL_TEXTURE_2D, fbotex);
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);//commenting these kills speed?
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
		//gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_GENERATE_MIPMAP, GL.GL_TRUE); // automatic mipmap

		//WORKS	LDR gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA8, width, height, 0, GL2.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);

		int type = (Graphics.current.hdr?GL2.GL_RGBA32F:GL.GL_RGBA8);//RGBA16F
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, type, width, height, 0, GL2.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);

		//Test upscaling
		//gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL2.GL_RGBA32F, width/2, height/2, 0, GL2.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
		//gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
		int[] array = new int[1];
		gl.glGenFramebuffers(1, array, 0);
		int fbo = array[0];
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fbo);

		Log.debug("Generated FBO "+ fbo + " with Colour "+fbotex+" with depth as assigned: "+depth);

		//gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL.GL_DEPTH_COMPONENT32, width, height);
		//gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER, dbo);

		//WARNING TODO FIXME Resizes repeatedly should reuse FB - that fucks shit up otherwise.



		gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, GL.GL_TEXTURE_2D, fbotex, 0 );
		gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_TEXTURE_2D, depth, 0 );

		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);


		int status = gl.glCheckFramebufferStatus(GL.GL_FRAMEBUFFER);

		boolean fine = false;
		if (status==GL.GL_FRAMEBUFFER_COMPLETE){fine = true;}
		if(!fine){Log.err("FBO failed!");}

		int[] out = new int[]{fbotex,depth,fbo};
		return out;
	}
}
