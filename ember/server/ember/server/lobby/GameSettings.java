package ember.server.lobby;

public class GameSettings {

	public String map;
	public long startCredits;
	
	public GameSettings(String proposedMap, long startCredits){
		this.map = proposedMap;
		this.startCredits = startCredits;
	}
}
