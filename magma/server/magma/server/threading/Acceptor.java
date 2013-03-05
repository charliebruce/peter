package magma.server.threading;

import magma.logger.Log;
import magma.server.Listener;

public class Acceptor implements Runnable {

	@Override
	public void run() {
		Log.info("Acceptor starting.");
		while(true){Listener.accept();}
	}

}
