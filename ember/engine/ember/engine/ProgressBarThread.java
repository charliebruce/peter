package ember.engine;

import ember.client.Client;
import ember.engine.graphics.EmberGraphics;

public class ProgressBarThread implements Runnable {

	public boolean abort = false;
	@Override
	public void run() {
		while (!abort){
			java.awt.Graphics g = EmberGraphics.getTarget();
			
			if (g!=null){
				Client.getInstance().renderProgressBar(g);
				EmberGraphics.doFlip();
			}
			//Maintain target fps TODO establish best value
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
