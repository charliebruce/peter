package shader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.nio.Buffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLUniformData;
import javax.media.opengl.glu.GLU;

import magma.client.graphics.opengl.Shader;

import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

public class SSv1 implements GLEventListener {
	
	long frames=0;

	int width = 640, height=480;
	
	GLU glu;
	
	@Override
	public void init(GLAutoDrawable glad) {
		GL2ES2 gl = glad.getGL().getGL2ES2();
		glu = new GLU();
		initSettings(gl);

		loadTextures(gl);
		pushVBOs(gl);
		initFBO(gl);
		try {
			initShaders(gl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setMatrices(gl);
	}

	

	int[] vbos = new int[2];
	FloatBuffer verticeb;
	FloatBuffer fs;
	private void pushVBOs(GL2ES2 gl) {
		
		gl.glGenBuffers(2, vbos, 0);
		
		verticeb = FloatBuffer.allocate(12);		
        verticeb.put(-2);  verticeb.put(  2);  verticeb.put( 0);
        verticeb.put( 2);  verticeb.put(  2);  verticeb.put( 0);
        verticeb.put(-2);  verticeb.put( -2);  verticeb.put( 0);
        verticeb.put( 2);  verticeb.put( -2);  verticeb.put( 0);
        verticeb.flip();
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbos[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, verticeb.capacity()*4, verticeb, GL.GL_STATIC_DRAW);
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
		fs = FloatBuffer.allocate(12);		
        fs.put(-1);  fs.put(1);  fs.put(0);
        fs.put(-1);  fs.put(-1);  fs.put( 0);
        fs.put(1);  fs.put(-1);  fs.put( 0);
        fs.put(1); fs.put(1);  fs.put( 0);
        //fs.order(ByteOrder.nativeOrder());
        fs.flip();
        FloatBuffer fbw = FloatBuffer.wrap(new float[]{-1,1,0,-1,-1,0,1,-1,0,1,1,0});
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbos[1]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, fs.capacity()*4, fbw/*fw*/, GL.GL_STATIC_DRAW);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
		
		System.out.println("Made some VBOs: "+vbos[0] + " and "+vbos[1]);
		
		
		
	}



	@Override
	public void display(GLAutoDrawable glad) {
		GL2ES2 gl = glad.getGL().getGL2ES2();
		frames++;
		
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fbo);
		renderMainPass(gl);
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
		
		renderOffscreenPass(gl);
	}

	private void renderOffscreenPass(GL2ES2 gl){
		
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		gl.glUseProgram(test.getShaderProgram());
		gl.glEnableVertexAttribArray(vbos[1]);
		
		
		int locver = gl.glGetAttribLocation(test.getShaderProgram(), "vertex");
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbos[1]);
		System.out.println("Location is "+locver);
		
		gl.glVertexAttribPointer(locver, 3/*xyz*/, GL.GL_FLOAT, false, 0,0);
	
		gl.glDrawArrays(GL.GL_TRIANGLES/*strip*/, 0, 4);


		gl.glUseProgram(0);
		
	}
	private void renderMainPass(GL2ES2 gl) {
		
	
	}




	private void initSettings(GL2ES2 gl) {
		gl.glClearColor(0,0,0,1);
		
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		GL2ES2 gl = arg0.getGL().getGL2ES2();
		System.out.println("Resize: "+arg3+","+arg4);
		width = arg3;
		height = arg4;
		destroyFBO(gl);
		initFBO(gl);
		setMatrices(gl);
	}
	
	private void setMatrices(GL2ES2 gl) {
		// TODO Auto-generated method stub
		
		gl.glUseProgram(test.getShaderProgram());
//System.out.println("uniform locaiton is " + gl.glGetUniformLocation(test.getShaderProgram(), "vertex"));
		
		float[] matrix;
		matrix = new float[]{0.5f,0,0,0,0,0.5f,0,0,0,0,0.5f,0,0,0,0,1,0.5f,0,0,0,0,0.5f,0,0,0,0,0.5f,0,0,0,0,1};
		float[] matrix1;//columnmajor
		matrix1 = new float[]{1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
		
        // Set location in front of camera
        //gl.glMatrixMode(GL2ES2.GL_PROJECTION);
        //gl.glLoadIdentity();
       // glu.gluPerspective(45.0f, (float)width / (float)height, 1.0f, 100.0f);
        //pmvMatrix.glOrthof(-4.0f, 4.0f, -4.0f, 4.0f, 1.0f, 100.0f);

        
        //int location = gl.glGetUniformLocation(test.getShaderProgram(), "transform");
        //System.out.println("Location of transform is "+location+ " being fed "+matrix.length + " values for matrix");
       
            // same data object
       // gl.glUniformMatrix4fv(location, 2, false, FloatBuffer.wrap(matrix));
        	

        

        gl.glUseProgram(0);

	}



	int fbotex;
	int fbo;
	int depth;
	
	private void initFBO(GL2ES2 gl) {
		if (!gl.isExtensionAvailable("GL_EXT_framebuffer_object")) {System.err.println("FBOs are not supported.");System.exit(1);}
		
		 gl.glBindTexture(GL.GL_TEXTURE_2D, fbotex);
		 gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		 gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		 //gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		 //gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
		 //gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_GENERATE_MIPMAP, GL.GL_TRUE); // automatic mipmap
		 gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA8, width, height, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
		 gl.glBindTexture(GL.GL_TEXTURE_2D, 0);

		 

		
	}
	private void destroyFBO(GL2ES2 gl) {
		if (!gl.isExtensionAvailable("GL_EXT_framebuffer_object")) {System.err.println("FBOs are not supported.");System.exit(1);}
		
		
		gl.glDeleteTextures(1, IntBuffer.wrap(new int[]{fbotex}));

	}
	
	
	String[] tex = {"data/test/chell.png"};//I die with anything but 3-letter extensions "png" "jpg" "tga" etc
	int[] ids = new int[tex.length];
	
	private void loadTextures(GL2ES2 gl) {
	
		gl.glGenTextures(tex.length, ids, 0);
		
		for (int i=0; i<tex.length; i++)
		try {
			File f = new File(tex[i]);
			
            TextureData data = TextureIO.newTextureData(gl.getGLProfile(), new File(tex[i]), true, tex[i].substring(tex[i].length()-3,tex[i].length()));
           
           //TODO BUG FIXME Uses a hack to convert RGB to BGR in the shader. SHIIIIIIIT.
            
            gl.glBindTexture(GL.GL_TEXTURE_2D, ids[i]);
            gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
            gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
            gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, data.getInternalFormat(), data.getWidth(), data.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, data.getBuffer());
            System.out.println("Loaded ."+ tex[i].substring(tex[i].length()-3,tex[i].length())+ " file " + f.getAbsolutePath()+ " into texture "+ids[i]);
			
		}
       catch (IOException exc) {
           exc.printStackTrace();
           System.exit(1);
       }

	}

	Shader test = new Shader();
	
	private void initShaders(GL2ES2 gl) throws IOException {
		
		test.load("test", new File("shaders/test.vp"), new File("shaders/test.fp"));
		test.init((GL2GL3) gl);
		

		
	}
	
	
	@Override
	public void dispose(GLAutoDrawable glad) {
		GL2ES2 gl = glad.getGL().getGL2ES2();
		gl.glDeleteFramebuffers(1, IntBuffer.wrap(new int[]{fbo}));
		gl.glDeleteTextures(tex.length, IntBuffer.wrap(ids));
		destroyFBO(gl);

		gl.glDeleteBuffers(1, IntBuffer.wrap(vbos));
		
	}


}
