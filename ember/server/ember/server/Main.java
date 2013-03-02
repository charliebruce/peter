package ember.server;
import ember.util.*;

/**
 * This kicks off the server. 
 *
 */
public class Main {

    private static Logger logger = Logger.getInstance();

    public static void main(String[] args) {
        new Thread(new Runnable() 
        {
            public void run() {
                Server s = null;
                try {
                    s = new Server();
                } catch (Exception e) {
                    logger.error(e.toString());
                    logger.stackTrace(e);
                    return;
                }
                System.gc();
                s.mainProcess();
            }
        }, "Server").start();
	}

}
