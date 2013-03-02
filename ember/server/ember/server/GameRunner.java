package ember.server;
/**
 * This code creates and oversees game engine instance(s).
 *
 */
public class GameRunner implements Runnable{
	
	private int tick =50;
	private GameEngine ge;
	
	public GameRunner(GameEngine gameEngine) {
		ge  = gameEngine;
	}

	@Override
	public void run() {
		
		ge.load();
		
		while(ge.isRunning()){
			//Game-specific processes are run here.
			ge.tick();
			
			try {
				Thread.sleep(tick);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
