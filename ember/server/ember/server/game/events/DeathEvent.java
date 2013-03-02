package ember.server.game.events;

import ember.server.event.OnceOnlyEvent;
import ember.server.game.Entity;

/**
 * This event fires on the death of an entity.
 * This handles corpse creation, client notification, statistics etc.
 * @author charlie
 *
 */
public class DeathEvent extends OnceOnlyEvent {

	//public Entity target;
	
	public DeathEvent(Entity theDyee, Entity killedBy) {
		if (theDyee.controlledBy != theDyee.ownedBy){
			//Controlled entities incur a loss for the owner and controller
		}
		else
		{
			//Normally entities only incur a loss for their controller
			//TODO register in PlayerStats
		}
		// TODO Auto-generated constructor stub
	}

	public void execute() {
		//TODO add

	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return true;
	}


}
