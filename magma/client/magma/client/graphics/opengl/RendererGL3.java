package magma.client.graphics.opengl;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.HashMap;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import magma.client.Client;
import magma.client.graphics.Graphics;
import magma.logger.Log;

/**
 * The OpenGL renderer implementation in JOGL3
 * This can additionally support version 130+ of slang amongst other things
 * IE in,out,geom
 * @author Charlie
 * 
 */
public class RendererGL3 implements GLEventListener {

	//Map texture names to GL texture IDs, identified by their filenames
	//Other textures like FBOs and noise are NOT included

	private HashMap<String, Integer> texmap = new HashMap<String, Integer>();
	private HashMap<String, Integer> texmap1D = new HashMap<String, Integer>();
	
	private HashMap<String, Integer> shaders = new HashMap<String, Integer>();
	//Typically pixel shaders (non-post) will use these bindings:
	//TEXTURE0 albedo
	//TEXTURE1 normals (bump)
	
	//Could do specular, diffuse, too
	
	//Terrain will use 0-3-base textures
	//Water is a post shader so is fed a heightmap
	
	//Colour toning shader uses 0 as input image, 1-2 to assign colour ramps
	
	//Bokeh blur needs depth, sprite, image, blur parameters
	
	//Loaded shaders by name
	//Of interest are cccore, genericoverlay - used for menus
	
	//Some custom, special-purpose VBOs for limited purposes only - TODO combine into one to reduce binds?
	private int fullscreenVbo;//-1 to 1 quad when drawn as triangle strip
	private int debugVbo;//Bottom-right of screen or so
	private int cubeVbo;
	
	private int ccRampA, ccRampB;
	private float ccBlend=1f;//1f=100%a?

	
	private Shader deferred;
	private int[] deferredTargets;
	private int deferredFbo;
	private int sponza;
	
	public void generateRenderBuffers(GL3 gl){
		//Generate the buffers required for deferred lighting
	}
	
