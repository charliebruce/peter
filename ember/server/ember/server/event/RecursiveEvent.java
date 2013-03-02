package ember.server.event;
/**
 * Events which run cyclically until disposed-of externally.
 * @author charlie
 *
 */
public abstract class RecursiveEvent extends Event{

	private int numberOfTimesHasRun = 0;
	private boolean terminate = false;
	
	public void postExecuteEvents(){
		numberOfTimesHasRun++;
	}
	
	public void terminate(){
		terminate = true;
	}
	@Override
	public boolean isStopped(){return terminate;}
	
	
}
