package magma.client;

import java.applet.Applet;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;

import magma.client.console.Console;
import magma.client.graphics.Graphics;
import magma.client.graphics.opengl.Renderer;
import magma.client.input.GeneralListener;
import magma.client.input.KeyBinding;
import magma.logger.Log;

public class Client {

	public static String basedir="";
	public boolean abort = false;
	public Renderer renderer;
	private GeneralListener nl;
	private Applet applet;
	public boolean useApplet;
	public boolean devmode = false;
	
	
	public Client(){
		//Make a renderer etc
		nl = new GeneralListener();
		renderer = new Renderer();
		
	}
	
	
	public void rungame(){
	
		Log.info("Magma starting.");
		//Download native libs (init FS, dl req files)
		
		//Check reqd libs
		
		//Initialise input systems
		KeyBinding.init();
		
		Console.init();
		//run startup scripts

		Console.runScript("data/scripts/startup.txt");
		Console.runScript("data/scripts/keys.txt");
		
		
		Graphics.initialise();
		
		
		while(!abort){
			long n0 = System.nanoTime();

			game();//Game logic
			frame();//Render
			
			//TODO multithread this - game()<--- ai,phys,gameplay, stream/audio in parallel, custom threaded tasks in parallel too, pvs branches p when cam pos decided, render
			
			long time=System.nanoTime()-n0;
			double timems = time/1000000;
			ClientStrapper.status((int)((int)1000/(int)(timems+1))+"fps "+"time: "+time+"ns "+timems+"ms ");
			//Log.debug("frame took "+(time));
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Disconnect network if connected, unload filesystem, die gracefully
		
		
		Log.info("Magma closing.");
		Log.close();//No more log entries can be written to file after this point. 
	}

	private void game() {
		//TODO this
		Graphics.camera.tick();
	}


	public GLEventListener getRenderer() {
		return renderer;
		
	}
	public static GLCapabilities getGLCapabilities() 
    { 
        GLProfile glp = GLProfile.get("GL2"); 
        GLCapabilities glc = new GLCapabilities(glp);
        glc.setHardwareAccelerated(true); 
        glc.setDoubleBuffered(true);
        return glc;
    }
	public GeneralListener getGeneralListener() {
		return nl;
	}
	public void useApplet(Applet a) {
		applet = a;
		useApplet = true;
		//We're in the field, making a basecamp might not be easy soldier.
		//Find a writable folder and check space. 
		//We can't work in RAM alone!
		basedir="../";//TODO this
		devmode = true;
		
	} 
	
	public void frame() {
		if (useApplet){
			ClientStrapper.canvas.display();
		}
		else{
			ClientStrapper.window.display();
		}
	}


	public boolean runningAsApplet() {
		return useApplet;
	}


	

	
}
