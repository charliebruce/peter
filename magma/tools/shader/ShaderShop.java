package shader;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import magma.client.graphics.Graphics;
import magma.client.graphics.opengl.GLCamera;
import magma.client.graphics.opengl.HWCheck;
import magma.client.graphics.opengl.Shader;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

public class ShaderShop implements GLEventListener, KeyListener {
	
	long frames=0;

	int width = 640, height=480;
	
	float blend = 1f;
	
	
	@Override
	public void init(GLAutoDrawable glad) {
		GL2 gl = glad.getGL().getGL2();

		HWCheck.checkHardware(gl);
		initSettings(gl);

		loadTextures(gl);
		
		if(Graphics.hwspec.useFbo)
			initFBO(gl);
		
		try {
			initShaders(gl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pushVBOs(gl);
		setMatrices(gl);
		
	}

	

	int[] vbos = new int[2];
	FloatBuffer verticeb;
	FloatBuffer fs;
	private void pushVBOs(GL2 gl) {
		
		
		gl.glUseProgram(test.getShaderProgram());
		
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
        FloatBuffer fbw = FloatBuffer.wrap(new float[]{-1,-1,0,-1,1,0,1,-1,0,1,1,0});
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbos[1]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, fs.capacity()*4, fbw/*fw*/, GL.GL_STATIC_DRAW);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
		
		System.out.println("Made some VBOs: "+vbos[0] + " and "+vbos[1]);
		
		/*
		
		
		GLArrayDataClient vertices = GLArrayDataClient.createGLSL(gl, "vertex", 3, gl.GL_FLOAT, false, 4);
        {
            // Fill them up
            FloatBuffer verticeb = (FloatBuffer)vertices.getBuffer();
            verticeb.put( 0);  verticeb.put(  2);  verticeb.put( 0);
            verticeb.put( 2);  verticeb.put(  2);  verticeb.put( 0);
            verticeb.put( 0);  verticeb.put( -2);  verticeb.put( 0);
            verticeb.put( 2);  verticeb.put( -2);  verticeb.put( 0);
        }
        vertices.seal(gl, true);
		//*/
		
		
        gl.glEnable(GL.GL_ARRAY_BUFFER);
		gl.glUseProgram(0);
		
	}



	@Override
	public void display(GLAutoDrawable glad) {
		GL2 gl = glad.getGL().getGL2();
		if (shadersRequireReload){
			try {
				initShaders(gl);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		frames++;
		
		if(Graphics.hwspec.useFbo){
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fbo);
		renderMainPass(gl);
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
		}
		renderOutPass(gl);
	}

	private void renderOutPass(GL2 gl){
		
		
	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	
	gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbos[1]);
	
	//Enable the colour correction shader
	gl.glUseProgram(cc.getShaderProgram());
	
		
	//Configure the vertex buffer - draw triangle strip of this and you get FS quad.
	int vboLocation = gl.glGetAttribLocation(cc.getShaderProgram(), "vertex");
	gl.glVertexAttribPointer(vboLocation, 3/*xyz*/, GL.GL_FLOAT, false, 0,0);
	gl.glEnableVertexAttribArray(vboLocation);
		
	
	

	//Bind the input to the colour correction shader.
	int location = gl.glGetUniformLocation(cc.getShaderProgram(), "ccin");
	gl.glActiveTexture(GL.GL_TEXTURE0);
	if(!Graphics.hwspec.useFbo){
	gl.glBindTexture(GL.GL_TEXTURE_2D, texids[0]/**/);//THE TEXTURE TO RENDER FULLSCREEN
	}
	else {
		//gl.glBindTexture(GL.GL_TEXTURE_2D, fbotex );
		gl.glBindTexture(GL.GL_TEXTURE_2D, /*depthtex/**/fbotex/**/ );
	}
	gl.glUniform1i(location, 0);
	
	//Bind the colour correction ramp to the shader.
	location = gl.glGetUniformLocation(cc.getShaderProgram(), "rampa");
	gl.glActiveTexture(GL.GL_TEXTURE1);
	gl.glBindTexture(GL2.GL_TEXTURE_1D, texids1d[0]);
	gl.glUniform1i(location, 1);
   
	location = gl.glGetUniformLocation(cc.getShaderProgram(), "rampb");
	gl.glActiveTexture(GL.GL_TEXTURE2);
	gl.glBindTexture(GL2.GL_TEXTURE_1D, texids1d[1]);
	gl.glUniform1i(location, 2);
	
	location = gl.glGetUniformLocation(cc.getShaderProgram(), "blend");
	
	gl.glUniform1f(location, blend);
	
	//Finally draw.
	gl.glDrawArrays(GL.GL_TRIANGLE_STRIP/*strip*/, 0, 4);
	gl.glUseProgram(0);
		
	}
	private void renderMainPass(GL2 gl) {

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbos[1]);
		
		gl.glUseProgram(test.getShaderProgram());
		
		
		int locver = gl.glGetAttribLocation(test.getShaderProgram(), "vertex");
		
		//System.out.println("Location is "+locver);
		
		gl.glVertexAttribPointer(locver, 3/*xyz*/, GL.GL_FLOAT, false, 0,0);
	gl.glEnableVertexAttribArray(locver);
		
	int drawmode;
	boolean wireframe = false;
		if(wireframe){
			drawmode = GL.GL_LINE_STRIP;
		}
		else{
			drawmode = GL.GL_TRIANGLE_STRIP;
		}
		gl.glDrawArrays(drawmode/*strip*/, 0, 4);
		

		gl.glUseProgram(0);
	
	}




