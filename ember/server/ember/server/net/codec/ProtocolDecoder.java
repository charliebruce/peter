package ember.server.net.codec;

import java.io.InputStream;
import java.nio.ByteBuffer;
import ember.server.net.ServerConnection;
import ember.server.net.PacketQueue;

public abstract interface ProtocolDecoder {

	/**
	 * Parses the data in the provided byte buffer and writes it to
	 * <code>out</code> as a <code>Packet</code>.
	 *
	 * @param connection The connection the data was read from
	 * @param in      The buffer
	 * @param out     The decoder output stream to which to write the <code>Packet</code>
	 * @return Whether enough data was available to create a packet
	 */
	public abstract boolean doDecode(ServerConnection connection, InputStream in/*ByteBuffer*/, PacketQueue out);
	
}
