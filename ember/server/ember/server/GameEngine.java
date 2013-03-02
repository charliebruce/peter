package ember.server;
import ember.server.exceptions.OutOfGameSlotsException;
import ember.server.game.*;
import ember.server.game.configurations.GameConfiguration;
import ember.server.lobby.GameSettings;
import ember.util.*;

public class GameEngine {
private Logger logger = Logger.getInstance();

	/**
	 * Running flag.
	 */
	private boolean isRunning;


	/**
	 * Our runner thread.
	 */
	private GameRunner gameRunner;

	/**
	 * Thread group.
	 */
	private ThreadGroup threads = new ThreadGroup("ENGINE");//TODO add instance reference

	/**
	 * World Config
	 */
	private GameConfiguration config;
	
	/**
	 * World instance
	 */
	private World world;

	/**
	 * Game ID
	 */
	private int myID;

	/**
	 * The settings fed to by the lobby.
	 */
	private GameSettings settings;
	
	
	/**
	 * Creates the game world and applies settings.
	 * 
	 * @param gc
	 * @param gs 
	 *
	 * @throws Exception
	 */
	public GameEngine(GameConfiguration gc, GameSettings gs) throws Exception {
		//Game Configuration is the mode (skirmish, task etc)
		//Game Settings contain user-tweakable stuff like economy and rules.
		
		
		int id = Server.getFreeGameId();
		if(id==-1){
			//TODO sync this? to avoid games created in quick succession colliding
			throw new OutOfGameSlotsException();
		}

		myID = id;
		config = gc;
		settings = gs;
		
		gameRunner = new GameRunner(this);
		Server.newGameThread("Game-"+myID, gameRunner);
		Server.addGameEngine(this, myID);
		
	
	}
	
	public void load(){
		
		//Load up the relevant map, configs, techtrees here
		
		logger.info("Setting up world...");
		world = new World(this, config, settings);

		isRunning = true;
		
	}

	/**
	 * Creates and starts new thread in the game's thread group.
	 * Useful for managers etc.
	 * Events could also run in here...
	 *
	 * @param name The name of the thread.
	 * @param r    The runnable object.
	 */
	public void newThread(String name, Runnable r) {
		new Thread(threads, r, name).start();
	}


	/**
	 * Called every tick by GameRunner.
	 */
	public void tick() {
		world.tick();
		//TODO tick players
		
	}

	/**
	 * Gets the is running flag.
	 *
	 * @return
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * This inhibits the GameRunner from ticking this engine. 
	 * Used to bring the engine to a graceful, not-ticking stop.
	 * @param isRunning
	 */
	public void stopRunning() {
		this.isRunning = false;
	}

	/**
	 * Kills all of the engine's sub-threads.
	 */
	public void cleanup() {
		threads.interrupt();
	}




}
