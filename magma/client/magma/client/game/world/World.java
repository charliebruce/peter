package magma.client.game.world;

import magma.logger.Log;

public class World {

	public String mapname;
	
	//Units
	
	//shit
	public float[] geometrybuffer;
	
	//Dimensions of the world in squares. Implies geometry buffer is [width+1][height+1]
	public int width=1,height=1;
	
	public void loadgeometry(){
		//Load the world's geometry and texture information.
		
		//Currently we only use the VBO method (no static pipeline)
		
		//So this means I only have to happen once
		
		//input format is left-right then top-bottom (compass points) I THINK!
		//defining height,texture blend at each point. 
		//Since we are reading from a heightmap
		//This is less compatible with opengl but more compact
		//so we have to "expand" this for OpenGL into triangle strips
		
		geometrybuffer = new float[]{0,0,0,0,0,0,0,0};
		Log.debug("Geometry buffer loaded with 8 null values.");
	}
	public float[] getgeometry(){
		//Return the geometry buffer.
		return geometrybuffer;
	}
	
	public void unloadgeometry(){
		//Clear my geometry to release some memory.
		geometrybuffer = null;
	}
	
}
