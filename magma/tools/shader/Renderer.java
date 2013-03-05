package shader;

import java.awt.Font;
import java.nio.FloatBuffer;

import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLPipelineFactory;
import javax.media.opengl.GLProfile;
import javax.media.opengl.GLUniformData;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.GLArrayDataClient;
import com.jogamp.opengl.util.PMVMatrix;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;
import com.jogamp.opengl.util.glsl.ShaderUtil;

public class Renderer implements GLEventListener {

	long frames = 0;
	ShaderState[] shaders;
	PMVMatrix proj;
	
	@Override
	public void display(GLAutoDrawable arg0) {
		frames++;
		  if(null==shaders[0]) return;

	        GL2ES2 gl = arg0.getGL().getGL2ES2();

	        shaders[0].glUseProgram(gl, true);

	        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);

	        // One rotation every four seconds
	        proj.glMatrixMode(proj.GL_MODELVIEW);
	        proj.glLoadIdentity();
	        proj.glTranslatef(0, 0, -10);
	       // float ang = ((float) window.getDuration() * 360.0f) / 4000.0f;
	        float ang = (float) frames/5000;
	        proj.glRotatef(ang, 0, 0, 1);
	        proj.glRotatef(0/*ang*/, 0, 1, 0);

	        GLUniformData ud = shaders[0].getUniform("mgl_proj");
	        if(null!=ud) {
	            // same data object
	            shaders[0].glUniform(gl, ud);
	        } 

	        // Draw a square
	        gl.glDrawArrays(gl.GL_TRIANGLE_STRIP, 0, 4);


	        

	        shaders[0].glUseProgram(gl, false);
	        
	        
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable arg0) {
GL2ES2 gl = arg0.getGL().getGL2ES2();
        
	   
        System.out.println(Thread.currentThread()+" Entering initialization");
        System.out.println(Thread.currentThread()+" GL Profile: "+gl.getGLProfile());
        System.out.println(Thread.currentThread()+" GL:" + gl);
        System.out.println(Thread.currentThread()+" GL_VERSION=" + gl.glGetString(gl.GL_VERSION));
        System.out.println(Thread.currentThread()+" GL_EXTENSIONS:");
        System.out.println(Thread.currentThread()+"   " + gl.glGetString(gl.GL_EXTENSIONS));
       // System.err.println(Thread.currentThread()+" swapInterval: " + swapInterval + " (GL: "+gl.getSwapInterval()+")");
        System.out.println(Thread.currentThread()+" isShaderCompilerAvailable: " + ShaderUtil.isShaderCompilerAvailable(gl));

        
        proj = new PMVMatrix();

        //shader = JoglShader.create(gl, TS.vert, TS.frag, true);
        
        shaders = new ShaderState[2];
        initShader(gl, "redsquare", 0);
        initShader(gl, "cc", 1);
 
        

        // Push the 1st uniform down the path 
        shaders[0].glUseProgram(gl, true);

        proj.glMatrixMode(proj.GL_PROJECTION);
        proj.glLoadIdentity();
        proj.glMatrixMode(proj.GL_MODELVIEW);
        proj.glLoadIdentity();

        if(!shaders[0].glUniform(gl, new GLUniformData("mgl_proj", 4, 4, proj.glGetPMvMatrixf()))) {
            throw new GLException("Error setting proj in shader: "+shaders[0]);
        }
        // Allocate vertex arrays
        GLArrayDataClient vertices = GLArrayDataClient.createGLSL(gl, "mgl_Vertex", 3, gl.GL_FLOAT, false, 4);
        {
            // Fill them up
            FloatBuffer verticeb = (FloatBuffer)vertices.getBuffer();
            verticeb.put(-2);  verticeb.put(  2);  verticeb.put( 0);
            verticeb.put( 2);  verticeb.put(  2);  verticeb.put( 0);
            verticeb.put(-2);  verticeb.put( -2);  verticeb.put( 0);
            verticeb.put( 2);  verticeb.put( -2);  verticeb.put( 0);
        }
        vertices.seal(gl, true);

        GLArrayDataClient colors = GLArrayDataClient.createGLSL(gl, "mgl_Color",  4, gl.GL_FLOAT, false, 4);
        {
            // Fill them up
            FloatBuffer colorb = (FloatBuffer)colors.getBuffer();
            colorb.put( 1);    colorb.put( 0);     colorb.put( 0);    colorb.put( 1);
            colorb.put( 0);    colorb.put( 0);     colorb.put( 1);    colorb.put( 1);
            colorb.put( 0);    colorb.put( 1);     colorb.put( 0);    colorb.put( 1);
            colorb.put( 0);    colorb.put( 1);     colorb.put( 0);    colorb.put( 1);
        }
        colors.seal(gl, true);
        
        
        // OpenGL Render Settings
        gl.glClearColor(0, 0, 0, 1);
        gl.glEnable(GL2ES2.GL_DEPTH_TEST);

        shaders[0].glUseProgram(gl, false);

        // Let's show the completed shader state ..
        System.out.println(Thread.currentThread()+" "+shaders[0]);
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		 if(null==shaders[0]) return;

	        GL2ES2 gl = arg0.getGL().getGL2ES2();

	        shaders[0].glUseProgram(gl, true);

	        // Set location in front of camera
	        proj.glMatrixMode(proj.GL_PROJECTION);
	        proj.glLoadIdentity();
	        proj.gluPerspective(45.0f, (float)arg3 / (float)arg4, 1.0f, 100.0f);
	        //pmvMatrix.glOrthof(-4.0f, 4.0f, -4.0f, 4.0f, 1.0f, 100.0f);

	        System.out.println("Resize" + arg1+","+arg2+","+arg3+","+arg4);
	        GLUniformData ud = shaders[0].getUniform("mgl_proj");
	        if(null!=ud) {
	            // same data object
	            shaders[0].glUniform(gl, ud);
	        } 

	        shaders[0].glUseProgram(gl, false);
	}
	
	
	
	  private void initShader(GL2ES2 gl, String name, int num) {
	        int tmpI;

	        // Create & Compile the shader objects
	        ShaderCode rsVp = ShaderCode.create(gl, GL2ES2.GL_VERTEX_SHADER, 1, Testbed.class,
	                                            "shaders", "shaders/bin", name);
	        ShaderCode rsFp = ShaderCode.create(gl, GL2ES2.GL_FRAGMENT_SHADER, 1, Testbed.class,
	                                            "shaders", "shaders/bin", name);

	        // Create & Link the shader program
	        ShaderProgram sp = new ShaderProgram();
	        sp.add(rsVp);
	        sp.add(rsFp);
	        if(!sp.link(gl, System.err)) {
	            throw new GLException("Couldn't link program: "+sp);
	        }

	        // Let's manage all our states using ShaderState.
	        shaders[num] = new ShaderState();
	        shaders[num].attachShaderProgram(gl, sp);
	        
	    }

	  
	  

}
