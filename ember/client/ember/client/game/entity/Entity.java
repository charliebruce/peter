package ember.client.game.entity;

import ember.client.game.ScreenSpaceBox;
import ember.client.game.Commander;
import ember.client.game.Location;
import ember.client.game.WorldItem;
import ember.engine.graphics.Sprite;
/**
 * If it breathes, it's an entity.
 * @author charlie
 *
 */
public class Entity extends WorldItem {
	/**
	 * The "boss" of me. 
	 * This can be a player (normally if a unit, in the case of controlled civilians)
	 * Or an NPC (enemy opponent or team-mate)
	 * Or even an NPC manager (civilian manager etc).
	 */
	public Commander controller;
	/**
	 * And the guy responsible for my creation.
	 * Restored when overridden control is broken.
	 */
	public Commander creator;
	
	/**
	 * Unique identifier
	 */
	public int id;
	
	private Sprite sprite;
	
	public Location position;
	
	public int height; //Relative to ground TODO integrate with Location?

	@Override
	public Sprite getSprite() {
		return sprite;
	}

	@Override
	public ScreenSpaceBox getBoundBox() {
		// TODO This
		return null;
	}

	public void setSprite(Sprite createSprite) {
		sprite = createSprite;
	}
	
	
	
}
