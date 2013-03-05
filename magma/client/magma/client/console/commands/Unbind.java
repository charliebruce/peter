package magma.client.console.commands;

import magma.client.console.Command;
import magma.client.input.KeyBinding;
import magma.logger.Log;

public class Unbind extends Command {

	@Override
	public void execute(String args) {
		try {
		KeyBinding.unbind(args);
		} catch (Exception ex){
			Log.warn("Unbind command is incorrectly formatted - arguments "+args +" are invalid");
		}

	}
}
