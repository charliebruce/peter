package ember.server.net.handlers;

import ember.server.game.Player;
import ember.server.net.Packet;

public class HeartbeatPacketHandler implements PacketHandler {

	@Override
	public void handlePacket(Player p, Packet packet) {
		System.out.println("Null packet from " + p.getUsername());
	}

}
