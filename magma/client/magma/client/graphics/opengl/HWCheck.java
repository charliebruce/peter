package magma.client.graphics.opengl;

import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2GL3;

import magma.client.graphics.Graphics;
import magma.logger.Log;

public class HWCheck {

	/*public static void checkHardware(GL2 gl){
		checkHardware(gl.getGL());
	}*/
	public static void checkHardware(GL gl){

		Log.info("GL Profile: "+gl.getGLProfile());
		Log.info("GL Version: " + gl.glGetString(GL.GL_VERSION));
		//System.out.println(Thread.currentThread()+"   " + gl.glGetString(gl.GL_EXTENSIONS));

		String str = gl.glGetString(GL2GL3.GL_SHADING_LANGUAGE_VERSION);
		String renderer = gl.glGetString(GL.GL_RENDERER);
		
		//Graphics.hwspec.shaderLangVer = Float.parseFloat(str);
		Log.debug("GL: Shader version is " + str);
		Log.debug("GL: Renderer is " + renderer);
	    
		if(!gl.isExtensionAvailable("GL_ARB_vertex_buffer_object")){
			Log.crit("VBOs are not supported.");
		}
		
		if(		!gl.isExtensionAvailable("GL_ARB_shader_objects")&&
				!gl.isExtensionAvailable("GL_ARB_fragment_shader")&&
				!gl.isExtensionAvailable("GL_ARB_vertex_shader")){
			
			Log.crit("Shaders are not supported.");
		}
		
		
		if (!gl.isExtensionAvailable("GL_ARB_vertex_buffer_object")){
			Log.crit("VBOs are not available.");
		}
		if(!gl.isExtensionAvailable("GL_ARB_texture_non_power_of_two")){
			Log.warn("Non-power-of-two textures are not supported.");
			Graphics.hwspec.nPoT = false;
			//Implied
			Graphics.hwspec.useFbo = false;
		}
		
		if(!gl.isExtensionAvailable("GL_EXT_framebuffer_object")){
			Log.warn("FBOs are not supported by the GL implementation. Certain effects involving alternate render targets are unavailable.");
			Graphics.hwspec.useFbo = false;
		}
		
		if(!gl.isExtensionAvailable("GL_EXT_geometry_shader4")){
			Log.warn("Geometry shaders are not supported by the GL implementation.");
			Graphics.hwspec.useGeom=false;
		}
		IntBuffer ib = IntBuffer.allocate(1);
		gl.glGetIntegerv(GL2GL3.GL_MAX_TEXTURE_IMAGE_UNITS, ib);
		Graphics.hwspec.maxTexUnits = ib.get(0);
		
		gl.glGetIntegerv(GL2GL3.GL_MAX_TEXTURE_SIZE, ib);
		Graphics.hwspec.maxTexRes = ib.get(0);
		

		gl.glGetIntegerv(GL2GL3.GL_MAX_COLOR_ATTACHMENTS,ib);
		Graphics.hwspec.maxClrAttachments = ib.get(0);
		
		gl.glGetIntegerv(GL2GL3.GL_MAX_DRAW_BUFFERS,ib);
		Graphics.hwspec.maxDrawBuffers = ib.get(0);
		
		Log.debug("Hardware check complete.");
		
    
		
	}
}
