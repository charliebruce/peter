package ember.server.game.entity;

import ember.server.game.Entity;
import ember.server.game.Location;
import ember.server.game.World;

/**
 * When the world kills an entity for any reason - gas, volcano, etc.
 * TODO tidy up all these silly requirements
 * @author charlie
 *
 */
public class Environment extends Entity {

	public Environment(World w, Location l) {
		super(w, l);
		this.setImmortal(true);
	}

	@Override
	public int getAttackAnimation() {
		return 0;
	}

	@Override
	public int getAttackSpeed() {
		return 0;
	}

	@Override
	public int getMaxHit() {
		return -1;
	}

	@Override
	public int getDefenceAnimation() {
		return 0;
	}

	@Override
	public int getDeathAnimation() {
		return 0;
	}

	@Override
	public boolean isDestroyed() {
		return false;
	}
	

}
