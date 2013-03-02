package ember.server.net;

import java.io.IOException;
import java.net.Socket;

import ember.engine.Constants;
import ember.server.net.codec.GameProtocolDecoder;
import ember.server.net.codec.GameProtocolEncoder;

public class GameConnection extends ServerConnection {

	
	public GameConnection(Socket s, String name) throws IOException
	{
		
		super(s,"GameConnection to " + s.getInetAddress().getHostAddress());

		type = Constants.ConnectionTypes.GAME_CONNECTION;
		
		decoder = new GameProtocolDecoder(this);
		
	}
	

	public Packet getPacket() {
		//Decrypt
		GameProtocolDecoder d = (GameProtocolDecoder) decoder;
		//TODO antiflood here - if someone spams packets, the calling code may be obstructed.
		Packet p = d.getPacket();
		if (p != null){
			resetTimeout();
		}
		return p;
	}
	public void writePacket(Packet packet) {
		//TODO Encrypt this, change structure (just encode(packet) that returns byte[].
		//TODO this based on Encoder still hangin' around.
		write(GameProtocolEncoder.encode(packet));
	}



}