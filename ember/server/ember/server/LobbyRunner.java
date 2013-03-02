package ember.server;

import ember.server.lobby.Lobby;

/**
 * This code creates and oversees lobby operation. 
 *
 */
public class LobbyRunner implements Runnable{

	public LobbyRunner() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		
		Lobby.load();
		
		while(Lobby.isRunning()){
			//Game-specific processes are run here.
			Lobby.tick();
			
			try {//TODO better timing accounting for processing time. 
				Thread.sleep(ServerConstants.LOBBY_TICK);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
