import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import magma.logger.Log;


public class checker implements GLEventListener {

	@Override
	public void display(GLAutoDrawable arg0) {
		GL2GL3 gl = arg0.getGL().getGL2GL3();
		if(!gl.isExtensionAvailable("GL_ARB_vertex_buffer_object")){
			Log.crit("VBOs are not supported.");
		}
		else{
			Log.info("safe ");
		}
		
		if(		!gl.isExtensionAvailable("GL_ARB_shader_objects")&&
				!gl.isExtensionAvailable("GL_ARB_fragment_shader")&&
				!gl.isExtensionAvailable("GL_ARB_vertex_shader")){
			
			Log.crit("Shaders are not supported.");
		}
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		GL2GL3 gl = arg0.getGL().getGL2GL3();
		if(!gl.isExtensionAvailable("GL_ARB_vertex_buffer_object")){
			Log.crit("VBOs are not supported.");
		}
		else{
			Log.info("safe ");
		}
		
		if(		!gl.isExtensionAvailable("GL_ARB_shader_objects")&&
				!gl.isExtensionAvailable("GL_ARB_fragment_shader")&&
				!gl.isExtensionAvailable("GL_ARB_vertex_shader")){
			
			Log.crit("Shaders are not supported.");
		}
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub
		
	}

	
}