	private void initSettings(GL2 gl) {
		gl.glClearColor(0, 0, 0, 1);
        gl.glEnable(GL2.GL_DEPTH_TEST);

	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		GL2 gl = arg0.getGL().getGL2();
		System.out.println("Resize: "+arg3+","+arg4);
		width = arg3;
		height = arg4;
		destroyFBO(gl);
		initFBO(gl);
		setMatrices(gl);
	}
	
	GLCamera c = new GLCamera();
	private void setMatrices(GL2 gl) {
		// TODO Auto-generated method stub
		
		gl.glUseProgram(test.getShaderProgram());
//System.out.println("uniform locaiton is " + gl.glGetUniformLocation(test.getShaderProgram(), "vertex"));
		
		float[] matrix;
		matrix = new float[]{0.5f,0,0,0,0,0.5f,0,0,0,0,0.5f,0,/*xlate*/0,0,0,1};//,0.5f,0,0,0,0,0.5f,0,0,0,0,0.5f,0,0,0,0,1
		
		 // Set location in front of camera
        //gl.glMatrixMode(GL2.GL_PROJECTION);
        //gl.glLoadIdentity();
       // glu.gluPerspective(45.0f, (float)width / (float)height, 1.0f, 100.0f);
        //pmvMatrix.glOrthof(-4.0f, 4.0f, -4.0f, 4.0f, 1.0f, 100.0f);

        
		int location = gl.glGetUniformLocation(test.getShaderProgram(), "project");
        System.out.println("Location of projtransform is "+location+ " being fed "+c.projection().length + " values for matrix");
       
            // same data object
       gl.glUniformMatrix4fv(location, 1, false, FloatBuffer.wrap(c.projection()));
       
       
       location = gl.glGetUniformLocation(test.getShaderProgram(), "transform");
      gl.glUniformMatrix4fv(location, 1, false, FloatBuffer.wrap(matrix));
       	

        /*gluPerspective specifies a viewing frustum into the world coordinate system. In general, the aspect ratio in gluPerspective should match the aspect ratio of the associated viewport. For example, aspect = 2.0 means the viewer's angle of view is twice as wide in x as it is in y. If the viewport is twice as wide as it is tall, it displays the image without distortion.

The matrix generated by gluPerspective is multipled by the current matrix, just as if glMultMatrix were called with the generated matrix. To load the perspective matrix onto the current matrix stack instead, precede the call to gluPerspective with a call to glLoadIdentity.

*/
        gl.glUseProgram(0);

	}



	int fbotex;
	int depthtex;
	
