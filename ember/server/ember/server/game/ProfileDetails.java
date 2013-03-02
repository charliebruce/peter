package ember.server.game;

import ember.server.net.GameConnection;

/**
 * Contains a player's name, password and current connection.
 *
 */
public class ProfileDetails {

	private String username;
	private String password;
	private GameConnection connection;
	
    public ProfileDetails(String username, String password, GameConnection connection) {
        this.username = username;
        this.password = password;
        this.connection = connection;
    }
	public void setUsername(String s) {
		this.username = s;
	}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
	public GameConnection getConnection() {
		return connection;
	}

}
