package ember.server.game.configurations;

public class TeamTask implements GameConfiguration {

	@Override
	public void checkCompletion() {
	
	}

	@Override
	public String getType() {
		return "Team Task";
	}

	/**
	 * Is it better to have a configurable TeamTask, or an abstract, extendable one?
	 * Who knows?
	 * 
	 */

}