	public void loadCore(GL3 gl){
		//Load core resources - fonts, fullscreen quad, cursors, core shaders, basic textures
		fullscreenVbo = GLUtils.loadFullscreenVBO(gl);
		debugVbo = GLUtils.loadDebugVBO(gl);
		cubeVbo = GLExperimentation.loadCubeIntoAVbo(gl);
		sponza = GLExperimentation.loadSponzaIntoAVbo(gl);
		//Pre-load
		String[] coreTextures = new String[]{"data/test/1k.png", "data/test/chell.png"/*, "data/fonts/fixed.png"*/};
		loadTextures(coreTextures,gl);
		
		

		Shader s=new Shader();
		/*s.load("cccore",new File("data/shaders/post/passthru2f.vp"), new File("data/shaders/post/cc.fp"));//TODO relocate me to post
		s.init(gl);
		s.link(gl);
		
		gl.glUseProgram(s.getShaderProgram());
		s.setUniform1i(gl,"ccin",0);
		s.setUniform1i(gl,"rampa",1);
		s.setUniform1i(gl,"rampb",2);
		s.setUniform1f(gl,"blend",ccBlend);

		//s.link(gl);
		s.validate(gl);
		gl.glUseProgram(0);
		//s.printLogs(gl);
		shaders.put("cccore", s.getShaderProgram());
		
		//Generic  Project
		*/
		
		/*
		
		
		s=new Shader();
		s.load("passthrough",new File("data/shaders/post/passthru2f.vp"), new File("data/shaders/post/passthrough.fp"));//TODO relocate me to post
		s.init(gl);
		s.link(gl);
		gl.glUseProgram(s.getShaderProgram());
		s.setUniform1i(gl,"texin",0);
		//s.link(gl);
		s.validate(gl);
		gl.glUseProgram(0);
		//s.printLogs(gl);
		shaders.put("passthrough", s.getShaderProgram());
		
		s=new Shader();
		s.load("waterrefracting",new File("data/shaders/post/passthru2f.vp"), new File("data/shaders/waterrefract.fp"));
		s.init(gl);
		s.link(gl);
		gl.glUseProgram(s.getShaderProgram());
		s.setUniform1i(gl,"beneath",0);
		//s.link(gl);
		s.validate(gl);
		gl.glUseProgram(0);
		//s.printLogs(gl);
		shaders.put("waterrefracting", s.getShaderProgram());*/
		
		
		
		
		/*
		 * TESTING SHADERS
		 */
		s=new Shader();
		s.load("generic",new File("data/shaders/post/passthru2f.vp"), new File("data/shaders/post/ccflip.fp"));//TODO relocate me to post
		s.init(gl);
		s.link(gl);
		
		gl.glUseProgram(s.getShaderProgram());
		//s.setUniform1i(gl,"ccin",0);
		s.setUniform1i(gl, "vertex", 0);
		//s.link(gl);
		s.validate(gl);
		gl.glUseProgram(0);

		shaders.put("generic", s.getShaderProgram());
		
		
		
		/*
		 * DEFERRED PIPELINE SHADERS
		 */
		
		
		deferred=new Shader();
		deferred.load("deferred",new File("data/shaders/deferred/deferred.vp"), new File("data/shaders/deferred/deferred.fp"));
		deferred.init(gl);
		deferred.link(gl);
		gl.glUseProgram(deferred.getShaderProgram());
		//deferred.setUniform1i(gl,"albedo",0);//diffuse light channel THIS HANDLED WITH BINDBUFFERS
		//deferred.setUniform1i(gl,"normal",1);//surface normal
		//s.link(gl);
		deferred.validate(gl);
		gl.glUseProgram(0);
		//s.printLogs(gl);
		shaders.put("deferred", deferred.getShaderProgram());
		
		
		s=new Shader();
		s.load("illum",new File("data/shaders/deferred/illum.vp"), new File("data/shaders/deferred/illum.fp"));
		s.init(gl);
		s.link(gl);
		gl.glUseProgram(s.getShaderProgram());
		s.setUniform1i(gl,"data0",0);
		s.setUniform1i(gl,"data1",1);
		s.setUniform1i(gl,"data2",2);
		//s.link(gl);
		s.validate(gl);
		gl.glUseProgram(0);
		//s.printLogs(gl);
		shaders.put("illum", s.getShaderProgram());
		
		s=new Shader();
		s.load("dedebug",new File("data/shaders/deferred/dedebug.vp"), new File("data/shaders/deferred/dedebug.fp"));//TODO relocate me to post
		s.init(gl);
		s.link(gl);
		
		gl.glUseProgram(s.getShaderProgram());
		s.setUniform1i(gl,"data0",0);
		s.setUniform1i(gl,"data1",1);
		s.setUniform1i(gl,"data2",2);
		s.validate(gl);
		gl.glUseProgram(0);

		shaders.put("dedebug", s.getShaderProgram());
		
		ccRampA = loadTexture1D("data/ccprofiles/base.bmp", gl);
		ccRampB = loadTexture1D("data/ccprofiles/base.bmp", gl);
		
		
	}


	long framecount = 0;
	


	@Override
	public void display(GLAutoDrawable arg0) {
		framecount++;
		GL3 gl = arg0.getGL().getGL3();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		//Render the game
		
		
		/*
		 * TESTING IS BELOW
		 * 
		 */
		
		//Display loading screen
		gl.glUseProgram(shaders.get("generic"));
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, fullscreenVbo);
			
		//Configure the vertex buffer - draw triangle strip of this and you get FS quad.
		int vboLocation = gl.glGetAttribLocation(shaders.get("generic"), "vertex");
		gl.glVertexAttribPointer(vboLocation, 2, GL.GL_FLOAT, false, 0,0);//2=xy
		gl.glEnableVertexAttribArray(vboLocation);

