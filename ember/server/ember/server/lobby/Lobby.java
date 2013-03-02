package ember.server.lobby;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import ember.server.game.Profile;
import ember.server.net.Packet;
import ember.util.Logger;
/**
 * Where Profiles like to hang out and try to make new ProposedGames/Games/Players
 * @author charlie
 *
 */
public class Lobby {

	private static List<Profile> profiles = new LinkedList<Profile>();
	private static Queue<Profile> profilesToRemove = new LinkedList<Profile>();
	private static boolean isRunning;
	
	public static void load() {
		
		//Create a new lobby instance
		isRunning = true;
	}
	
	public static void tick(){
		//Tidy up profiles.
		while (!profilesToRemove.isEmpty()){
			profiles.remove(profilesToRemove.poll());
		}
		
		//Process profiles
		synchronized(profiles){
		for (Profile p : profiles){
			
			//Check they're still s.
			if (p.getConnection().hasTimedOut){
				profilesToRemove.add(p);
				//TODO save and remove profile, connection etc gracefully. 
				//TODO this for other situations too.
				continue;
			}
			
			//CRAP below
			//Logger.getInstance().info("Lobby handling "+p.getUsername());
			//getPacket returns a packet or null depending on input. 
			//process these.
			Packet pa = p.getConnection().getPacket();
			while(pa != null){
				handleLobbyPacket(p,pa);
				pa = p.getConnection().getPacket();
			}
			//TODO something interesting with this. Or get me to do this myself
			//Or make me feed this to PacketHandlers as well. That's an option too. 
			
		}
		}
		//TODO handle proposed games here?
		
		//Lobby Chat
		
		//Handle input and outputs.
		
		//Server.createGame(new FreeForAll());
		
		//TODO this should be done with proposed games and include players..
	
		
		
	}

	public static boolean isSlotFree(){
		//TODO
		return true;
	}
	
	public static boolean register(Profile r) {
		// TODO True for OK, false for no more lobby slots avail.
		
		profiles.add(r);
		//This needs to wait for a bit, store profile, tell it about the lobby state, 
		//keep it in the loop, then start ticking the Profile for packets.
		return true;
	}



	public static boolean isRunning() {
		return isRunning;
	}
	public static void stopRunning() {
		//Emergency use only!
		isRunning = false;
	}

	public static void handleLobbyPacket(Profile p, Packet pa){
		//TODO me
	}
}
