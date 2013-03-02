package ember.client.net;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import ember.client.ClientConstants;
import ember.engine.Constants;

public class GameConnection extends ClientConnection {



	public int returnCode;
	
	public GameConnection(Socket s, String name) throws IOException {
		super (s, name);

	}
	public static GameConnection connect(String host, int port) throws Exception {
		
		int rc;
		Socket s = null;
		
		try {
			s = new Socket(InetAddress.getByName(host), port);
			s.setSoTimeout(ClientConstants.SO_TIMEOUT);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new Exception("Unknown host");
			
		} catch (ConnectException e){
			rc = Constants.ConnectCodes.CONNECT_EXCEPTION;
			throw new Exception("Connect exception");
			
		} catch (IOException e){
			e.printStackTrace();
			throw e;
		}
		GameConnection c = new GameConnection(s, "Test Connection");
		
		try {
			c.out.write(Constants.ConnectionTypes.GAME_CONNECTION);
			rc = c.in.read();//Return Result - 1 is success. Corresponds to ConnectionCodes in server.

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		c.returnCode = rc;
		if (rc == 1){
		c.status = Constants.ConnectionStatus.CONNECTED;
		}
		else
		{
			c.status = Constants.ConnectionStatus.SERVERFAIL;
		}
		return c;
		
	}


}
