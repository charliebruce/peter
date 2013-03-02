package ember.server.game;

/**
 * The head's-up display on the player's client is represented by this class.
 * 
 * @author charlie
 *
 */
public class Hud {

	private boolean needsUpdate;
	
	
	
	
	public boolean requiresUpdate(){
		return needsUpdate;
	}
	
	public void touch(){
		needsUpdate = true;
	}

	public void hasBeenUpdated() {
		needsUpdate = false;
	}
}
