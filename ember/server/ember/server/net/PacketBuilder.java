package ember.server.net;

import ember.engine.Constants;
import ember.server.game.Player;

public class PacketBuilder {

	private byte[] payload;
	
	private int id;
	
	public PacketBuilder(){
		payload = new byte[Constants.DEFAULT_PACKET_SIZE];
	}
	public PacketBuilder(int size){
		
	}
	public PacketBuilder setId(int ID){
		id=ID;
		return this;
	}
	
	public void writePacket(){
		//to.getOutputStream().write();
	}
	public PacketBuilder addShort(int sound) {
		// TODO Auto-generated method stub
		return null;
	}
	public PacketBuilder addByte(byte volume) {
		// TODO Auto-generated method stub
		return null;
	}
	public Packet toPacket() {
		// TODO Auto-generated method stub
		//encode
		return null;
	}
	public PacketBuilder setBare(boolean b) {
		// TODO Auto-generated method stub
		return null;
	}
}
