package ember.server.game;

import java.util.Map;
import java.util.Queue;


import ember.server.net.ActionSender;
import ember.server.net.GameConnection;
import ember.server.net.Packet;
import ember.server.net.handlers.PacketHandlers;

/**
 * Represents a (connected) player in a game.
 * Player is a Controller. 
 */
public class Player implements Commander {
	
	private World world;
	private GameConnection connection;
	private Profile profile;
	
	private transient boolean hasFocus;
	private ProfileDetails details;
	private transient ActionSender actionSender;
	private transient Hud hud;
	private transient Queue<Packet> queuedPackets;
	private transient PlayerUpdateFlags updateFlags;
	private transient Sprite sprite;
	private transient TravelQueue walkingQueue;
	private transient LocalEntityList localEntities;
	private transient ChatMessage lastChatMessage;
	private transient Map<String, Object> temporaryAttributes;
	private Settings settings;
	private Friends friends;
	private int rights = 0;
	public boolean inChat = false;
	private boolean onLogin;
	private int displayMode;
	private boolean fullScreen;
	

		public Player(Profile profile, GameConnection conn) {
			this.connection = conn;
			this.profile = profile;
			this.friends = new Friends();
			this.settings = new Settings();
			this.hud = new Hud();
			this.settings.setDefaultSettings();
		}


		public void setTemporaryAttribute(String attribute, Object value) {
			temporaryAttributes.put(attribute, value);
		}

		public Object getTemporaryAttribute(String attribute) {
			return temporaryAttributes.get(attribute);
		}

		public void removeTemporaryAttribute(String attribute) {
			temporaryAttributes.remove(attribute);
		}

		public Settings getSettings() {
			return this.settings;
		}

		public ProfileDetails getPlayerDetails() {
			return this.details;
		}

		public ActionSender getActionSender() {
			return this.actionSender;
		}

		public String getUsername() {
			return this.details.getUsername();
		}

		public int getRights() {
			return this.rights;
		}

		public void processQueuedPackets() {
			synchronized (queuedPackets) {
				Packet p = null;
				while ((p = queuedPackets.poll()) != null) {
					PacketHandlers.handlePacket(this, p);
				}
			}
		}

		public void addPacketToQueue(Packet p) {
			synchronized (queuedPackets) {
				queuedPackets.add(p);
			}
		}

		public PlayerUpdateFlags getUpdateFlags() {
			return updateFlags;
		}

		public Sprite getSprites() {
			return sprite;
		}

		public TravelQueue getWalkingQueue() {
			return walkingQueue;
		}

	
		public boolean isDisconnected() {
			return connection.stopped;
		}

		public ChatMessage getLastChatMessage() {
			return lastChatMessage;
		}

		public void setLastChatMessage(ChatMessage msg) {
			lastChatMessage = msg;
		}

		public World getWorld() {
			return world;
		}

		public Friends getFriends() {
			return friends;
		}

		public void destroy() {
			world.leaveChat(this);
		}

		public void setDisplayMode(int displayMode) {
			this.displayMode = displayMode;
		}

		public int getDisplayMode() {
			return displayMode;
		}
		public void setFullScreen(boolean fullScreen) {
			this.fullScreen = fullScreen;
		}

		public boolean isFullScreen() {
			return fullScreen;
		}
		public void setHasFocus(boolean b) {
			hasFocus = b;
		}
		public boolean getHasFocus(){
			return hasFocus;
		}
		public GameConnection getConnection() {
			return connection;
		}


		public Hud getHud() {
			return hud;
		}
		
	}
