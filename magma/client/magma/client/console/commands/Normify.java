package magma.client.console.commands;

import magma.client.console.Command;
import magma.client.graphics.Graphics;
import magma.logger.Log;

public class Normify extends Command {

	@Override
	public void execute(String args) {
		Graphics.normify=!Graphics.normify;
		Log.debug("Visualise normal buffer: "+Graphics.normify);
	}

}
