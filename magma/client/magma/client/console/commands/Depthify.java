package magma.client.console.commands;

import magma.client.console.Command;
import magma.client.graphics.Graphics;
import magma.logger.Log;

public class Depthify extends Command {

	@Override
	public void execute(String args) {
		Graphics.depthify=!Graphics.depthify;
		Log.debug("Visualise depth buffer: "+Graphics.depthify);
	}

}
