package ember.server;
import ember.engine.Constants;
import ember.server.game.configurations.GameConfiguration;
import ember.server.io.LoginHandler;
import ember.server.io.TestProfileSaver;
import ember.server.io.TestProfileLoader;
import ember.server.lobby.GameSettings;
import ember.server.lobby.Lobby;
import ember.server.net.*;
import ember.util.*;

import java.util.HashMap;
import java.util.Map;

/**
 * The workhorse of the server.
 * This code handles initial start-up, oversees managers and stuff.
 *
 */
public class Server {

    private Logger logger = Logger.getInstance();

   
	private static boolean isRunning;
	
	private static Map<Integer,GameEngine> games = new HashMap<Integer,GameEngine>();
	
	private static ThreadGroup gamethreads = new ThreadGroup("GAME");
	private static ThreadGroup miscthreads = new ThreadGroup("MISC");
	private static ProfileWorkerThread profileWorker;

	/**Set up a server and listen
	 * 
	 * @throws Exception
	 */
	
    public Server() throws Exception {
    	
        isRunning=true;
        logger.info("Ember initialised.");
        
        new Thread(miscthreads, new LobbyRunner(), "Lobby").start();
        logger.info("Lobby initialised.");
        
        profileWorker = new ProfileWorkerThread(new TestProfileLoader(), new TestProfileSaver());
        new Thread (miscthreads, profileWorker, "Profile Worker").start();
        logger.info("Profile worker initialised.");
        
        LoginHandler lh = new LoginHandler();
        new Thread (lh, "Login Handler").start();
        //miscthreads,
        
        new Thread(new ListenRunner(), "Socket Acceptor").start();
        logger.info("Listening on port " + Constants.PORT);
        
        
    }


  

	/**
	 * The main "course" of the program.   
	 */
    public void mainProcess() {

    	//TODO ScriptManager.getInstance().invoke("startup", this);
    	
        while (Server.isRunning()) {
        	long start = System.nanoTime();
            processGlobalEvents();
            //TODO this is for debugging only - this is for Global ticks
            @SuppressWarnings("unused")
			long timeTaken = System.nanoTime()-start;
            try {//TODO this better with timing accounting for processing (nanotimer)?
                Thread.sleep(ServerConstants.SERVER_GLOBAL_EVENTS_TICK);
            } catch (InterruptedException e) {
                break;
            }
        }
       
          //TODO  ScriptManager.getInstance().invoke("shutdown", this);
 
        /*
         * Clean up.
         */
        logger.info("Interrupting all threads...");
        profileWorker.quit();
        
        //?
        Lobby.stopRunning();
        
        //Clean up remaining engines here
        
    }
	public static ProfileWorkerThread getProfileWorker() {
		return profileWorker;
	}
 
    private static boolean isRunning() {
		return isRunning;
	}
    
	public void processGlobalEvents() {
		
		SocketManager.processEvents();

		//TODO other managers or these in threads!...
		
		/*for (int i=0;i<Constants.MAX_GAMES;i++){
			if (games.get(i)!=null){
				games.get(i).tick();
				}		
			
		}*///THIS IS NO LONGER NECESSARY WITH INTRODUCTION OF GAME RUNNERS
    
    //UPDATE INTERCHAT/Profile
    }
	
	public static void addGameEngine(GameEngine g, int ID){
		games.put(ID, g);
	}

	static int getFreeGameId(){
		
		for (int i=0; i<ServerConstants.MAX_GAMES;i++){
			if (games.get(i)==null)
			{return i;}
		}
		Logger.getInstance().error("No more free game slots!");
		return -1;
	}
	
	
	
	public static void newGameThread(String name, Runnable r) {
		new Thread(gamethreads, r, name).start();
	}

	public static void createGame(GameConfiguration worldConfiguration, GameSettings gs) {
		try {
			new GameEngine(worldConfiguration, gs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}