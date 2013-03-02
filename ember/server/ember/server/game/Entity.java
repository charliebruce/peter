package ember.server.game;

import ember.server.game.events.DeathEvent;

/**
 * Any "breathing"/"oxidising" object in the game world. If it consumes oxygen it is an entity.
 * These should have a controller - be it a player, AI, or world manager (Civilians/Animals)
 * This class should (TODO) handle low level health, death, walking queue and other stuff.
 * TODO immobile entities don't need WalkQueue any more. Handle shifts in walk type and stuff.
 * TODO think of sensible handler for TravelTypes allowed
 */

public abstract class Entity {

	public static enum TravelType {
        IMMOBILE,
		FOOT,
        WHEEL,
        WATER,
        AIR,
        TELE,
        AMPHIBIOUS,
        
    }
	private transient World world;
	public Location location;
	private transient int index;
	private transient Location teleportTo;
	private transient Hits hits;
	private transient Graphics lastGraphics;
	private transient Entity interactingWith;
	private transient int combatTurns;
	private transient boolean dead;
	private transient boolean hidden;
	private transient Entity killer;

	private transient boolean immortal;

	public Commander ownedBy;
	public Commander controlledBy;
	
	private TravelType traveltype;
	
	private int hp;
	private int hpMax;
	
	private TravelQueue queue;
	private UpdateFlags updateFlags;
	private Location origin;
	


	public void hit(int damage, Hits.HitType type, Entity hitter){
		if (!immortal){
			//TODO ;
			hp = hp - damage;
			
			//TODO notify
			if (hp < 1){
				world.registerEvent(new DeathEvent(this, hitter));
			}
		}
	}

	public boolean inCombat() {
		// TODO Auto-generated method stub
		return false;
	}

    public void heal(int amount) {
        hp += amount;
        if (hp >= hpMax) {
            hp = hpMax;
        }
    }

	public Entity(World w, Location l) {
		this.location = l;
		this.origin = l;
		this.world = w;
		this.queue = new TravelQueue(this);
	}

	public void teleport(Location location) {
		this.teleportTo = location;//TODO better
	}

	   
    public boolean isAnimating() {
        return getUpdateFlags().isAnimationUpdateRequired();
    }



    public boolean isAutoRetaliating() {
        return true;//TODO
    }

	/**
	 * Hit the given entity with the given damage. Damage type is "normal". 
	 * @param damage
	 * @param hitter
	 */
	public void hit(int damage, Entity hitter){
		hit(damage, Hits.HitType.NORMAL_DAMAGE, hitter);
	}
	
	public Entity getKiller() {
		return killer;
	}
	
	public void resetTeleportTo() {
		this.teleportTo = null;
	}

	public Location getTeleportTo() {
		return this.teleportTo;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return this.index;
	}

	public Graphics getLastGraphics() {
		return lastGraphics;
	}

	public void setLastGraphics(Graphics lastGraphics) {
		this.lastGraphics = lastGraphics;
	}

	public Entity getInteractingWith() {
		return interactingWith;
	}

	public int getCombatTurns() {
		return combatTurns;
	}

	public void resetCombatTurns() {
		combatTurns = 0;
	}

	public void incrementCombatTurns() {
		combatTurns++;
	}

	public abstract int getAttackAnimation();

	public abstract int getAttackSpeed();

	public abstract int getMaxHit();

	public abstract int getDefenceAnimation();

	public abstract int getDeathAnimation();

	public abstract boolean isDestroyed();

	public void setCombatTurns(int i) {
		combatTurns = i;
	}

	public void setDead(boolean b) {
		dead = b;
	}

	public boolean isDead() {
		return dead;
	}

	public void setHidden(boolean b) {
		hidden = b;
	}

	public boolean isHidden() {
		return hidden;
	}

	public World getWorld() {
		return world;
	}

	public void setHits(Hits hits) {
		this.hits = hits;
	}

	public Hits getHits() {
		return hits;
	}

	public TravelQueue getWalkingQueue(){
		return queue;
	}

	public void setTravelType(TravelType newtype) {
		this.traveltype = newtype;
	}

	
	public TravelType getTravelType() {
		return traveltype;
	}

	public void setImmortal(boolean immortal) {
		this.immortal = immortal;
	}

	public boolean isImmortal() {
		return immortal;
	}

    public void setHp(int val) {
        hp = val;
    }
    
    public int getHp(){
    	return hp;
    }
    
    public Location getOriginalLocation() {
        return origin;
    }


    
    public void turnTo(Entity entity) {
        //getUpdateFlags().setFaceToUpdateRequired(true, entity.getClientIndex());
    }

    public void turnTemporarilyTo(Entity entity) {
        //getUpdateFlags().setFaceToUpdateRequired(true, entity.getClientIndex());
        getUpdateFlags().setClearFaceTo(true);
    }

    public void resetTurnTo() {
        getUpdateFlags().setFaceToUpdateRequired(true, 0);
    }

    public void graphics(int id) {
        graphics(id, 0);
    }

    public void graphics(int id, int delay) {
        setLastGraphics(new Graphics(id, delay));
        updateFlags.setGraphicsUpdateRequired(true);
    }

    public void animate(int id) {
        animate(id, 0);
    }

    public void animate(int id, int delay) {
        //setLastAnimation(new Animation(id, delay));
        updateFlags.setAnimationUpdateRequired(true);
    }

    public UpdateFlags getUpdateFlags() {
        return updateFlags;
    }
}
