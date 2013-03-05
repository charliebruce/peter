package magma.client.graphics.opengl;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import magma.client.graphics.Graphics;

public class OldRendererCode {

	
	//The FBO stuff
	int baseColour;//clr buffer
	int baseDbuf;//dep buffer
	int baseFbo; //fbo itself

	int colourToneColour;//clr buffer
	int colourToneDbuf;//dep buffer
	int colourToneFbo; //fbo itself

	public void loadMap(GL2 gl){
		//Load a map, generate base textures etc?
		
		//we could generate a single high resolution (4k?) base texture for the base and just sample the shit out of it.
		//Would be ok for maps up to 2 screen widths across? Should be sufficient I guess
		//Mem cost of that is 134,217,728b at 8bpp for albedo only
		//so the normal map alone is 400mb+circa400 mips?
		//On ultra spec machines this is acceptable, but for low-range machines this is a terrible idea. 
		//Time to generate the texture is negligible given 4x 512x512 albedos to blend with?
	
		//Alternative is to break into chunks and blend, continually, every frame. More intense but low mem budget.
		
		}
	
	public void loadGameEngine(GL2 gl){
		//Create the FBOs etc required to make the game look good.
		int[] fbos = GLUtils.createFBO(Graphics.camera.width, Graphics.camera.height, gl);
		baseColour = fbos[0];
		baseDbuf = fbos[1];
		baseFbo = fbos[2];
		
		fbos = GLUtils.createFBO(Graphics.camera.width, Graphics.camera.height,/* baseDbuf,*/ gl);
		colourToneColour = fbos[0];
		colourToneDbuf = fbos[1];
		colourToneFbo = fbos[2];
		//BAD IDEA using a separate depth - then the water will get drawn over naval units
		//*/
		
		
		//131,072,000b = 130mb for 1600p screen at 32bpp - so half-sampling is a nice cheat to quarter this
		//and is perfectly acceptable for many 
	}
	public void loadResourceSet(GL2 gl){
		//Load an abstract set of resources
		
		//Use filesystem or network loader depending on what resources we have cached
	}
	
	public void generateNoise(GL2 gl){
		//(Re)generate the noise functions used for texture blending, film grain etc
		
	}
	
