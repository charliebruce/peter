package ember.server.game.statistics;

import ember.server.game.Player;
/**
 * Statistics gathered about play performance. 
 * TODO proper
 * @author charlie
 *
 */
public class PlayerStats {

	private int kills;
	private int deaths;
	
	private Player p;
	
	public PlayerStats(Player p){
		kills = 0;
		deaths = 0;
		this.p = p;
	}
	public void addKill(){
		kills++;
	}
	public void addDeath(){
		deaths++;
	}
}
