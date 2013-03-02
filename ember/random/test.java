/**
 * File: JoglTextureDemo.java
 *
 * This file is free to use and modify as it is for educational use.
 *
 * Version:
 * 1.1 Initial Version
 *
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 * A simple example of how to map a texture to a GL_QUAD this is the basis for
 * all your texture mapping needs. However, this does not involve a more
 * optimal approaches which is to use buffers. If you are intending to make a
 * high performance application you should look into using buffers instead of
 * the glVertex, glColor, glTexCoord calls, since each call must send
 * information to the GPU for rendering. Buffers can send large amounts of data
 * and be sent to the GPU less often instead of each frame.
 */
public class test extends JFrame implements GLEventListener {

	private static final String FROM_GPSNIPPETS = "from gpsnippets";

	private static final String HELLO_WORLD = "Hello World!";

	private static final int WND_WIDTH = 640;

	private static final int WND_HEIGHT = 480;

	private Animator anim;

	private GLCanvas canvas;

	private Texture texture;

	private BufferedImage image;

	/**
	 * Our constructor that sets up and starts the application. This does not
	 * immediately initialize the scene.
	 */
	public test() {
		super("JOGL: Basic Texture Drawing Demo"); //window title
		setLayout(new BorderLayout()); //our generic fill the window layout.

		//create the canvas, animator, and other objects
		setup();

		// need to wait till the window and all components are shown before
		// starting the jogl rendering thread.
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				start();
			}
		});

		//setup the JFrame size and starting location
		setSize(WND_WIDTH, WND_HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// queue on the event thread to reduce the likelihood of threading
		// issues with swing.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setVisible(true);
			}
		});
	}

	/**
	 * Add and start the animator for rendering our scene.
	 */
	private void start() {
		anim.add(canvas);
		anim.start();
	}

	/**
	 * Create the GLCanvas and set properties for the graphics device
	 * initialization, such as bits per channel. And advanced features for
	 * improved rendering performance such as the stencil buffer.
	 */
	private void setup() {
		// setup the hardware default settings
		GLCapabilities caps = new GLCapabilities();
		caps.setDoubleBuffered(true);
		caps.setHardwareAccelerated(true);

		// create the image to be the size of the window, texture size must
		// be a power of two. 512 is adequate for display decent looking text
		//without taking up too much space
		image = new BufferedImage(128, 128,
				BufferedImage.TYPE_INT_ARGB);

		// create the canvas for drawing
		canvas = new GLCanvas(caps);

		// create the render thread
		anim = new Animator();

		// add the canvas to the main window
		add(canvas, BorderLayout.CENTER);

		// need this to receive callbacks for rendering (i.e. display() method)
		canvas.addGLEventListener(this);
	}

	/**
	 * Called by the drawable immediately after the OpenGL context is
	 * initialized; the GLContext has already been made current when this method
	 * is called.
	 *
	 * @param drawable
	 *            The display context to render to
	 */
	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		// paint the texture we wish to draw.
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		//draw or load texture
		int w = image.getWidth();
		int h = image.getHeight();
		GradientPaint gp = new GradientPaint(0, 0, Color.white, w, h, Color.LIGHT_GRAY);

		//this tells the graphics context to use paint instead of color.
		g.setPaint(gp);
		g.fillRect(0, 0, w, h);
		g.setColor(Color.green.darker());

		//get the centered x position within the image for the text
		int centerOnX = w/2-g.getFontMetrics().stringWidth(HELLO_WORLD)/2;

		//draw it, the int cast is needed because 0.25 is defaulted to a double
		g.drawString(HELLO_WORLD,  centerOnX, (int)(h*0.25));

		//get the centered x position within the image for the text
		centerOnX = w/2-g.getFontMetrics().stringWidth(FROM_GPSNIPPETS)/2;

		//draw it, arithmetic will cast to the more precise type
		g.drawString(FROM_GPSNIPPETS, centerOnX, (int)(h*0.5));

		//little visual sugar with a round rectangle overlay
		g.setColor(new Color(255, 255, 255, 60)); //white color overlay
		g.fillRoundRect((int)(w*0.1), (int)(h*0.1), (int)(w*0.8), (int)(w*0.5), 25, 25);

		// enable 2D textures
		gl.glEnable(GL.GL_TEXTURE_2D);

		// select modulate to mix texture with color for shading
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);

		// create the texture, you may need to find an alternative if this
		// method is not available for the jogl version you use.
		texture = TextureIO.newTexture(image, false);

		// set the texture parameters to allow for properly displaying
		texture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
		texture.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
		texture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		texture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);

		texture.bind(); // only after settings texture properties

		// now we set the stage basically
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); //the color to use when clearing the screen
		gl.glMatrixMode(GL.GL_PROJECTION); //
		gl.glLoadIdentity(); //the identity matrix
		gl.glOrtho(0, 1, 0, 1, -1, 1);
	}

	/**
	 * Called by the drawable when the surface resizes itself. Used to reset the
	 * viewport dimensions.
	 *
	 * @param drawable
	 *            The display context to render to
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		//TODO: adjust the scene
	}

	/**
	 * Called by the drawable when the display mode or the display device
	 * associated with the GLDrawable has changed
	 */
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged) {
		//TODO: reset the device states
	}

	/**
	 * Called by the drawable to perform rendering by the client.
	 *
	 * @param drawable
	 *            The display context to render to
	 */
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		//variables for controlling the rendered geometry
		float startX = 0.25f;
		float startY = 0.25f;
		float width = 0.50f;
		float height = 0.50f;

		//this is the good part
		gl.glClear(GL.GL_COLOR_BUFFER_BIT); //use the clear color we set
		gl.glBegin(GL.GL_QUADS); //the gl draw mode

		//set the texture coordinates for the next vertex
		gl.glTexCoord2d(1.0, 1.0);
		//add a two-dimensional vertex
		gl.glVertex2f(startX + width, startY);

		gl.glTexCoord2d(0.0, 1.0);
		gl.glVertex2f(startX, startY);

		gl.glTexCoord2d(0.0, 0.0);
		gl.glVertex2f(startX, startY + height);

		gl.glTexCoord2d(1.0, 0.0);
		gl.glVertex2f(startX + width, startY + height);

		gl.glEnd();
		gl.glFlush(); //show the back buffer is double buffering was set
	}

	/**
	 * Main application entry-point.
	 *
	 * @param args Command line arguements
	 */
	public static void main(String[] args) {
		new JoglTextureDemo(); //create and show the app
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

}


public class test {

}