	int fbo;
	int depth;
	int dbo;
	
	
	private void initFBO(GL2 gl) {
		if (!gl.isExtensionAvailable("GL_EXT_framebuffer_object")) {System.err.println("FBOs are not supported.");System.exit(1);}
		
		int[] array22 = new int[1];
	    IntBuffer ib22 = IntBuffer.wrap(array22);
	    gl.glGenTextures(1,ib22);
	    fbotex= array22[0];
		 gl.glBindTexture(GL.GL_TEXTURE_2D, fbotex);
		 gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);//commenting these kills speed?
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
		//gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_GENERATE_MIPMAP, GL.GL_TRUE); // automatic mipmap
	//WORKS	LDR gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA8, width, height, 0, GL2.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
	 gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL2.GL_RGBA32F, width, height, 0, GL2.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
	 //Test upscaling
	//	gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL2.GL_RGBA32F, width/2, height/2, 0, GL2.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
		
	 //gl.glBindTexture(GL.GL_TEXTURE_2D, 0);

		    ib22 = IntBuffer.wrap(array22);
		    gl.glGenTextures(1,ib22);
		    depthtex= array22[0];
			 gl.glBindTexture(GL.GL_TEXTURE_2D, depthtex);
			 gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
			 gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
			 gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
			 gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
			 gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_DEPTH_TEXTURE_MODE, GL2.GL_INTENSITY);
			 gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_TEXTURE_COMPARE_MODE, GL2.GL_COMPARE_R_TO_TEXTURE);
			 gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_TEXTURE_COMPARE_FUNC, GL2.GL_LEQUAL);
			 
			 gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_DEPTH_COMPONENT24, width, height, 0, GL2.GL_DEPTH_COMPONENT, GL.GL_UNSIGNED_BYTE, null);
			// gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_DEPTH_COMPONENT24, width/16, height/16, 0, GL2.GL_DEPTH_COMPONENT, GL.GL_UNSIGNED_BYTE, null);
			 
			 //gl.glBindTexture(GL.GL_TEXTURE_2D, 0);

			 
		 
		 
		 
		 int[] array = new int[1];
		    IntBuffer ib = IntBuffer.wrap(array);
		    gl.glGenFramebuffers(1, ib);
		 fbo = array[0];
		 gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fbo);
		
		 
		 
		/* int[] array2 = new int[1];
		    IntBuffer ib2 = IntBuffer.wrap(array2);
		    gl.glGenRenderbuffers(1, ib2);
		 dbo = array[0];
		 gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, dbo);
		 */
		 
		 
		 System.out.println("Generated FBO: "+ fbo + " and Depth NA "/*+dbo*/);
		 
		 
		 
		// gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL.GL_DEPTH_COMPONENT32, width, height);

		 
		// gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER, dbo);

		 //WARNING TODO FIXME Resizes repeatedly should reuse FB - that fucks shit up otherwise.
		 
		 
		 
		 gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, GL.GL_TEXTURE_2D, fbotex, 0 );
		
		 gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_TEXTURE_2D, depthtex, 0 );
			

		 
		 gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
		 int status = gl.glCheckFramebufferStatus(GL.GL_FRAMEBUFFER);
		 boolean fine = false;
		 if (status==GL.GL_FRAMEBUFFER_COMPLETE){fine = true;}
		 System.out.println("FBO Status is "+status+ " which is fine: "+fine+ " FUCK YEAH");
	}
	
	
	
	private void destroyFBO(GL2 gl) {
		if (!gl.isExtensionAvailable("GL_EXT_framebuffer_object")) {System.err.println("FBOs are not supported.");System.exit(1);}
		
		
		gl.glDeleteTextures(1, IntBuffer.wrap(new int[]{fbotex}));

	}
	
	
	String[] tex = {"data/test/"+"chell.png"/**//*"left_4_dead.jpg"/**/};//I die with anything but 3-letter extensions "png" "jpg" "tga" etc
	int[] texids = new int[tex.length];
	
	//0 is cc ramp for image. "cc
	String[] tex1d = {"data/ccprofiles/injured.bmp", "data/ccprofiles/blackbias.bmp", "data/ccprofiles/blueup.bmp"};
	//I die with anything but 3-letter extensions "png" "jpg" "tga" etc
	int[] texids1d = new int[tex1d.length];
	
	
	private void loadTextures(GL2 gl) {
	
		gl.glGenTextures(tex.length, texids, 0);
		
		for (int i=0; i<tex.length; i++)
		try {
			File f = new File(tex[i]);
			
            TextureData data = TextureIO.newTextureData(gl.getGLProfile(), f, true, tex[i].substring(tex[i].length()-3,tex[i].length()));
           
           //TODO BUG FIXME Uses a hack to convert RGB to BGR in the shader. SHIIIIIIIT.
            
            gl.glBindTexture(GL.GL_TEXTURE_2D, texids[i]);
            gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
            gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
            gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, data.getInternalFormat(), data.getWidth(), data.getHeight(), 0, GL2.GL_BGR, GL.GL_UNSIGNED_BYTE, data.getBuffer());
            System.out.println("Loaded ."+ tex[i].substring(tex[i].length()-3,tex[i].length())+ " file " + f.getAbsolutePath()+ " into texture "+texids[i]);
			
		}
       catch (IOException exc) {
           exc.printStackTrace();
           System.exit(1);
       }
		
		gl.glGenTextures(tex1d.length, texids1d, 0);
		
		for (int i=0; i<tex1d.length; i++)
		try {
			File f = new File(tex1d[i]);
			
            TextureData data = TextureIO.newTextureData(gl.getGLProfile(), f, true, tex1d[i].substring(tex1d[i].length()-3,tex1d[i].length()));
           
           //TODO BUG FIXME Uses a hack to convert RGB to BGR in the shader. SHIIIIIIIT.
            
            gl.glBindTexture(GL2.GL_TEXTURE_1D, texids1d[i]);
            gl.glTexParameterf(GL2.GL_TEXTURE_1D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
            gl.glTexParameterf(GL2.GL_TEXTURE_1D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
            gl.glTexImage1D(GL2.GL_TEXTURE_1D, 0, data.getInternalFormat(), data.getWidth(), 0/*border*/, GL2.GL_BGR, GL.GL_UNSIGNED_BYTE, data.getBuffer());
            System.out.println("Loaded 1D ."+ tex1d[i].substring(tex1d[i].length()-3,tex1d[i].length())+ " file " + f.getAbsolutePath()+ " into texture "+texids1d[i]);

		}
       catch (IOException exc) {
           exc.printStackTrace();
           System.exit(1);
       }

	}

	Shader test = new Shader();
	Shader cc = new Shader();

	private boolean shadersRequireReload;
	
	private void initShaders(GL2 gl) throws IOException {

		test.load("test", new File("data/shaders/test.vp"), new File("data/shaders/test.fp"));
		test.init(gl);

		test.printLogs(gl);
		cc.load("cc", new File("data/shaders/post/passthru2f.vp"), new File("data/shaders/post/cc.fp"));
		cc.init(gl);
		cc.link(gl);
		cc.validate(gl);
		cc.printLogs(gl);
		
		
		System.out.println("Test is " + test.getShaderProgram() + " frag "+ test.getFragProgram() + " vert "+test.getVertProgram());
		

		shadersRequireReload=false;
		
	}
	
	
	@Override
	public void dispose(GLAutoDrawable glad) {
		GL2 gl = glad.getGL().getGL2();
		gl.glDeleteFramebuffers(1, IntBuffer.wrap(new int[]{fbo}));
		gl.glDeleteTextures(tex.length, IntBuffer.wrap(texids));
		destroyFBO(gl);

		gl.glDeleteBuffers(1, IntBuffer.wrap(vbos));
		
	}



	@Override
	public void keyPressed(KeyEvent arg0) {
		System.out.println("Key: "+arg0.getKeyCode());
		char c = arg0.getKeyChar();
		System.out.println(c);
		
		if(arg0.getKeyCode()==61){//+
			blend = (float) (blend + 0.05);
		}
		if(arg0.getKeyCode()==45){//-
			blend = (float) (blend - 0.05);
		}
		if (blend > 1){
			blend = 1;
		}
		if (blend < 0){
			blend = 0;
		}
		if(arg0.getKeyCode()==123){
			//F12
			shadersRequireReload = true;
		}
	}



	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
