package ember.server.game;

/**
 * Client's settings.
 *
 */
public class Settings {

    private boolean chat = true, split = false, mouse = true, aid = false;
    private boolean hideDeathInterface = false, autoRetaliate = true;
    private transient Player player;

    public void setDefaultSettings() {
        chat = true;
        split = false;
        mouse = true;
        aid = false;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void refresh() {
    	//TODO write these to client
    }

  //TODO getters and setters

}
