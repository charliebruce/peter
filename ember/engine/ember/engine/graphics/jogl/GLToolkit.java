package ember.engine.graphics.jogl;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;

import ember.engine.graphics.AbstractGraphicsBuffer;
import ember.engine.graphics.GraphicsToolkit;
import ember.util.Logger;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;

public class GLToolkit extends GraphicsToolkit {

	public GL gl;
	
	public GLToolkit(Canvas target) {
		// TODO Auto-generated constructor stub
		// Using GLCanvas
		
		Logger.getInstance().info(System.getProperty("java.library.path"));
		GLCanvas canvas = new GLCanvas();   // create a GLCanvas
		canvas.resize(800,600);

		gl = canvas.getGL();
		Frame f = new Frame("Charlie");
		f.resize(800,600);
		
		f.add(canvas);

		f.setVisible(true);
		checkFeatures();
	}

	@Override
	public void flip() {
		// TODO Flip the buffer to the screen
		
	}

	public boolean checkFeatures(){
		
		Logger.getInstance().info(gl.glGetString(7936));
		return true;
	}

	@Override
	public AbstractGraphicsBuffer getBuffer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleResize() {
		// TODO Auto-generated method stub
		
	}

}
