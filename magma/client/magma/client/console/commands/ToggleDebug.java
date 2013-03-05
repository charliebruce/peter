package magma.client.console.commands;

import magma.client.console.Command;
import magma.client.graphics.Graphics;
import magma.logger.Log;

public class ToggleDebug extends Command {

	@Override
	public void execute(String args) {
		Graphics.showdebug=!Graphics.showdebug;
		Log.info("Show Debug: "+Graphics.showdebug);
	}

}
