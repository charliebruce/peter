package ember.server.game;

import ember.server.game.entity.Animal;
import ember.server.util.Misc;

/**
 * A piece of code which handles 
 * Animals, civilians and uncontrolled (idle) vehicles would be examples of NPCs
 */
public class AnimalIntelligence {

	/**
	 * The entity (animal) to which this code applies.
	 */
    public Animal associated;
	private int sprite;
    
    public AnimalIntelligence(Animal a) {
        associated = a;

    }

    public void tick() {
        sprite = -1;
        if (!associated.inCombat() && Math.random() > 0.8 && associated.getTravelType() == Entity.TravelType.FOOT) {
            int moveX = (int) (Math.floor((Math.random() * 3)) - 1);
            int moveY = (int) (Math.floor((Math.random() * 3)) - 1);
            int tgtX = associated.getLocation().getX() + moveX;
            int tgtY = associated.getLocation().getY() + moveY;
            sprite = Misc.direction(associated.getLocation().getX(), associated.getLocation().getY(), tgtX, tgtY);
            if (tgtX > associated.maximumCoords.getX() || tgtX < associated.minimumCoords.getX() || tgtY > associated.maximumCoords.getY() || tgtY < associated.minimumCoords.getY()) {
                sprite = -1;
            }
            if (sprite != -1) {
                sprite >>= 1;
                associated.setLocation(Location.location(tgtX, tgtY));
            }
        }
    }


    public int getSprite() {
        return sprite;
    }




}