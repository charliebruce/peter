package magma.client.graphics.opengl;

import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import magma.client.Client;
import magma.logger.Log;

import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

public class GLFont {

	private int textureid;
	private int[] widths;
	private String filename;
	public int lineheight=10;
	
	public GLFont(String name){
		//This will be used when actually loading into GL
		filename = "fonts/"+name+".dds";
		
		//Lengths and data load, to enable fonts to "crush up" nicely.
		
	}
	
	
	public void load(GL2 gl){
		//Load the font's texture itno memory
		int[] target = new int[1];
		gl.glGenTextures(1, target, 0);


		try {
			File f = new File(Client.basedir+filename);

			TextureData data = TextureIO.newTextureData(gl.getGLProfile(), f, true, filename.substring(filename.length()-3,filename.length()));

			gl.glBindTexture(GL.GL_TEXTURE_2D, target[0]);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);//Default to GL_Nearest for performance. GL_Linear bilerps
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);//Other option is to GL_NEAREST it up and clip to nearest pixel
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL2.GL_GENERATE_MIPMAP, GL.GL_TRUE);
			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, data.getInternalFormat(), data.getWidth(), data.getHeight(), 0, data.getPixelFormat()/*GL2.GL_RGBA/**/, GL.GL_UNSIGNED_BYTE, data.getBuffer());
			//texmap.put(string, target[0]);
			Log.debug("Loaded file " + f.getAbsolutePath()+ " into font-texture "+target[0]);
			textureid = target[0];
		}
		catch (IOException exc) {
			exc.printStackTrace();
			System.exit(1);
		}
	}


	public int getLength(char c) {
		// TODO Auto-generated method stub
		return 0;
	}


	public float texcoordu(char t, boolean amRightHand, boolean amBottomEdge) {
		return 0;
	}
	public float texcoordv(char t, boolean amRightHand, boolean amBottomEdge) {
		return 0;
	}
	
}
