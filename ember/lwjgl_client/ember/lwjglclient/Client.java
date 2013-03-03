package ember.lwjglclient;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;

public class Client {

	public static void main(String[] args){

		System.setProperty("org.lwjgl.librarypath", "C:/Users/administrator.VRCTP/git/peter/ember/data/native/windows");
		
		
		//Create a display


		PixelFormat pixelFormat = new PixelFormat();
		//OpenGL version 3.2
		ContextAttribs contextAttributes = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);

		try {
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.create(pixelFormat, contextAttributes);
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		
		System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));
		
		
		//Print the "loading" text here.

	
		
		
		
		//Check the "core" files (cursors, login text, stuff)
		
		
		while(!Display.isCloseRequested()){
			//Render
			//Refresh
			Display.sync(60);
			Display.update();
		}
		
		Display.destroy();
		
	}
	
	
	
}
