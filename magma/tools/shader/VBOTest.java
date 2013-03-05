package shader;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import magma.client.graphics.opengl.Shader;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class VBOTest implements GLEventListener, KeyListener {

	Shader test;
	
	@Override
	public void display(GLAutoDrawable arg0) {
		GL2GL3 gl = arg0.getGL().getGL2GL3();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		gl.glUseProgram(test.getShaderProgram());
		 
		 int vertexLoc = gl.glGetAttribLocation(test.getShaderProgram(), "vertex");


		 
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffers[0]);
		 gl.glVertexAttribPointer(vertexLoc, 2, GL.GL_FLOAT, false, 8/*strid4*2e*/, 0);
		 gl.glEnableVertexAttribArray(vertexLoc);
		 
		 gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buffers[1]);
		gl.glDrawElements(GL.GL_TRIANGLE_STRIP, 4/*count*/, GL.GL_UNSIGNED_INT, 0);
		// gl.glDrawArrays(GL.GL_TRIANGLE_STRIP, 0, 4);
		 gl.glDisableVertexAttribArray(buffers[0]);
		 gl.glUseProgram(0);
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		GL2GL3 gl = arg0.getGL().getGL2GL3();
		initShaders(gl);
		initBuffers(gl);
	}

	int[] buffers = new int[2];
	
	void initBuffers(GL2GL3 gl){
		gl.glGenBuffers(2, buffers, 0);
		
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffers[0]);
		FloatBuffer data = FloatBuffer.wrap(new float[]{-1,-1,1,-1,-1,1,1,1});
		data.rewind();
		gl.glBufferData(GL.GL_ARRAY_BUFFER, 4*data.capacity(), data, GL2.GL_STATIC_DRAW);
		
		
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buffers[1]);
		IntBuffer dat = IntBuffer.wrap(new int[]{0,1,2,3});//12
		dat.rewind();
		gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, 4*dat.capacity(), dat, GL2.GL_STATIC_DRAW);
		
		
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);	
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	void initShaders(GL2GL3 gl){
		test = new Shader();
		test.load("test", new File("data/shaders/proj.vp"), new File("data/shaders/flat.fp"));
		test.init(gl);
	}
	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
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
