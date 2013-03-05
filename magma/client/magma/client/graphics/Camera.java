package magma.client.graphics;

public abstract class Camera {

	public int width=1024;

	public int height=768;
	
	//Target FoV width
	//private float fovx=75f;
	
	
	/**
	 * Get the cached projection matrix
	 * @return
	 */
	public abstract float[] projection();


	/**
	 * Rebuild the cached matrix ie for resize.
	 */
	public abstract void recalculateProjection();


	public abstract float[] transformation();


	public abstract void forcetransform(float[] temp);


	public abstract void changelook(String args);


	public abstract void tick();


	public abstract float[] position();
}
