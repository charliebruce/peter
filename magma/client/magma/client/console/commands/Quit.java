package magma.client.console.commands;

import magma.client.ClientStrapper;
import magma.client.console.Command;

public class Quit extends Command {

	@Override
	public void execute(String args) {
		ClientStrapper.notifyWindowDestroyed();//HACK TODO FIXME
	}

}
