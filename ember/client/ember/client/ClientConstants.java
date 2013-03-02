package ember.client;

public class ClientConstants {
public static boolean DEBUG=true;
public static class States {
	final static int LOADUP = 0;
	final static int LOGIN = 10;
	final static int authenticating = 20;
	final static int LOBBY = 30;
	final static int PROPOSEDGAME = 35;
	final static int LOADINGGAME = 40;
	final static int INGAME = 50;
	final static int completegame = 60;
}
public static final int PORT = 11235;
public static final int SO_TIMEOUT = 5000;

}
