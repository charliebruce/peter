package magma.client.console.commands;

import magma.client.console.Command;
import magma.logger.Log;

public class Echo extends Command {

	@Override
	public void execute(String args) {
		Log.info(args);
	}

}