		//Bind the input to the colour correction shader.
		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, texid("data/test/1k.png", gl));//THE TEXTURE TO RENDER to CTFBO
		//Already bound ccin to tex0 in setup
		
		//Finally draw.
		//gl.glDrawArrays(GL.GL_TRIANGLE_STRIP, 0, 4);
		gl.glUseProgram(0);

		
		
		
		
		/*
		 * PUT CHELL INTO THE DEFERRED BUFFER
		 */
		
		
		//gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, deferredFbo);
		
		//Display loading screen
		gl.glUseProgram(shaders.get("generic"));
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, fullscreenVbo);
			
		//Configure the vertex buffer - draw triangle strip of this and you get FS quad.
		vboLocation = gl.glGetAttribLocation(shaders.get("generic"), "vertex");
		//gl.glEnableClientState(GL3.GL_VERTEX_ARRAY);
		gl.glVertexAttribPointer(vboLocation, 2, GL.GL_FLOAT, false, 0,0);//2=xy
		gl.glEnableVertexAttribArray(vboLocation);

		//Bind the input to the colour correction shader.
		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, texid("data/test/chell.png", gl));//THE TEXTURE TO RENDER to CTFBO
		//Already bound ccin to tex0 in setup
		
		//Finally draw.
		//gl.glDrawArrays(GL.GL_TRIANGLE_STRIP, 0, 4);
		gl.glUseProgram(0);

		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
		
		
		//TODO think about how I want to do lighting, HDR, albedo etc!!!!!
		//Using stencil/render albedos and etc to scene then light all might be most efficient
		//TODO Stencils, position, motion vector encoded in texture?
		
		//Check if we're using FBOs and choose the appropriate method
		if(Graphics.current.useFbo){
			
			/*
			 * DEFERRED PIPELINE FTW?
			 */
			
			//Using the deferred pipeline - geometry rendered first, then lit with illum.
			//All mat params are stored here - albedo, specular, normals, reflectivity
			gl.glUseProgram(shaders.get("deferred"));
			//also stored in Shader deferred
			
			//Bind the multiple targets correctly and TODO clear buffers
			gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, deferredFbo);
			
			int[] buffers = {GL3.GL_COLOR_ATTACHMENT0, GL3.GL_COLOR_ATTACHMENT1, GL3.GL_COLOR_ATTACHMENT2};
			gl.glDrawBuffers(3, buffers, 0);
			//Now the textures are available to gl_FragData[0/1/2]
			
			//Send the matrices
			int location;
			location = gl.glGetUniformLocation(shaders.get("deferred"), "projection");
			//System.out.println("Location: "+location);
			gl.glUniformMatrix4fv(location, 1, false, Graphics.camera.projection(), 0);

			location = gl.glGetUniformLocation(shaders.get("deferred"), "camtransformation");
			//System.out.println("Location: "+location);
			
			gl.glUniformMatrix4fv(location, 1, false, Graphics.camera.transformation(), 0);
			
			location = gl.glGetUniformLocation(shaders.get("deferred"), "transformation");
			gl.glUniformMatrix4fv(location, 1, false, GLExperimentation.identity4/* GLCamera.multMatrix(GLExperimentation.rotationy(framecount),GLExperimentation.rotationx(framecount))*/, 0);
			
			gl.glClearColor(0.2f,0.2f,0.2f,0.0f);
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
			gl.glClearColor(0.0f,0.0f,0.0f,0.0f);
			
			//Enable Depth Test Func
			gl.glEnable(GL.GL_DEPTH_TEST);
			
			
			/*
			
			int stride = (3 + 3 + 4 + 2) * 4; // 3 for vertex, 3 for normal, 4 for colour and 2 for texture coordinates. * 4 for bytes
			//vert
			int offset = 0 * 4; // 0 as its the first in the chunk, i.e. no offset. * 4 to convert to bytes.
			gl.glVertexPointer(3, GL.GL_FLOAT, stride, offset);
			//nrm
			offset = 3 * 4; // 3 components is the initial offset from 0, then convert to bytes
			gl.glNormalPointer(GL.GL_FLOAT, stride, offset);
			//tex
			
			offset = (3 + 3 + 2) * 4;
			glTexCoordPointer(2, GL.GL_FLOAT, stride, offset);
			*/
			
			
			//Bind the TEST cube
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, sponza);//cubeVbo
			int vlocation;
			vlocation = gl.glGetAttribLocation(shaders.get("deferred"), "vertexin");
			gl.glVertexAttribPointer(vlocation, 3, GL.GL_FLOAT, false, 32,0);//3=xyz
			vlocation = gl.glGetAttribLocation(shaders.get("deferred"), "texcoordin");
			gl.glVertexAttribPointer(vlocation, 2, GL.GL_FLOAT, false, 32,6);//3=xyz//0,0
			
			//gl.glEnableVertexAttribArray(vlocation);
			
			
			gl.glDepthFunc(GL.GL_LEQUAL);//TODO verify this
			//gl.glEnable(GL3.GL_CULL_FACE);
			//gl.glCullFace(GL.GL_FRONT);
			//gl.glFrontFace(GL3)
			int drawmode = GL.GL_TRIANGLES;//TRIANGLES
			if(Graphics.wireframe) drawmode = GL.GL_LINE_STRIP;//!
			
			//Draw the triangles
			gl.glDrawArrays(drawmode/*TRIANGLES or LINES or something like TRINALGE_STRIP*/, 0, 36*1024);//36
			
			gl.glDisable(GL.GL_DEPTH_TEST);
			
			//*/
			
			//Done with the deferred part of the deferred rendering.
			//Now prepare for output
			
			gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
			gl.glUseProgram(shaders.get("illum"));
			
			//Bind the CC colour buffer only, no need for depth
			
			//Bind the deferred buffers as data0-2
			
			//Bind the screen
			
			//Draw the screen
			
			
			//TODO light parameters, distance calculations and similar
			//TODO restrict light to volumes only, not full screen
			//TODO shadow mapping/stencils?
			
			
			
			
			//Draw overlays
			
			//Draw console where applicable
			
			//Debugging slots show contents of deferred textures in smaller rectangles.
			gl.glUseProgram(shaders.get("dedebug"));
			
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, fullscreenVbo);
			
			//Configure the vertex buffer - draw triangle strip of the debug rectangle (bottom right)
			vboLocation = gl.glGetAttribLocation(shaders.get("dedebug"), "vertex");
			gl.glVertexAttribPointer(vboLocation, 2, GL.GL_FLOAT, false, 0,0);//2=xy
			gl.glEnableVertexAttribArray(vboLocation);
			
			//TODO use uniforms to tell it which info to write
			gl.glActiveTexture(GL.GL_TEXTURE0);
			//0 albedo 3 depth
			gl.glBindTexture(GL.GL_TEXTURE_2D, deferredTargets[0]);//THE TEXTURE TO RENDER to CTFBO
			
			gl.glDrawArrays(GL.GL_TRIANGLE_STRIP, 0, 4);
			
			//Now tidy up
			gl.glUseProgram(0);
			gl.glBindBuffer(GL.GL_FRAMEBUFFER, 0);
		}
		else {
			//SEVERELY limited here!
			//Fixed functionality (fwd rendering) pipeline gives us much less lighting flexibility
			
			
		}
		

		if(Graphics.captureNextFrame){
			GLUtils.capture(gl);
			Graphics.captureNextFrame = false;
		}

	}
	
