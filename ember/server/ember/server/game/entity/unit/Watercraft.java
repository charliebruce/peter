package ember.server.game.entity.unit;

import ember.server.game.Location;
import ember.server.game.World;

public abstract class Watercraft extends Vehicle {

	public Watercraft(World w, Location l) {
		super(w, l);
		this.setTravelType(TravelType.WATER);
	}

}
