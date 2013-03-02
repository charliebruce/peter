package ember.server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import ember.engine.Constants;
import ember.engine.Constants.ConnectCodes;
import ember.engine.Constants.ConnectionTypes;
import ember.server.Server;
import ember.server.ServerConstants;
import ember.server.io.LoginHandler;
import ember.util.Logger;

public class SocketManager {
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

			serverChannel = new ServerSocket(Server.PORT,ServerConstants.BACKLOG);
		
		} catch (IOException e) {
			
			e.printStackTrace();
			System.err.println("Fatal: Cannot bind to socket.");
			System.exit(1);
			
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
				 * Now check for a free connection. 
				 */
				
				int id = getFreeConnectionId();
				if(id==-1){
					s.getOutputStream().write(ConnectCodes.NO_MORE_CONNECTIONS);
					s.close();
					Logger.getInstance().info("Connection refused: " + host + " - increase Constants.MAX_CONNECTIONS");
					break;
				}
				
				//Handle no more connections more gracefully - inform client?.
				//TODO Choose appropriate value - 30k, 60k or 1?
				
				/*
				 * Configure low-level settings for the port.
				 */
				s.setSoTimeout(Constants.SO_TIMEOUT); // This is the time to wait for read() to return data or InterruptedIO thrown.
	            s.setTcpNoDelay(Constants.TCP_NO_DELAY); //?
	            

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
	            		con = new GameConnection(s,"");
	            		//Client now must sign in before being put through to the lobby.
	            		LoginHandler.handle((GameConnection) con);

	            		break;
	            	case ConnectionTypes.UPDATE_CONNECTION:
	            		typetext = "Update";
	            		con = new UpdateConnection(s,"");
	            		//TODO to handler.
	            		break;
	            		
	            	default:
	            		s.getOutputStream().write(ConnectCodes.UNKNOWN_CONNECTION_TYPE);
						s.close();
						//TODO log/ban better.
						Logger.getInstance().info("Connection type unknown: " + host + " - tried "+requestedType + ". Hack attempt or odd client?");
						continue;
	            
	            }
	            
	            connections.put(id,con);
	            con.connectionId = id;
	            
				con.out.write(ConnectCodes.CONNECTION_OK);
				Logger.getInstance().info(typetext + " connection accepted: "+host);
				
				
			} 
		}
		catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	private static void checkConnections() {

		for (int i=0;i<ServerConstants.MAX_CONNECTIONS;i++){
			if (connections.get(i)==null){break;}
			
			
			if (connections.get(i).timeSinceLastActivity()>=10000){
				//TODO warn client before kicking? TODO ensure player/profile disposed of and game notified.
				Logger.getInstance().info("Connection is idle. Dropping.");
				removeConnection(connections.get(i));
			}
			continue;
			
			//TODO TIME OUT checks.
			
			/*if (connections.get(i).isStopped()){
				dropConnection(connections.get(i));
			}*/
			
			
			//TODO Check for disconnects/timeouts here - then remove dead ones nicely.	
			
		}
	}
	

	public static void removeConnection (ServerConnection c){
		ServerConnection sc = connections.get(c.connectionId);
		
		//Stop the writer thread. TODO friendly disconnect notice to client wherever possible. 
		
		sc.disconnectStream();
		sc.hasTimedOut = true;
		connections.remove(c.connectionId);

		sc = null;
		
	}
	
	public static void processEvents() {
		checkConnections();
	}

	public static void dropConnection(int i){
		try {
			Logger.getInstance().info("Connection dropped: "+connections.get(i).getSocket().getInetAddress().getHostAddress());
			
			//Shutdown I,O,Socket and close.
			connections.get(i).disconnectStream();
			connections.put(i, null);
			
			//TODO kill GameConnection too
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public Map<Integer, ServerConnection> getConnections() {
        return connections;
    }
    
	private static int getFreeConnectionId(){
		
		for (int i=0; i<ServerConstants.MAX_CONNECTIONS;i++){
			if (connections.get(i)==null)
			{return i;}
		}
		Logger.getInstance().warning("No more free socket channels - connection must be refused!");
		return -1;
	}

	public static ServerConnection getConnection(int connectionId) {
		return connections.get(connectionId);
	}
	public static Socket getSocket(int connectionId) {
		// TODO Check
		return connections.get(connectionId).getSocket();
	}
}
