package ember.server;

public class ServerConstants {

	/** 
	 * Flexible Configuration
	 */
	
	//The delay in ms to run lobby processing
	public static final int LOBBY_TICK = 100;
	
	//The delay in ms to run profile worker processing
	public static final int PROFILE_WORKER_TICK = 100;
	
	//The delay in ms to run server task processing
	public static final int SERVER_GLOBAL_EVENTS_TICK = 100;
	
	//The delay in ms to run login handling
	public static final int SERVER_LOGIN_HANDLER_TICK = 100;
	
	//The maximum number of connections to store before processing
	public static final int BACKLOG=32;
	
	//The port to listen for connections on
	public static final int PORT = 11235;

	//The maximum number of games which may be running at any given time.
	public static final int MAX_GAMES = 64;//Generic, based on per-game processing

	//The maximum number of players per game. Limited also by bandwidth!
	public static final int MAX_PLAYERS = 32;//Generic, based on per-game processing

	//The maximum number of connections which may be assigned.
	public static final int MAX_CONNECTIONS = MAX_GAMES*MAX_PLAYERS*2+8; //Max player connections, max data, plus 8 for admin/backup
	
	//The maximum number of points in a walking queue
	public static final int WALKING_QUEUE_MAX_LENGTH = 64;

	
	private ServerConstants(){}//Prevent instantiation.
}
