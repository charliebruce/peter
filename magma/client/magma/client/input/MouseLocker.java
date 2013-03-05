package magma.client.input;

import java.awt.AWTException;
import java.awt.Robot;

import magma.client.graphics.Graphics;

public class MouseLocker {

	private Robot r;
	
	public MouseLocker(){
		try {
			r = new Robot();
		}
		catch (AWTException e) {
			e.printStackTrace();
		}
		catch(SecurityException e){
			//The createRobot permission has not been granted.
			e.printStackTrace();
		}
	}
	public void centre(){
		if(r!=null){
			r.mouseMove(Graphics.camera.width/2,Graphics.camera.height/2);
		}
	}
}
