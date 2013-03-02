package ember.server.event;
/**
 * Events which "fire" only once before being removed.
 * @author charlie
 *
 */
public abstract class OnceOnlyEvent extends Event{

	private boolean hasRun = false;
	
	public void postExecuteEvents(){
	hasRun = true;
	}

	public boolean isStopped(){
		return hasRun;
		
	}
}
