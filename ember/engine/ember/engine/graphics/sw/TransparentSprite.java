package ember.engine.graphics.sw;

public class TransparentSprite extends SWSprite {

	int[] pixels;
	
	
	
	
	TransparentSprite(SWToolkit swtk, int[] pixels, int w, int h) {
		super(swtk, w, h);
		
		this.pixels = pixels;
		
		//TODO bounds check against [w*h]
		// TODO Auto-generated constructor stub
	}

}
