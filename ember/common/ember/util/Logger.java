package ember.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class Logger {

    private static Date date = new Date();
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static String dateText = dateFormat.format(date);
    private static long lastUpdatedDate = 0;

    /**
     * Debug levels:
     * DEBUG   - Low-level, for tracing intricate details only.
     * INFO	   - Often used at the completion of a process.
     * WARNING - Not critical but should be investigated.
     * ERROR   - Stability-affecting errors.
     */
    private static enum Level {
        DEBUG,
        INFO,
        WARNING,
        ERROR
    }

    private Logger() {}
    /**
     * Log an error.
     *
     * @param level The level of seriousness of the message.
     * @param message The unformatted message.
     * 
     * TODO: Make this information remotely accessible to any debug clients.
     */ 
    public void log(Level level, String message) {
    	boolean DEBUG = true;
        if (level == Level.DEBUG && !DEBUG) {
            return;
        }
        synchronized (getClass()) {
            if (level == Level.DEBUG || level == Level.INFO) {
            	//Print debug and info to out
                System.out.println(formatMessage(message));
            } else {
            	//Print warnings and errors to the error console.
                System.err.println(formatMessage(message));
            }
        }
    }

    /**
     * Formats a log message.
     *
     * @param message The unformatted message.
     * @return The formatted message.
     */
    private String formatMessage(String message) {
        if (message == null) {
            message = "";
        }
        if ((lastUpdatedDate + 500) < System.currentTimeMillis()) {
            lastUpdatedDate = System.currentTimeMillis();
            date = new Date();
            dateText = dateFormat.format(date);
        }
        message = message.replaceAll("\t", "    ");
        return "[" + dateText + "] [" + Thread.currentThread().getName() + "]: " + message;
    }

    /**
     * Log a INFO-level message.
     *
     * @param message
     */
    public void info(String message) {
        log(Level.INFO, message);
    }

    /**
     * Log a DEBUG-level message.
     *
     * @param message
     */
    public void debug(String message) {
        log(Level.DEBUG, message);
    }

    /**
     * Log a WARNING-level message.
     *
     * @param message
     */
    public void warning(String message) {
        log(Level.WARNING, message);
    }

    /**
     * Log a ERROR-level message.
     *
     * @param message
     */
    public void error(String message) {
        log(Level.ERROR, message);
    }

    /**
     * Log a stack trace.
     *
     * @param throwable
     */
    public void stackTrace(Throwable throwable) {
        synchronized (getClass()) {
            log(Level.ERROR, throwable.getMessage());
            for (StackTraceElement e : throwable.getStackTrace()) {
                log(Level.ERROR, e.toString());
            }
        }
    }

    /**
     * The logger instance.
     */
    private static Logger instance = new Logger();

    /**
     * Gets the instance of the logger.
     *
     * @return
     */
	public static Logger getInstance() {
		return instance;
	}
	
}
