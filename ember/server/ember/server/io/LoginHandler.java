package ember.server.io;

import java.util.LinkedList;
import java.util.Queue;

import ember.server.Server;
import ember.server.ServerConstants;
import ember.server.game.ProfileDetails;
import ember.server.net.GameConnection;
import ember.server.net.Packet;
import ember.util.Logger;

/**
 * Handles Game Connection handshakes and logins.
 * @author Charlie
 *
 */
public class LoginHandler implements Runnable {

	private static Queue<GameConnection> queued;
	private static Queue<GameConnection> queuedForRemoval;
	public static void tick(){
		
	}
	
	public static void handle(GameConnection con){
		queued.add(con);
		
	}

	@Override
	public void run() {
		queued = new LinkedList<GameConnection>();
		queuedForRemoval = new LinkedList<GameConnection>();
		
		while(true){
			
			/*
			 * Remove GameConnections marked for removal from the login handler.
			 */
			while(!queuedForRemoval.isEmpty()){
				try{queued.remove(queuedForRemoval.poll());}catch(Exception ex){}
			}
			
			/*
			 * Now process the GameConnections at login stage.
			 */
			
			if (!queued.isEmpty()){
				for (GameConnection curr : queued){
				
				//Read a packet from the connection. Null if unavailable.
				Packet in = curr.getPacket();
					
				if (in != null){

					//A complete packet has been received. 
					//TODO check if the operation is valid.
					
					if (in.getId() == 1){
						
						
						
						/*
						 * Received the encrypted, super-secret login block.
						 * Decrypt it.
						 */
						byte[] raw = in.getData();

						LoginBlockDecryptor lbd = null;
						
						try {	
							lbd = new LoginBlockDecryptor(raw);
						}
						catch (Exception ex){
							markForRemoval(curr);
							System.err.println("Decrypt or response failed!");
							ex.printStackTrace();
							continue;
						}
						
						/*
						 * The data has been received. Now do useful things with it.
						 */
						
						Logger.getInstance().info("Login decrypted. Username: "+lbd.getUsername()+", password: "+lbd.getPassword());
						
						//Logger.getInstance().info("Seed: "+lbd.getSeed());
						//TODO use this to initialise stream cipher, and feed to loader.
						
						/*
						 * Cipher done and put into the stream/connection model. 
						 * Now read() and write()s are secure.
						 * Now formulate the response packet to the client, depending on the details.
						 * Profile Worker handles the process from here on.
						 */
						
						Server.getProfileWorker().loadProfile(new ProfileDetails(lbd.getUsername(),lbd.getPassword(),curr));
						
						/*
						 * No longer my responsibility! :)
						 */
						markForRemoval(curr);
						
					}
				}
				else {
					//A session has been opened but there has been no login packet from the client.
					//After a set number of these the session should be closed.
				}
				}
				
			}
			
			try {
				Thread.sleep(ServerConstants.SERVER_LOGIN_HANDLER_TICK);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

	private void markForRemoval(GameConnection curr) {
		queuedForRemoval.add(curr);
	}
}
