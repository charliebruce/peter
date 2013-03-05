package magma.client.console.commands;

import magma.client.console.Command;
import magma.client.graphics.Graphics;

public class ToggleWireframe extends Command{

	@Override
	public void execute(String args) {
		Graphics.wireframe = !Graphics.wireframe;
	}

}
