package ember.server.net.handlers;

import java.util.Map;

import ember.server.game.Player;
import ember.server.net.Packet;

public class PacketHandlers {

	/**
     * Packet handlers map.
     */
    private static Map<Integer, PacketHandler> handlers;
 
	public static void handlePacket(Player p, Packet packet) {
		// TODO error handling.
		handlers.get(packet.getId()).handlePacket(p, packet);
	}

	public static void load(){
		
		//1-10 are lower-level, pre-encryption commands and as such are not here.
		//1 - signals handshake (RSA-encrypted block)
		//2 - Timing?
		

		
		/*
		 * Keep-alive signal.
		 */
		handlers.put(255, new HeartbeatPacketHandler());
		
	}
	
	private PacketHandlers(){};//Prevent instantiation.

}
