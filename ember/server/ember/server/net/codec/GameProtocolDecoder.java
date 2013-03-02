package ember.server.net.codec;

import java.io.IOException;
import java.io.InputStream;
import ember.server.net.*;
import ember.util.*;

public class GameProtocolDecoder implements ProtocolDecoder {

	/**
	 * Logger instance.
	 */
	private Logger logger = Logger.getInstance();

	public ServerConnection c;

	
	public GameProtocolDecoder(ServerConnection conn)
	{
		c = conn;
	}


	/**
	 * Decodes a message.
	 * @param session
	 * @param in
	 * @param out
	 * @return
	 */
	public Packet getPacket() {
		try {
			if(c.in.available() >= 2) {
				c.bookmarkRead();
				// Read opcode
				int id = c.in.read();
				// Now get length
				int len = c.in.read();

				
				if(id!=255){//Don't mention heartbeat packets (use &&) TODO 255
					logger.info("Packet received. ID: "+id+", Length: "+len+ ". Remaining:" + c.in.available());
				}

					
				// If we can get the packet then do so. For now, just delay a bit.
				if (c.in.available() >= len) {
					c.unbookmarkRead();
					byte[] payload = new byte[len];
					c.in.read(payload);
					Packet p = new Packet(id, len, payload);
					return p;
				} else {
					//TODO delay until it all comes in. Dont block though!
					c.rewindRead();
					return null;
				}
			}
			//No more packets, or incomplete.
			return null;
			
		} catch(NullPointerException err) {
			//Input stream no longer exists, as a best guess.
			System.out.println("Charlie. Do better disposal method. Really.");
		} catch (IOException e) {
			//Client closed in a read cycle or something like that. 
			System.out.println("This might cause some faffing around, combat this?");
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}


	@Override
	public boolean doDecode(ServerConnection connection, InputStream in,
			PacketQueue out) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
