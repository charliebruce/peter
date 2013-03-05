package shader;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;

import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;

public class Testbed {


	static GLEventListener r;

	public static GLCapabilities getGLCapabilities() 
    { 
        GLProfile glp = GLProfile.get("GL2"); 
        return new GLCapabilities(glp); 
    } 
	
	public static void main(String[] args){

		GLProfile.initSingleton(true);
		
		r = new ShaderShop(); //JoglTestRenderer();
		
		GLWindow window = GLWindow.create(getGLCapabilities()); 
	    window.addGLEventListener(r); 
	    //window.addWindowListener(this); 
	    window.setAutoSwapBufferMode(true); 
	    window.setSize(1024, 768); 
	    window.setVisible(true); 
	    window.addWindowListener(new Listener());
	    window.addKeyListener((KeyListener) r);
	    
	    while (true)
	    {
	    	long t0 = System.nanoTime();
	    	window.display(); 
	    	long t1 = System.nanoTime();
	    	float fpsmax = 1000000000/(t1-t0);
	    	//System.out.println("FPS is " + jtr.fps);
	    	if(true){
	    	try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
			t1 = System.nanoTime();
			float fpsactual =1000000000/(t1-t0);
			window.setTitle((int)fpsmax + " FPS Possible - " + fpsactual + " actual");
	    } 
		
		
	}
	
}
