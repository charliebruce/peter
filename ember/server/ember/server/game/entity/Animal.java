package ember.server.game.entity;

import ember.server.game.AnimalIntelligence;
import ember.server.game.Entity;
import ember.server.game.Location;
import ember.server.game.World;

/**
 * Self-explanatory, really.
 * @author charlie
 */
public abstract class Animal extends Entity {

	public Location maximumCoords;
	public Location minimumCoords;

	private AnimalIntelligence ai;
	
	public Animal(World w, Location l) {
		super(w, l);
		
        setTravelType(TravelType.FOOT);
        maximumCoords = new Location(w.width, w.height);
        minimumCoords = new Location (0,0);
        
        ai = new AnimalIntelligence(this);

	}
	
	public void tick(){
		ai.tick();
	}

	
}
