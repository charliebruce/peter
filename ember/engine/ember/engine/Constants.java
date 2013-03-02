package ember.engine;

public class Constants {
	//TODO improve this.
	
	public static final int PORT = 11235;
	
	
	public static final int SO_TIMEOUT = 5000;
	public static final boolean TCP_NO_DELAY = true;


	
	
	
	public static final int DEFAULT_PACKET_SIZE = 1;



	
	
	/**
	 * The following should not be changed except in development or in revisions. 
	 */
	
	
	/**
	 * Packet IDs and lengths
	 */
	
	public class SrvCliPackets {
		public static final int PLAYSOUND = 123;
	}
	public class CliSrvPackets {
		public static final int HEARTBEAT = 255;
	}

	
	/**
	 * Profile states.  
	 */

	public class ProfileState {
		public static final int IN_LOGIN = 1;
		public static final int IN_LOBBY = 2;
		public static final int IN_PROPSED_GAME = 3;
		public static final int IN_LOADING_GAME = 4;
		public static final int IN_ACTIVE_GAME = 5;
		public static final int IN_COMPLETED_GAME = 6;
		public static final int IN_MENU = 7;
		//public static final int IN_VIEWING_STATS = 8;
	}
	
	/**
	 * Login messages - Outgoing from server
	 * @author charlie
	 *
	 */
	public static final class LoginCodes {
		//Success
		public static final int LOGIN_OK = 1;
		//Fail
		public static final int INVALID_CREDENTIALS = 2;
		public static final int BANNED = 3;
		public static final int ALREADY_ONLINE = 4;
		public static final int NO_MORE_SLOTS = 5;
		public static final int TRY_AGAIN = 6;
		
	}
	/**
	 * Connection codes. 
	 * @author Charlie
	 *
	 */
	public class ConnectCodes {
		public static final int CONNECTION_OK = 1;
		public static final int CONNECT_EXCEPTION = 2;
		public static final int UNKNOWN_CONNECTION_TYPE = 3;
		public static final int NO_MORE_CONNECTIONS = 5;
	}
	
	public class GameTypes {
		public static final int SKIRMISH = 1;
		public static final int FREE_FOR_ALL = 2;
		public static final int TEAM_TASK = 3;
	}
	public static final class ConnectionTypes {
		public static final int GAME_CONNECTION = 10;
		public static final int UPDATE_CONNECTION = 1;
		
	}


	public static final class ConnectionStatus {
		public static final int CONNECTED = 1;
		public static final int SERVERFAIL = 2;
		public static final int DISCONNECTED = 3;
	}


	private Constants(){} //Prevent instantiation

}