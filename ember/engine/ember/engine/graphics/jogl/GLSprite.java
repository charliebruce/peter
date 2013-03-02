package ember.engine.graphics.jogl;

import ember.engine.graphics.Sprite;

public class GLSprite extends Sprite {

	GLToolkit gltoolkit;
	int width;
	int height;
	
	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	GLSprite(GLToolkit gltk, int w, int h){
		gltoolkit = gltk;
		width = w;
		height = h;
	}
}
