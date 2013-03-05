package magma.server.net;

import java.io.IOException;
import java.net.Socket;

public class GameConnection extends ServerConnection {

	public GameConnection(Socket s, int connectionid, String name) throws IOException {
		super(s, connectionid, name);
	}
}
