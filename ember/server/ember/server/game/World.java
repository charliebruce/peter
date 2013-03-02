package ember.server.game;

import java.util.ArrayList;
import java.util.List;

import ember.server.GameEngine;
import ember.server.event.Event;
import ember.server.game.configurations.GameConfiguration;
import ember.server.game.events.RestoreOreEvent;
import ember.server.lobby.GameSettings;
import ember.util.*;

public class World {
	
	/**
	 * Logger instance.
	 */
	private Logger logger = Logger.getInstance();

	
	/**
	 * The game engine.
	 */
	private GameEngine engine;

	/**
	 * A list of pending events.
	 */
	private List<Event> events;
	private List<Event> eventsToAdd;
	private List<Event> eventsToRemove;

	/**
	 * A list of players
	 */
	private List<Player> players;

	
	/**
	 * A list of entities
	 */
	private List<Entity> entities;

	/**
	 * The current world's settings
	 */
	private GameConfiguration configuration;


	/**
	 * Map dimensions
	 */
	public int height;
	public int width;
	
	
	/**
	 * We create the world here.
	 */
	public World(GameEngine eng, GameConfiguration wc, GameSettings gs) {
		
		configuration = wc;
			//TODO interpret settings
		logger.info("Loaded world configuration [" + configuration.getType() + " in "+gs.map+ "].");
		
		//TODO loadMap 
		//TODO set width and height of map
		width = 1024;
		height = 1024;
		
		
		events = new ArrayList<Event>();
		eventsToAdd = new ArrayList<Event>();
		eventsToRemove = new ArrayList<Event>();
		engine = eng;
		//TODO managers - score, game logic?
		//They can go into Engine.start thread
		registerGlobalEvents();
	}

	/**
	 * Gets the game engine.
	 *
	 * @return
	 */
	public GameEngine engine() {
		return engine;
	}

	/**
	 * Gets the configuration.
	 *
	 * @return
	 */
	public GameConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * Register our global events.
	 */
	public void registerGlobalEvents() {
		registerEvent(new RestoreOreEvent());
	}

	/**
	 * Registers an event.
	 *
	 * @param event
	 */
	public void registerEvent(Event event) {
		eventsToAdd.add(event);
	}

	
	public void processEvents() {
		for (Event e : eventsToAdd) {
			if (e == null) {
				continue;
			}
			events.add(e);
		}
		eventsToAdd.clear();
		for (Event e : events) {
			if (e == null) {
				continue;
			}
			if (e.isStopped()) {
				eventsToRemove.add(e);
			} 
			else if (e.isReady()) {
				e.run();
			}
		}
		for (Event e : eventsToRemove) {
			if (e == null) {
				continue;
			}
			events.remove(e);
		}
		eventsToRemove.clear();
	}

	/**
	 * Get the world instance.
	 *
	 * @return
	 */
	public World getInstance() {
		return this;
	}

	/**
	 * Called every tick.
	 */
	public void tick() {
		/*for (Player p : players) {
			if (p == null) {
				continue;
			}
			p.processQueuedPackets();
		}
		processEvents();*///TODO reimplement a better tick system
		
		for (Entity e : entities){
			//e.tick();
		}
	}

	
	/**
	 * Register a player
	 *
	 * @param p
	 * @return the slot
	 */
	public int register(Player p) {
		int slot = -1;
		// TODO make it so this works better
		players.add(p);
		slot = this.getFreeIndex();
		if (slot != -1) {
			logger.info("Player " + p.getPlayerDetails().getUsername() + " entered world - slot " + slot + " - now " + players.size() + " players.");
		} else {
			logger.info("Could not register " + p.getPlayerDetails().getUsername() + " - too many online [online=" + players.size() + "]");
		}
		return slot;
	}

	private int getFreeIndex() {
		// TODO Auto-generated method stub
		return -1;
	}

	/**
	 * Unregister a player.
	 *
	 * @param p
	 */
	public void unregister(Player p) {
		players.remove(p);
		p.destroy();
		//Server.getLogonWorker().savePlayer(p);
		logger.info("Unregistered " + p.getPlayerDetails().getUsername() + " [game has=" + players.size() + "]");
		//TODO if game has victory???
	}

	/**
	 * Gets the world game engine.
	 *
	 * @return
	 */
	public GameEngine getEngine() {
		return engine;
	}

	
	
	public Player getPlayer(String name) {
		
		for(Player player : this.players) {
			if(player.getUsername().equals(name)) {
				return player;
			}
		}
		return null;
	}

	public void leaveChat(Player player) {
		// TODO Auto-generated method stub
		
	}

	public List<Entity> getEntityList() {
		return entities;
	}


	
}
