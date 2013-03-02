package ember.server.net;

import java.io.IOException;
import java.net.Socket;
import ember.engine.net.Connection;
import ember.server.net.codec.ProtocolDecoder;

public abstract class ServerConnection extends Connection {

	public ServerConnection(Socket s, String name) throws IOException {
		super(s, name);
	}

	/**
	 * The decoder for the given protocol.
	 */
	ProtocolDecoder decoder;
	
	
	/**
	 * Common with the connection in SocketManager
	 */
	public int connectionId;


	public boolean hasTimedOut = false;





}
