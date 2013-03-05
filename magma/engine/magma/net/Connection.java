package magma.net;

import java.io.IOException;
import java.net.Socket;

public class Connection extends SocketStream {

	public int id;
	
	public Connection(Socket s, int connectionid, String name) throws IOException {
		super(s, name);
		id = connectionid;
	}
	
}
