package ember.server.game.entity.unit;

import ember.server.game.Location;
import ember.server.game.World;

public abstract class Tank extends Vehicle {

	public Tank(World w, Location l) {
		super(w, l);
		this.setTravelType(TravelType.WHEEL);
	}

	
}
