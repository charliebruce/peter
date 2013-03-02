package ember.server.event;
/**
 * Any abstract event can extend this.
 * @author charlie
 */
public abstract class Event {

    private long lastRun;

	/**
     * Runs the event.
     */
    public void run() {
        this.lastRun = System.currentTimeMillis();
        execute();
        postExecuteEvents();
    }


	/**
     * Subclasses of the event class can put their own code here.
	 */
	public abstract void execute();
	
	/**
	 * Get the last time event was run
	 * @return
	 */
	public long getLastRun(){return lastRun;}

	
	/**
	 * Subclasses of event put their code in here to handle times to run
	 */
	 public abstract void postExecuteEvents();


	public abstract boolean isStopped();


	public abstract boolean isReady();

}
