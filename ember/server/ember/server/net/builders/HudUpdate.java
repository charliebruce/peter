package ember.server.net.builders;

import packetbuilder.PacketBuilder;
import packetbuilder.StaticPacketBuilder;
import ember.server.game.Player;

/**
 * Update the in-game HUD details
 * 
 * @author charlie
 *
 */
public class HudUpdate implements PacketBuilder {

    /**
     * Prevent instance creation.
     */
    private HudUpdate() {
    }

    /**
     * Update the player's HUD.
     *
     * @param p
     */
    public static void update(Player p) {

    	
        StaticPacketBuilder mainPacket = new StaticPacketBuilder().setId(100);

        if (p.getHud().requiresUpdate()){
            //TODO fill mainPacket
        	p.getConnection().writePacket(mainPacket.toPacket());
        	p.getHud().hasBeenUpdated();
        }


    }

}

