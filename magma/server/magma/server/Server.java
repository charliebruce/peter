package magma.server;

import magma.logger.Log;
import magma.server.threading.Acceptor;

public class Server {

	final static int port = 11235;
	public static final int SO_TIMEOUT = 100;
	public static final boolean TCP_NO_DELAY = false;
	public static final int MAX_CONNECTIONS = 2048;
	
	static Server instance;
	static Acceptor acceptor;
	
	public static void main(String[] args){
		instance = new Server();
		while(true){
			//Tasks go here
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Server(){
		
		//Start up worker threads.
		Log.info("Starting worker threads.");
		
		//Start lobby thread.
		Log.info("Starting lobby.");
		
		//Start listening.
		Log.info("Listening on port "+port);
		Listener.listen();
		
		//Begin accepting connections
		new Thread(new Acceptor(),"Acceptor").start();
		
	}
	
	
	
	
}
