import java.applet.Applet;
import java.awt.Graphics;

import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;

import magma.logger.Log;
import magma.nativeloader.NativeLoader;


public class hwprofiler extends Applet {
	
	public void init(){
		Log.info("Init");
	}
	public void run(){
		Log.info("Run");
	}
	
	GLCanvas g;
	
	public void start(){
		Log.info("Start");
		NativeLoader.ensureNativeLibs();
		GLProfile.initSingleton(true);
		
		GLProfile glp = GLProfile.get("GL2GL3");
		
		GLCapabilities caps = new GLCapabilities(glp);
		caps.setHardwareAccelerated(true);
		caps.setDoubleBuffered(true);
		
		g = new GLCanvas(caps);
		g.setRealized(true);
		this.add(g);
		
		g.addGLEventListener(new checker());
		
		FPSAnimator a = new FPSAnimator(g, 60);
		a.start();
		
		
		Log.info("Canvas is "+g.toString());
		this.stop();
		//g.display();

		//g.display();
		
	}
	public void stop(){
		Log.info("Stop");
		//Redirect to success fail or upload process now.
	}
	public void draw(Graphics g){
		
		
	}
	public void update(){
		Log.info("Update");
	}


}
