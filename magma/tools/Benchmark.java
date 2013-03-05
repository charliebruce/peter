import java.awt.GraphicsEnvironment;

import magma.client.Client;
import magma.client.graphics.opengl.Renderer;
import magma.logger.Log;

import com.jogamp.newt.opengl.GLWindow;


public class Benchmark {

	public static void main (String[] args){
		//run a complete graphics benchmark
		
		if (GraphicsEnvironment.isHeadless()){
			Log.crit("Client cannot run in headless mode.");
		}
		
		Renderer renderer = new Renderer();
		GLWindow window;
		
		
		window = GLWindow.create(Client.getGLCapabilities()); 
	    window.addGLEventListener(renderer); 

	    window.setAutoSwapBufferMode(true); 
	    window.setSize(1024, 768); 


	    window.setVisible(true);  
	    
	   
	    
	    while(true){
	    	window.display();
	    }
	}
}
