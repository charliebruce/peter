package ember.server.util;

import ember.server.game.Entity;
import ember.server.game.Location;
import ember.server.game.World;

public class ProjectileManager {

	private World world;
	
	public ProjectileManager(World w){
		world = w;
	}
	
	public void fire(Location source, Location dest, int gfx, int angle ,int speed, int startHeight, int endHeight, Entity lockon) {
		int offsetX = (source.getX() - dest.getX()) * -1;
		int offsetY = (source.getY() - dest.getY()) * -1;
		
		//TODO this
		for(Entity p : world.getEntityList()) {
			if(p.getLocation().withinDistance(source) && p.getLocation().withinDistance(dest)) {
				//p.getActionSender().sendProjectile(source, dest, offsetX, offsetY, gfx, angle,  speed, startHeight, endHeight, lockon);
			}
		}
	}
}
