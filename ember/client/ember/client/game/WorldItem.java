package ember.client.game;

import ember.engine.graphics.Sprite;

public abstract class WorldItem {

	public abstract Sprite getSprite();
	public abstract ScreenSpaceBox getBoundBox();
	
}
