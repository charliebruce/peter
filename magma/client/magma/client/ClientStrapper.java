package magma.client;

import java.applet.Applet;
import java.awt.GraphicsEnvironment;

import javax.media.opengl.awt.GLCanvas;

import com.jogamp.newt.opengl.GLWindow;

import magma.client.graphics.Graphics;
import magma.logger.Log;
import magma.nativeloader.NativeLoader;

public class ClientStrapper extends Applet {

	private static final long serialVersionUID = -2978813334341224276L;

	static GLCanvas canvas;
	static GLWindow window;

	private static Applet applet;
	private static Client client;

	public static void main(String[] args){
		//NOTE if basedir is used (not ""), logging will fail into files until the basedir is set
		Log.debug("Main Routine Called - Initialising.");

		if (GraphicsEnvironment.isHeadless()){
			Log.crit("Client cannot run in headless mode.");
		}

		client = new Client();

		int targetWidth=1024;
		int targetHeight=786;
		//Read arguments
		for (int i=0; i<args.length;i++)
		{
			if(args[i].equals("-devmode")){
				client.devmode = true;
				continue;
			}
			if(args[i].equals("-width")){
				try 
				{
					int nw = Integer.parseInt(args[i+1]);
					if(nw<64||nw>8192)
					{
						//Width is invalid
						Log.warn("Startup parameter -width was given incorrect parameter. Width must fall within 64 and 8192 pixels.");
					}else {
						//Set Width in Graphics
						targetWidth = nw;
					}

					i++;
					continue;
				} catch(NumberFormatException e) {
					Log.warn("Startup parameter -width was given non-number parameter.");
				}
			}

			if(args[i].equals("-height")){
				try 
				{
					int nh = Integer.parseInt(args[i+1]);
					if(nh<64||nh>8192)
					{
						//Height is invalid
						Log.warn("Startup parameter -height was given incorrect parameter. Height must fall within 64 and 8192 pixels.");
					}else {
						//Set Height in Graphics
						targetHeight = nh;
					}

					i++;
					continue;
				} catch(NumberFormatException e) {
					Log.warn("Startup parameter -height was given non-number parameter.");
				}
			}

		}
		String strDebug="";
		if(client.devmode) strDebug = " (devmode)";
		
		NativeLoader.ensureNativeLibs();
		
		window = GLWindow.create(Client.getGLCapabilities()); 
		window.setTitle("Magma"+strDebug);
		window.addGLEventListener(client.getRenderer()); 

		window.setAutoSwapBufferMode(true); 
		window.setSize(targetWidth, targetHeight); 
		
		
		
		window.addWindowListener(client.getGeneralListener());
		window.addKeyListener(client.getGeneralListener());
		window.addMouseListener(client.getGeneralListener());


		
		window.setVisible(true);  
		

		//goFullscreen();
		
		client.rungame();

	}
	
	public void run(){
		Log.debug("Run");
	}
	@Override
	public void start(){
		Log.debug("Start");

	}
	@Override
	public void stop(){
		Log.debug("Stop");
	}
	@Override
	public void init(){
		//NOTE: Logging before useApplet(this) will not log to file since Client.basedir isn't set until then. 
		if (GraphicsEnvironment.isHeadless()){
			Log.crit("Client cannot run in headless mode.");
		}

		applet = this;
		client = new Client();


		NativeLoader.ensureNativeLibs();
		
		
		canvas = new GLCanvas(Client.getGLCapabilities());
		canvas.addGLEventListener(client.getRenderer());

		this.setVisible(true);
		canvas.setAutoSwapBufferMode(true);
		this.add(canvas);

		client.useApplet(this);

		canvas.setEnabled(true);
		canvas.setLocation(0, 0);
		canvas.setSize(800,600);
		canvas.setVisible(true);
		canvas.validate();

		canvas.requestFocus();

		canvas.addKeyListener(client.getGeneralListener());
		canvas.addMouseListener(client.getGeneralListener());
		canvas.addMouseWheelListener(client.getGeneralListener());
		canvas.addMouseMotionListener(client.getGeneralListener());

		client.rungame();


	}


	public static void appletgoFullscreen(){
		//canvas.removeGLEventListener(listener)
		//Create a new fullscreen frame

		//Change the renderer over to it

		//Continue
	}
	public static void notifyWindowDestroyed() {
		client.abort=true;
	}


	public static void goFullscreen() {
		if(client.runningAsApplet()){
			appletgoFullscreen();
		}
		else{
			window.setFullscreen(true);
		}
	}
	public static void status(String string) {
		if(client.runningAsApplet()){
			applet.showStatus(string);
		}else{
			String strDebug="";
			if(client.devmode) strDebug = " (devmode)";
			window.setTitle("Magma"+strDebug+string);
		}
	}


}
