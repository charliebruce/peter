package ember.server.event;
/**
 * Events which run a set number of times before being disposed of.
 * Poison damage might be one example of this.
 * @author charlie
 *
 */
public abstract class IterativeEvent extends Event {

	long times;
	private boolean hasFinished = false;
	
	public void setTimesToRun(long t){
		times = t;
	}
	
	public void PostExecuteEvents(){
		times--;
		if (times == 0){
			hasFinished = true; 
		}
	}
	@Override
	public boolean isStopped(){
		return hasFinished;
		
	}
	
}
