package ember.server.game.configurations;
/**
 * An abstract class representing any mode in which a game can run.
 * Examples of modes are: Skirmish, Free For All, and Team Tasks. 
 * This is currently a basic prototype only. TODO complete.
 * @author charlie
 *
 */
public interface GameConfiguration {

	public abstract String getType();

	public abstract void checkCompletion();
	
}
