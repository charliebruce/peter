package ember.engine.graphics.sw;

import ember.engine.graphics.Sprite;

public abstract class SWSprite extends Sprite {

	SWToolkit swtoolkit;
	int width;
	int height;
	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return height;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return width;
	}
	
	SWSprite(SWToolkit swtk, int w, int h){
		swtoolkit = swtk;
		width = w;
		height = h;
	}

}
