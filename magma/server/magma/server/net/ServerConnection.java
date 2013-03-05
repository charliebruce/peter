package magma.server.net;

import java.io.IOException;
import java.net.Socket;

import magma.net.Connection;

public class ServerConnection extends Connection {

	public ServerConnection(Socket s, int connectionid, String name)
			throws IOException {
		super(s, connectionid, name);
		// TODO Auto-generated constructor stub
	}

}
