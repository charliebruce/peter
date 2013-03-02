package ember.server;

import ember.server.net.SocketManager;

public class ListenRunner implements Runnable {

	@Override
	public void run() {
        SocketManager.listen();
        SocketManager.accept();
	}
	

}
