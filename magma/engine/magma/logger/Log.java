package magma.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import magma.client.Client;

/**
 * @author Charlie
 *
 */
public class Log {

	private static File logfile;
	private static FileWriter writer;
	private static boolean appendToFile = true;
	private static boolean writeLogfile = true;
	
	public static void debug(String msg){
		String enc = "["+Thread.currentThread().getName()+"] Debug: " + msg;
		System.out.println(enc);
		writeFileLine(enc);
		writeConsoleLine(enc);
	}
	public static void info(String msg){
		String enc = "["+Thread.currentThread().getName()+"] Info: " + msg;
		System.out.println(enc);
		writeFileLine(enc);
		writeConsoleLine(enc);
	}
	public static void warn(String msg){
		String enc = "["+Thread.currentThread().getName()+"] Warn: " + msg;
		System.out.println(enc);
		writeFileLine(enc);
		writeConsoleLine(enc);
	}	
	public static void err(String msg){
		String enc = "["+Thread.currentThread().getName()+"] Error: " + msg;
		System.err.println(enc);
		writeFileLine(enc);
		writeConsoleLine(enc);
	}
	public static void crit(String msg){
		String enc = "["+Thread.currentThread().getName()+"] CRITICAL: " + msg;
		System.err.println(enc);
		writeFileLine(enc);
		writeConsoleLine(enc);
		System.exit(1);
	}
	
	public static void writeConsoleLine(String msg){
		//TODO this
	}
	
	public static void writeFileLine(String msg){
		if(writeLogfile){
			if (logfile == null)
			{
				String name = Client.basedir + "data/logs/magma.log";
				if (appendToFile)
					try
					{
						File f = new File(name);
						if(!f.canWrite()){
							System.err.println("Log file "+f.getAbsolutePath()+" is not writable!");
							f = null;
							return;
						}
						System.out.println("Info: Logging to file " + f.getAbsolutePath());
						logfile = f;//new RandomAccessFile(f, "rw");
						writer = new FileWriter(logfile);
						//logfile.seek(logfile.length());
						//System.out.println("Opened log file "+logfile);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				else {
					
					try {
						File f = new File(name);
						if(!f.canWrite()){
							System.err.println("Log file "+f.getAbsolutePath()+" is not writable!");
							f = null;
							return;
						}
						logfile = f;
						logfile.delete();
						writer = new FileWriter(logfile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if (writer != null) {
				//System.out.println("Writing log file!");
				try
				{
					writer.append(msg+"\n");
					//FIXME dodgy.
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void close(){
		if(writer!=null) {
			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
