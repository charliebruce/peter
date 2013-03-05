package magma.client.console.commands;

import magma.client.console.Command;
import magma.client.graphics.Graphics;

public class ChangeLook extends Command {

	@Override
	public void execute(String args) {
		Graphics.camera.changelook(args);
	}

}
