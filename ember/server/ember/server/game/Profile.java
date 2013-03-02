package ember.server.game;

import ember.server.net.GameConnection;

public class Profile {

	private String username;
	public GameConnection conn;
	
	public Profile(ProfileDetails d) {
		// TODO replace me with a proper load-result thing.
		username = d.getUsername();
		
	}

	public byte getRights() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getUsername() {
		return username;
	}

	public void setConnection(GameConnection connection) {
		conn = connection;
	}
	public GameConnection getConnection(){
		return conn;
	}

}
