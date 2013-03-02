package ember.server.lobby;

import java.util.List;

import ember.engine.Constants;
import ember.server.Server;
import ember.server.ServerConstants;
import ember.server.game.Profile;
import ember.server.game.configurations.FreeForAll;
import ember.server.game.configurations.Skirmish;
import ember.server.game.configurations.TeamTask;
import ember.util.Logger;

/**
 * When a game is created in the lobby, the game is only a "ProposedGame" until started.
 * This class represents the current state of the proposed game.
 * Handles profile (soon to be player) group/team, game type, economic settings etc. 
 * 
 * @author Charlie
 *
 */
public class ProposedGame {

	public int proposedMaxPlayers;
	public int proposedGameType;
	public String proposedMap;

	public long startCredits;
	public boolean[] confirmReady;
	
	public Profile leader;
	public List<Profile> players;
	//TODO all this nicely
	
	public ProposedGame(Profile first){
		
		players.add(first);
		leader = first;
		
		
	}
	
	public void joinProposedGame(Profile p){
		if ((players.size()<proposedMaxPlayers) && !players.contains(p)){
			
			players.add(p);
			updateStuff();
			
			//Profile is signed up. Inform them now. 
		}
		else {
			//Game full or player already in it. Tell the profile.
		}
	}

	public void leaveProposedGame(Profile p){
		//TODO this can be done without player awareness (kicking). 
		//This may also happen on timeout. 
		//Tell player.
		players.remove(p);
		if (p == leader){
			//Host has left game.
			destroyProposedGame();
		}
		updateStuff();
	}
	public void confirmReady(Profile p){
		
		if (allPlayersAreReady()){
			actualiseGame();
		}
	}
	
	public void destroyProposedGame(){
		//The game has been closed without starting. 
		//Notify players.
		//Wipe me;
		
		
	}
	
	private void updateStuff() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void actualiseGame(){
		//TODO:
		//Check all players
		//Stick all players into a group.
		//(HERE ORE ELSEWHERE) Produce Player objects (associated wit Profiles)
		//Verify game settings are all vanilla (doublechecking at this point). 
		//NOTE TICKRATE IS FED IN TO GAMERUNNER SO MUST BE CLEAN AND ACCEPTABLE
		//Feed this to the Server and trigger the loading/commencing of the Game
		proposedMap = "Testmap";
		startCredits = 10000;
		GameSettings gs = new GameSettings(proposedMap, startCredits);
		
		switch(proposedGameType){
		case Constants.GameTypes.SKIRMISH:
			Server.createGame(new Skirmish(),gs);
			
			break;
		case Constants.GameTypes.FREE_FOR_ALL:
			Server.createGame(new FreeForAll(),gs);
			break;
			
		case Constants.GameTypes.TEAM_TASK:
			Server.createGame(new TeamTask(),gs);
			break;
		}
		
	}
	
	public void changeMaxPlayers(int max){
		if ((proposedMaxPlayers<1)&&(proposedMaxPlayers>=ServerConstants.MAX_PLAYERS)){
			proposedMaxPlayers = max;
			resetConfirmReady();
			if (max<players.size()){
				//Lowered limit and now a player can't "fit".
				Logger.getInstance().warning("Game is oversubscribed. Wat do?!");
			}
		}
		else {
			//Invalid number of players.
		}
	}
	public void changeGlobalSetting(){
		
		resetConfirmReady();
		
	}
	
	private boolean allPlayersAreReady() {
		// TODO Auto-generated method stub
		return false;
	}


	public void resetConfirmReady(){

	}
	
	
	
}