	public void drawTerrainRegions(GL2 gl){
		//Work out which regions are visible
		
		//Draw the regions *terrain, world objects)
		
	}
	public void drawWater(boolean useRefraction, GL2 gl){
		//Check which regions of water would be visible
		
		//Draw them
		if(useRefraction){
			gl.glUseProgram(shaders.get("waterrefracting"));
			
			//Bind the terrain layer to the refracting shader
			gl.glActiveTexture(GL.GL_TEXTURE0);
			gl.glBindTexture(GL.GL_TEXTURE_2D, baseFbo);
			
			//Water is a flat quad drawn at the appropriate height?
			//How do valve do it inexpensively?
			//HL2LC
			//Reload Texture 1Ds
			int location = gl.glGetUniformLocation(shaders.get("waterrefracting"), "phase");
			gl.glUniform1f(location, (float) (0.001* Math.sin(System.currentTimeMillis())));
			
			//ccBlend = ccBlend + 0.001f;
			//reloadCCAttribs=false;
			
			//TODO this tests the look only.
			
			//Waves on CPU?
			
			//Phase and shit for water waves.
			//int location = gl.glGetUniformLocation(shaders.get("waterrefraction"), "phase");
			//gl.glUniform1f(location, waterphase);
			//TODO tessendorf, crysis, etc, fogging via heightmap
			
			//RGB Chromatic refraction triples the lookup cost but offers pretty r/b "highlights"
			//Select the refracting shader
			
			//Draw the quads
		
			
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, fullscreenQuadVbo);

			//Configure the vertex buffer - draw triangle strip of this and you get FS quad.
			int vboLocation = gl.glGetAttribLocation(shaders.get("waterrefracting"), "vertex");
			gl.glVertexAttribPointer(vboLocation, 2, GL.GL_FLOAT, false, 0,0);//2=xy
			gl.glEnableVertexAttribArray(vboLocation);

			//Finally draw.
			gl.glDrawArrays(GL.GL_TRIANGLE_STRIP, 0, 4);
				
			//Finish up
			gl.glUseProgram(0);
		} else {
			//Select the non-refracting shader
			//Draw the mesh using alpha, water tex, etc
		}
	}
	public void drawUnitsBuildings(GL2 gl){
		//Calculate which units must be visible
		
		//Bind the unit/vehicle/generic shader
		gl.glUseProgram(shaders.get("generic"));
		int location = gl.glGetUniformLocation(shaders.get("generic"), "project");
		gl.glUniformMatrix4fv(location, 1, false, Graphics.camera.projection(),0);//Last int is offset - 0?
		//gl.glUniformMatrix4fv(location, 1, false, FloatBuffer.wrap(c.projection()));
		
		//DRAW
		
		gl.glUseProgram(0);
		
	}
	public void drawEffects(GL2 gl){
		
	}

	private void drawUI(GL2 gl) {
		// TODO Auto-generated method stub
		
	}
	public void drawConsole(GL2 gl){
		
	}
	public void drawBase(GL2 gl){
		//Select the shader
		int ccid = shaders.get("passthrough"); 
		gl.glUseProgram(ccid);

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, fullscreenQuadVbo);

		//Configure the vertex buffer - draw triangle strip of this and you get FS quad.
		int vboLocation = gl.glGetAttribLocation(ccid, "vertex");
		gl.glVertexAttribPointer(vboLocation, 2, GL.GL_FLOAT, false, 0,0);//2=xy
		gl.glEnableVertexAttribArray(vboLocation);

		//DISABLE DEPTH WRITING!!?
		//gl.glDisable(GL.GL_DEP)
		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, baseColour);//THE TEXTURE TO RENDER FULLSCREEN

		//Finally draw.
		gl.glDrawArrays(GL.GL_TRIANGLE_STRIP, 0, 4);
		gl.glUseProgram(0);
	}

	boolean reloadCCAttribs = true;

	private void drawFBOOutputToScreen(GL2 gl) {

		//Select the shader
		int ccid = shaders.get("cccore"); 
		gl.glUseProgram(ccid);

		//Bind the ramps
		gl.glActiveTexture(GL.GL_TEXTURE1);
		gl.glBindTexture(GL2.GL_TEXTURE_1D,ccRampA);
		gl.glActiveTexture(GL.GL_TEXTURE2);
		gl.glBindTexture(GL2.GL_TEXTURE_1D,ccRampB);

		//Tell the shader which blend factor to use
		if(reloadCCAttribs){
			//Reload Texture 1Ds
			int location = gl.glGetUniformLocation(ccid, "blend");
			gl.glUniform1f(location, ccBlend);
			ccBlend = ccBlend *0.999f;
			//ccBlend = ccBlend + 0.001f;
			//reloadCCAttribs=false;
		}

		//Display loading screen
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, fullscreenQuadVbo);

		//Configure the vertex buffer - draw triangle strip of this and you get FS quad.
		int vboLocation = gl.glGetAttribLocation(ccid, "vertex");
		gl.glVertexAttribPointer(vboLocation, 2, GL.GL_FLOAT, false, 0,0);//2=xy
		gl.glEnableVertexAttribArray(vboLocation);

		//Bind the input to the colour correction shader.
		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glBindTexture(GL.GL_TEXTURE_2D,/*baseColour*/colourToneColour);//THE TEXTURE TO RENDER FULLSCREEN
		//gl.glBindTexture(GL.GL_TEXTURE_2D, texid("data/test/IMG_8254_RECOLOUR.jpg", gl));//THE TEXTURE TO RENDER FULLSCREEN
		//Already bound ccin to tex0 in setup

		
		
		//Finally draw.
		gl.glDrawArrays(GL.GL_TRIANGLE_STRIP, 0, 4);
		gl.glUseProgram(0);
	}
	
}
