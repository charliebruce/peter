package ember.engine.graphics;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

/**
 * The "back buffer" use for software rendering. 
 * This could potentially be used elsewhere too. 
 * @author charlie
 *
 */
public class GraphicsBuffer extends AbstractGraphicsBuffer {


	/**
	 * The last canvas we were made aware of.
	 */
	private Canvas canvas;
	
	private Rectangle rectangle;
	private BufferedImage image;
	private Shape clip;

	
	public final void init(Canvas canvas) {
		
		this.canvas = canvas;
		rectangle = new Rectangle();
		width = this.canvas.getSize().width;
		height = this.canvas.getSize().height;
		
		if (width<1 || height <1){
			//Resize has been given bad parameters, compensate. TODO less hacky. 
			width = 1;
			height = 1;
			System.out.println("Bad dimensions given to GraphicsBuffer");
		}
		
		pixels = new int[width * height];
		DataBufferInt databufferint = new DataBufferInt(pixels, pixels.length);
		DirectColorModel directcolormodel = new DirectColorModel(32, 0xff0000, 65280, 255);
		java.awt.image.WritableRaster writableraster = Raster.createWritableRaster(directcolormodel.createCompatibleSampleModel(width, height), databufferint, null);
		//TODO understand this better.
		image = new BufferedImage(directcolormodel, writableraster, false, new Hashtable<Object,Object>());
	}
	int count = 0;
	public final void draw(int x, int y, Graphics g) {
		g.drawImage(image, x, y, canvas);
		count++;
		boolean captureImages = false;
		
		
		try {
			if (captureImages)
			writeImageToJPG(new File("C:\\output\\img"+count+".jpg"), image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

    public static void writeImageToJPG (File file,BufferedImage bufferedImage) throws IOException
 {
     ImageIO.write(bufferedImage,"jpg",file);
 }

	final void clip(int x, int y, int width, int height, Graphics g) {
		clip = g.getClip();
		rectangle.width = width;
		rectangle.x = x;
		rectangle.y = y;
		rectangle.height = height;
		g.setClip(rectangle);
		g.drawImage(image, 0, 0, canvas);
		g.setClip(clip);
	}

	@Override
	Graphics getGraphics() {
		return image.getGraphics();
	}

}
