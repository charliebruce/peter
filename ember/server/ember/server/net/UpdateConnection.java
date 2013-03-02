package ember.server.net;

import java.io.IOException;
import java.net.Socket;

import ember.engine.Constants;
import ember.server.net.codec.UpdateProtocolDecoder;

public class UpdateConnection extends ServerConnection {

	public UpdateConnection(Socket s, String name) throws IOException {
		super(s, name);
		
		type = Constants.ConnectionTypes.UPDATE_CONNECTION;
		
		decoder = new UpdateProtocolDecoder();

	}

	
	
	
}
