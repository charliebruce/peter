package ember.server.net.codec;

import java.io.InputStream;
import ember.server.net.ServerConnection;
import ember.server.net.PacketQueue;

public class UpdateProtocolDecoder implements ProtocolDecoder {

	public UpdateProtocolDecoder() {

	}


	public boolean doDecode(ServerConnection connection, InputStream in, PacketQueue out) {
		return false;
	}





}
