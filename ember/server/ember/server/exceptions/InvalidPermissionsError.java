package ember.server.exceptions;
/**
 * Thrown when a profile attempts to do something which it is not allowed to do.
 * These events should eventually be logged and monitored to ensure aren't malicious.
 * @author charlie
 *
 */
public class InvalidPermissionsError extends Exception { 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
