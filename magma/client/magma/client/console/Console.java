package magma.client.console;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import magma.client.Client;
import magma.client.console.commands.*;
import magma.logger.Log;

public class Console {

	private static HashMap<String,Command> commands = new HashMap<String,Command>();
	
	public static void init(){
		register("bind", new Bind());
		register("unbind", new Unbind());
		register("echo", new Echo());
		register("cls", new Cls());
		register("quit", new Quit());
		register("say", new Say());
		register("wireframe", new ToggleWireframe());
		register("changelook",new ChangeLook());
		register("normify", new Normify());
		register("depthify", new Depthify());
		register("toggledebug",new ToggleDebug());
	}
	
	//TODO buffer commands rather than executing on-the-spot.
	
	private static void register(String name, Command cmd) {
		if(!commands.containsKey(name)){
			commands.put(name,cmd);
		}else{
			Log.err("Command "+name+" already exists. Unable to register new command.");
		}
	}
	
	public static void runScript(String fname) {
		//Parse a plain text script file
		BufferedReader bufferedReader = null;
		
        try {
            //File f = new File(Client.basedir+fname);
            //Log.info("File "+f.getAbsolutePath());
            
        	//Construct the BufferedReader object
            bufferedReader = new BufferedReader(new FileReader(Client.basedir+fname));
            String line = null;            
            
            while ((line = bufferedReader.readLine()) != null) {
                executeCommand(line);
            }
            
        } catch (FileNotFoundException ex) {
            Log.warn("File "+fname+" not found for execution.");
        } catch (IOException ex) {
        	Log.warn("File "+fname+" not readable for execution.");
        } finally {
            //Close the BufferedReader
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException ex) {
            	Log.warn("File "+fname+" caused an IOException when closing.");
                ex.printStackTrace();
            }
        }
	}

	public static void executeCommand(String line){
		//Log.debug("Command line='"+line+"' ");
		if(line.startsWith("#")||line.startsWith("//")||line.equals("")||line.equals(" "))return;//Ignore comments, blank lines
		line= line.split("//")[0];
		//Anything after // is not interpreted.
		String[] split = line.split(" ");
		if(!commands.containsKey(split[0])){
			Log.warn("Unknown command "+split[0]);
			return;
		}
		commands.get(split[0]).execute(line.replace(split[0]+ " ", ""));
	}
}
