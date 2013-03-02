package ember.engine.net;

import java.io.IOException;
import java.net.Socket;

public class Connection extends SocketStream {

	/**
	 * The function of the given connection.
	 */
	public int type;
	
	/**
	 * The last time a packet has been successfully received.
	 * In millis using System.currentTimeMillis();
	 */
	public long timeout = 0;
	
	public Connection(Socket s, String name) throws IOException {
		super(s, name);
		timeout = System.currentTimeMillis();
	}

	public void resetTimeout(){
		timeout = System.currentTimeMillis();
	}
	public long timeSinceLastActivity(){
		return System.currentTimeMillis() - timeout;
	}
}