@Override
	public void init(GLAutoDrawable arg0) {
		GL3 gl = arg0.getGL().getGL3();
		HWCheck.checkHardware(gl);
		//Chooses correct profile now hardware check is complete.
		Graphics.initialise();
		loadCore(gl);
		createFbos(gl);
		//TODO Create FBOs
	}
	@Override
	public void dispose(GLAutoDrawable arg0) {
		
		//Delete all the textures, VBOs, FBOs etc
		//In other words, delete ALL the things.
		Log.info("Disposed of a GL renderer.");
		GL3 gl = arg0.getGL().getGL3();
		
		if(!texmap.isEmpty()){
			Collection<Integer> c = texmap.values();
			int[] texids = new int[c.size()];
			Object[] texarr = c.toArray();
			for(int i=0;i<c.size();i++){
				texids[i]=(Integer) texarr[i];
			}
			gl.glDeleteTextures(texids.length, IntBuffer.wrap(texids));
		}
		texmap.clear();
		
		if(!texmap1D.isEmpty()){
			Collection<Integer> c = texmap1D.values();
			int[] texids = new int[c.size()];
			Object[] texarr = c.toArray();
			for(int i=0;i<c.size();i++){
				texids[i]=(Integer) texarr[i];
			}
			gl.glDeleteTextures(texids.length, IntBuffer.wrap(texids));
		}
		texmap1D.clear();
		
		if(!shaders.isEmpty()){
			Collection<Integer> c = texmap.values();
			Object[] shaderarr = c.toArray();
			for(int i=0;i<c.size();i++){
				Log.debug("Deleting shader "+(Integer) shaderarr[i]);
				gl.glDeleteShader((Integer) shaderarr[i]);
			}	
		}
		shaders.clear();
		
		
		
	}
	
	private void createFbos(GL3 gl) {
		if (!gl.isExtensionAvailable("GL_EXT_framebuffer_object")) {Log.err("FBOs are not supported, cannot create."); return;}

		deferredTargets = new int[4];
		gl.glGenTextures(4,deferredTargets,0);

		for(int i = 0; i<3; i++){
			
			int fbotex = deferredTargets[i];
			gl.glBindTexture(GL.GL_TEXTURE_2D, fbotex);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);//LINEAR?
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);//commenting these kills speed?
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
			//gl.glTexParameteri(GL.GL_TEXTURE_2D, GL3.GL_GENERATE_MIPMAP, GL.GL_TRUE); // automatic mipmap
			
			//WORKS	LDR gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA8, width, height, 0, GL3.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
			
			//int type = (Graphics.current.hdr?GL3.GL_RGBA32F:GL.GL_RGBA8);//RGBA16F
			int type = GL.GL_RGBA8;
			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, type, Graphics.camera.width, Graphics.camera.height, 0, GL3.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
		}
		
		int depthtex = deferredTargets[3];
		gl.glBindTexture(GL.GL_TEXTURE_2D, depthtex);
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);//LINEAR?
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);//commenting these kills speed?
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
		
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL3.GL_DEPTH_TEXTURE_MODE, GL3.GL_LUMINANCE);//when read as tex interpret as luminance. can be alpha
		//gl.glTexParameteri(GL.GL_TEXTURE_2D, GL3.GL_TEXTURE_COMPARE_MODE, GL3.GL_COMPARE_R_TO_TEXTURE);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL3.GL_TEXTURE_COMPARE_FUNC, GL3.GL_LEQUAL);

		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_DEPTH_COMPONENT32, Graphics.camera.width, Graphics.camera.height, 0, GL3.GL_DEPTH_COMPONENT, GL.GL_UNSIGNED_INT, null);

		
		
		
		int[] array = new int[1];
		gl.glGenFramebuffers(1, array, 0);
		deferredFbo = array[0];
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, deferredFbo);

		Log.debug("Generated FBO "+ deferredFbo + " with textures.");
				
		gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL3.GL_COLOR_ATTACHMENT0, GL.GL_TEXTURE_2D, deferredTargets[0], 0 );
		gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL3.GL_COLOR_ATTACHMENT1, GL.GL_TEXTURE_2D, deferredTargets[1], 0 );
		gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL3.GL_COLOR_ATTACHMENT2, GL.GL_TEXTURE_2D, deferredTargets[2], 0 );
		gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL3.GL_DEPTH_ATTACHMENT, GL.GL_TEXTURE_2D, depthtex, 0 );

		
		
		int status = gl.glCheckFramebufferStatus(GL.GL_FRAMEBUFFER);
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
		
		boolean fine = false;
		if (status==GL.GL_FRAMEBUFFER_COMPLETE){fine = true;}
		if(!fine){Log.err("Creation of deferred target FBO failed!");}
		
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		GL3 gl = arg0.getGL().getGL3();
		//Regenerate the projection matrices, resize the FBOs where applicable
		//Set Graphics.camera's width, height and trigger a matrix rebuild
		Log.info("Resize: "+arg3+","+arg4);
		Graphics.camera.width=arg3;
		Graphics.camera.height=arg4;
		Graphics.camera.recalculateProjection();
		
		//TODO resize FBOs AND DEPTH TEXTURE TOO
		for(int i = 0; i<3; i++){
			int fbotex = deferredTargets[i];
			gl.glBindTexture(GL.GL_TEXTURE_2D, fbotex);
			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA8, Graphics.camera.width, Graphics.camera.height, 0, GL3.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
		}
	}
	
	public void loadTextures(String[] filenames, GL3 gl){
		//Load an array of filenames into the graphics memory, giving their key as filename.
		//This is how all textures should be referenced, except ones used for FBO, fonts, loads, sprites etc??
		//Mipmapping? No, linear?
		int actual = filenames.length;
		for(int i = 0; i<filenames.length;i++){
			if(texmap.containsKey(filenames[i])){
			Log.warn("Texture "+filenames[i]+" already loaded.");
			filenames[i]="ELIMINATED";
			actual--;
			continue;//?
			}
		}
		String[] fn = new String[actual];
		int count = 0;
		for(int i = 0; i<filenames.length;i++){
			if(filenames[i]!="ELIMINATED"){fn[count]=filenames[i];count++;}
		}
		
		int[] ids = new int[actual];
		gl.glGenTextures(actual, ids, 0);
	
		for(int i=0;i<fn.length; i++)
		{
			
			try{
				File f = new File(Client.basedir+fn[i]);
				TextureData d = TextureIO.newTextureData(gl.getGLProfile(), f, true, fn[i].substring(fn[i].length()-3,fn[i].length()));
				gl.glBindTexture(GL.GL_TEXTURE_2D, ids[i]);
				gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);//Default to GL_Nearest for performance. GL_Linear bilerps
				gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);//Other option is to GL_NEAREST it up and clip to nearest pixel
				gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, d.getInternalFormat(), d.getWidth(), d.getHeight(), 0, d.getPixelFormat()/*GL3.GL_RGBA/**/, GL.GL_UNSIGNED_BYTE, d.getBuffer());
				texmap.put(fn[i], ids[i]);
				Log.debug("Loaded "+fn[i]+ " into texture "+ids[i]);
			} catch (IOException e){
				Log.warn(e.toString());
			}
		}
	}
	
	private int newSingleFixedTexture(String string, GL3 gl) {
		int[] target = new int[1];
		gl.glGenTextures(1, target, 0);


		try {
			File f = new File(Client.basedir+string);
			
            TextureData data = TextureIO.newTextureData(gl.getGLProfile(), f, true, string.substring(string.length()-3,string.length()));
            
            gl.glBindTexture(GL.GL_TEXTURE_2D, target[0]);
            gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
            gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
            
            gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, data.getInternalFormat(), data.getWidth(), data.getHeight(), 0, data.getPixelFormat()/*GL3.GL_RGBA/**/, GL.GL_UNSIGNED_BYTE, data.getBuffer());
            texmap.put(string, target[0]);
            Log.debug("Loaded ."+ string.substring(string.length()-3,string.length())+ " file " + f.getAbsolutePath()+ " into texture "+target[0]);
			
		}
       catch (IOException exc) {
           exc.printStackTrace();
           System.exit(1);
       }
       return target[0];
	}
	
	
	
	private int loadTexture1D(String string, GL3 gl) {
		int[] texid = new int[1];
		gl.glGenTextures(1, texid, 0);
		
		try {
			File f = new File(Client.basedir+string);
			
            TextureData data = TextureIO.newTextureData(gl.getGLProfile(), f, true, string.substring(string.length()-3,string.length()));
           
           //TODO BUG FIXME Uses a hack to convert RGB to BGR in the shader. SHIIIIIIIT.
            
            gl.glBindTexture(GL3.GL_TEXTURE_1D, texid[0]);
            
            //Assuming we are just loading CC ramps this is awesome.
            
            gl.glTexParameterf(GL3.GL_TEXTURE_1D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
            gl.glTexParameterf(GL3.GL_TEXTURE_1D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
    		gl.glTexParameterf(GL3.GL_TEXTURE_1D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
    		gl.glTexParameterf(GL3.GL_TEXTURE_1D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
    		
            gl.glTexImage1D(GL3.GL_TEXTURE_1D, 0, data.getInternalFormat(), data.getWidth(), 0/*border*/, data.getPixelFormat(), GL.GL_UNSIGNED_BYTE, data.getBuffer());
            texmap1D.put(string, texid[0]);
            Log.debug("Loaded 1D texture " + string + " into "+texid[0]);
		}
       catch (IOException exc) {
           exc.printStackTrace();
       }
		return texid[0];
	}
	
	public int texid(String filename, GL3 gl){
		if(texmap.containsKey(filename)){return texmap.get(filename);}
		Log.warn("Late load of texture "+filename+"!");
		int i = newSingleFixedTexture(filename, gl);
		texmap.put(filename, i);
		return i;
	}

	public void listLoadedTextures(){
		
		int num = texmap.keySet().size();
		Log.info(num + " texture(s) loaded:");
		Object[] arr = texmap.keySet().toArray();
		for(int i =0; i<num;i++){
			String s = (String) arr[i];
			Log.info(s);
		}
	}
}
