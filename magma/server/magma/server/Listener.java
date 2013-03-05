package magma.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import magma.constants.ConnectCodes;
import magma.constants.ConnectionTypes;
import magma.logger.Log;
import magma.server.net.FileConnection;
import magma.server.net.GameConnection;
import magma.server.net.ServerConnection;

public class Listener {
	/**
	 * The socket used to accept connections.
	 */
	
	private static ServerSocket serverChannel;
	/**
	 * The map used to store incoming connection channels.
	 */
	private static Map <Integer,ServerConnection> connections = new HashMap<Integer, ServerConnection>();
	
	
	public static void listen(){		
		try {
			serverChannel = new ServerSocket(Server.port,16/*backlog - a maximum of X connections per tick (100ms) */);
		} catch (IOException e) {
			
			e.printStackTrace();
			Log.crit("Cannot bind to socket.");
		}
				
	}
	
	public static void accept(){
		try {
			while (true){
				
				/*
				 * This blocks until a socket is connected, so runs in a thread.
				 */
				
				Socket s = serverChannel.accept();
				String host = s.getInetAddress().getHostAddress();
					
				/*
				 * Now check for a free connection ID. 
				 */
				
				int id = getFreeConnectionId();
				if(id==-1){
					s.getOutputStream().write(ConnectCodes.NO_MORE_CONNECTIONS);
					s.close();
					Log.info("Connection refused: " + host + " - increase Constants.MAX_CONNECTIONS");
					break;
				}
				
				//Handle no more connections more gracefully - inform client?.
				//TODO Choose appropriate value - 30k, 60k or 1?
				
				/*
				 * Configure low-level settings for the port.
				 */
				s.setSoTimeout(Server.SO_TIMEOUT); // This is the time to wait for read() to return data or InterruptedIO thrown.
	            s.setTcpNoDelay(Server.TCP_NO_DELAY); //?
	            

				//TODO antiflood
	            
	            
	            /*
	             * By now the connection has basically succeeded. Stick it further into execution, login etc.
	             */
	            
	            int requestedType;
	            requestedType = s.getInputStream().read();//TODO someone could block this and delay others.
	            //TODO perhaps thread this further?
	            
	            ServerConnection con;
	            String typetext = null;
	            
	            switch(requestedType){
	            	case ConnectionTypes.GAME_CONNECTION:
	            		typetext = "Game";
	            		con = new GameConnection(s,id,"");
	            		//Client now must sign in before being put through to the lobby.
	            		//LoginHandler.handle((GameConnection) con);

	            		break;
	            	case ConnectionTypes.UPDATE_CONNECTION:
	            		typetext = "Update";
	            		con = new FileConnection(s,id,"");
	            		//TODO to handler.
	            		break;
	            		
	            	default:
	            		s.getOutputStream().write(ConnectCodes.UNKNOWN_CONNECTION_TYPE);
						s.close();
						//TODO log/ban better.
						Log.info("Connection type unknown: " + host + " - tried "+requestedType + ". Hack attempt or odd client?");
						continue;
	            
	            }
	            
	            connections.put(id,con);
	            
				con.out.write(ConnectCodes.CONNECTION_OK);
				Log.info(typetext + " connection accepted: "+host);
				
				
				
				Thread.sleep(100);//Wait for 100ms before next accepting.
				//We don't want to hog CPU
				
			} 
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (InterruptedException e){
			Log.warn("Listener called an InterruptedException. This can probably be ignored if it isn't happening regularly.");
		}
	}
	
	private static void checkConnections() {

		for (int i=0;i<Server.MAX_CONNECTIONS;i++){
			
			//Ignore if not connected
			if (connections.get(i)==null){break;}
			
			//If idle, forget it.
			//if (connections.get(i).timeSinceLastActivity()>=10000){
				//TODO warn client before kicking? TODO ensure player/profile disposed of and game notified.
				//Log.info("Connection "+i+" is idle. Dropping.");
				//removeConnection(connections.get(i));
			//}
			continue;
			
			//TODO TIME OUT checks.
			
			/*if (connections.get(i).isStopped()){
				dropConnection(connections.get(i));
			}*/
			
			
			//TODO Check for disconnects/timeouts here - then remove dead ones nicely.	
			
		}
	}
	

	public static void removeConnection (ServerConnection c){
		ServerConnection sc = connections.get(c.id);
		
		//Stop the writer thread. TODO friendly disconnect notice to client wherever possible. 
		
		sc.disconnectStream();
		//sc.hasTimedOut = true;
		connections.remove(c.id);
		sc = null;
		
	}
	
	public static void processEvents() {
		checkConnections();
	}

	public static void dropConnection(int i){
		try {
			Log.info("Connection dropped: "+connections.get(i).getSocket().getInetAddress().getHostAddress());
			
			//Shutdown I,O,Socket and close.
			connections.get(i).disconnectStream();
			connections.put(i, null);
			
			//TODO kill GameConnection too
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public Map<Integer, ServerConnection> getConnections() {
        return connections;
    }
    
	private static int getFreeConnectionId(){
		
		for (int i=0; i<Server.MAX_CONNECTIONS;i++){
			if (connections.get(i)==null)
			{return i;}
		}
		Log.err("No more free socket channels - connection must be refused!");
		return -1;
	}

	public static ServerConnection getConnection(int connectionId) {
		return connections.get(connectionId);
	}
	public static Socket getSocket(int connectionId) {
		return connections.get(connectionId).getSocket();
	}
	
	
}
