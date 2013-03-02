package ember.server.net.handlers;

import ember.server.game.Player;
import ember.server.net.Packet;

public interface PacketHandler {

	public abstract void handlePacket(Player p, Packet packet);
	
}
