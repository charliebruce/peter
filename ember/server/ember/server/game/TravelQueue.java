package ember.server.game;

import ember.server.ServerConstants;
import ember.server.util.Misc;

/**
 * Handles movement for a given entity.
 * This should be memory and calculation-efficient.
 * @author charlie
 *
 */
public class TravelQueue {

	private class Point {
		private int x;
		private int y;
		private int dir;
	}


	public int wQueueReadPtr = 0;
	public int wQueueWritePtr = 0;
	public Point[] walkingQueue = new Point[ServerConstants.WALKING_QUEUE_MAX_LENGTH];
	
	private Entity entity;

	public TravelQueue(Entity e) {
		this.entity = e;
		for (int i = 0; i < ServerConstants.WALKING_QUEUE_MAX_LENGTH; i++) {
			walkingQueue[i] = new Point();
			walkingQueue[i].x = 0;
			walkingQueue[i].y = 0;
			walkingQueue[i].dir = -1;
		}
		reset();
	}


	public void reset() {
		walkingQueue[0].x = entity.getLocation().getX();
		walkingQueue[0].y = entity.getLocation().getY();
		walkingQueue[0].dir = -1;
		wQueueReadPtr = wQueueWritePtr = 1;
	}

	public void addStepToWalkingQueue(int x, int y) {
		int diffX = x - walkingQueue[wQueueWritePtr - 1].x, diffY = y - walkingQueue[wQueueWritePtr - 1].y;
		int dir = Misc.direction(diffX, diffY);
		if (wQueueWritePtr >= ServerConstants.WALKING_QUEUE_MAX_LENGTH) {
			return;
		}
		if (dir != -1) {
			walkingQueue[wQueueWritePtr].x = x;
			walkingQueue[wQueueWritePtr].y = y;
			walkingQueue[wQueueWritePtr++].dir = dir;
		}
	}
	  public void addStepToWalkingQueue(int x, int y, int dir) {
	        if (wQueueWritePtr >= ServerConstants.WALKING_QUEUE_MAX_LENGTH) {
	            return;
	        }
	        if (dir != -1) {
	            walkingQueue[wQueueWritePtr].x = x;
	            walkingQueue[wQueueWritePtr].y = y;
	            walkingQueue[wQueueWritePtr++].dir = dir;
	        }
	    }

	public void doNextMovement() {
		
		//entity.getSprites().setSprites(-1, -1);
		
		Location oldLocation = entity.getLocation();
		int walkDir = getNextWalkingDirection();
		int runDir = -1;
		//entity.getSprites().setSprites(walkDir, runDir);
		//TODO move meeee
		
	}
	public void GoTo(int CoordX, int CoordY) {

		entity.getWalkingQueue().reset();
		int[][] path = findRoute(CoordX, CoordY);
		
		if(path != null) {
			for(int i=0; i<path.length; i++) {
				addToWalkingQueueFollow(path[i][0], path[i][1]);
			}
		}
	}
	public void addToWalkingQueue(int x, int y) {
		int diffX = x - walkingQueue[wQueueWritePtr - 1].x, diffY = y - walkingQueue[wQueueWritePtr - 1].y;
		int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (int i = 0; i < max; i++) {
			if (diffX < 0)
				diffX++;
			else if (diffX > 0)
				diffX--;
			if (diffY < 0)
				diffY++;
			else if (diffY > 0)
				diffY--;
			addStepToWalkingQueue(x - diffX, y - diffY);
		}
	}
	public void addToWalkingQueueFollow(int x, int y) {
			int diffX = x - walkingQueue[wQueueWritePtr - 1].x, diffY = y - walkingQueue[wQueueWritePtr - 1].y;
			int max = Math.max(Math.abs(diffX), Math.abs(diffY));
			for (int i = 0; i < max; i++) {
				if (diffX < 0)
					diffX++;
				else if (diffX > 0)
					diffX--;
				if (diffY < 0)
					diffY++;
				else if (diffY > 0)
					diffY--;
				int toQueX = x - diffX;
				int toQueY = y - diffY;
				if (toQueX != x || toQueY != y)
					addStepToWalkingQueue(toQueX, toQueY);
			}
	}	
	private int[][] findRoute(int toX, int toY) {
		//TODO pathfinder for map.
		return new int[][] { new int[] { toX, toY } };
	}

	private int getNextWalkingDirection() {
		if (wQueueReadPtr == wQueueWritePtr) {
			return -1;
		}
		int dir = walkingQueue[wQueueReadPtr++].dir;
		//int xdiff = Misc.DIRECTION_DELTA_X[dir];
		//int ydiff = Misc.DIRECTION_DELTA_Y[dir];
		//Location newLocation = Location.location(entity.getLocation().getX() + xdiff, entity.getLocation().getY() + ydiff, entity.getLocation().getZ());
		//entity.setLocation(newLocation);
		return dir;
	}


}
