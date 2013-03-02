package ember.client.net;

import java.io.IOException;
import java.net.Socket;

import ember.engine.net.Connection;

public class ClientConnection extends Connection {

	public ClientConnection(Socket s, String name) throws IOException {
		super(s, name);
	}

}
