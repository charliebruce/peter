package magma.client.console.commands;

import magma.client.console.Command;
import magma.client.input.KeyBinding;
import magma.logger.Log;

public class Bind extends Command {

	@Override
	public void execute(String args) {
		try {
		String[] split = args.split(" ");
		KeyBinding.bind(split[0],args.replace(split[0]+" ", ""));
		} catch (Exception ex){
			Log.warn("Bind command is incorrectly formatted - arguments "+args +" are invalid");
		}

	}
}
