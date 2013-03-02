package ember.server.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages friends and ignores.
 *
 */
public class Friends {

	private transient Player player;

	private List<String> friends;
	private List<String> ignores;

	private transient int messageCounter;

	public Friends() {
		friends = new ArrayList<String>(256);
		ignores = new ArrayList<String>(128);
	}

	public int getNextUniqueId() {
		if (messageCounter >= 16000000) {
			messageCounter = 0;
		}
		return messageCounter++;
	}

	public void setPlayer(Player player) {
		this.player = player;
		this.messageCounter = 1;
	}

	public void refresh() {
		//SEND TO CLIENTL
		
	}
	public List<String> getFriends() {
		return friends;
	}
	public void addFriend(String name) {
		name = name.toLowerCase();
		
	}
	public void addIgnore(String name) {
		if (ignores.size() >= 100) {
			player.getActionSender().sendMessage("Your ignore list is full.");
			return;
		}
		if (ignores.contains( name)) {
			player.getActionSender().sendMessage((name) + " is already on your ignore list.");
			return;
		}
		ignores.add(name);

	}

	public void removeFriend(String name) {
		name = name.toLowerCase();
		friends.remove(name);
	}

	public void removeIgnore(String name) {
		name = name.toLowerCase();
		ignores.remove(name);
	}


	public void sendMessage(String name, String text) {
	
	}
}
