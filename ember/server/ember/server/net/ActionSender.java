package ember.server.net;

import ember.engine.Constants;
import ember.server.game.Player;


/** 
 * Handles data going to the client - coder does not have to go into low levels
 * 
 * @author charlie
 *
 */
public class ActionSender {

	private Player player;


	public ActionSender(Player player) {
		this.player = player;
	}


	public void sendSound(int sound, int volume, int delay) {
		player.getConnection().writePacket(new PacketBuilder()
			.setId(Constants.SrvCliPackets.PLAYSOUND)//TODO this better
			.addShort(sound)
			.addByte((byte) volume)
			.addShort(delay)
			.toPacket());
	}


	public void sendMessage(String string) {
		// TODO Auto-generated method stub
		
	}


	
}
