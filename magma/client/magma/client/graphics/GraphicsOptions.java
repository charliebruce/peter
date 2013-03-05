package magma.client.graphics;

public class GraphicsOptions {

	/*
	 * Whether or not Frame Buffer Objects are available.
	 * Basically render-to-texture targets
	 * DX=render buffer objects
	 */
	public boolean useFbo = true;
	
	/*
	 * Precision of FBOs (where applicable) chosen with this.
	 */
	public boolean hdr = true;
	
	/*
	 * The greatest dimension a texture can have.
	 * Usually a multiple of 2 except for FBOs.
	 * Safe to presume 1024x1024 will always be available.
	 * 512 is nice and safe
	 * Probably safe to assume that the monitor's resolution will also be available for modern GPUs
	 */
	public int maxTexRes;

	/*
	 * The maximum number of texture sampler units on card?
	 * GL_MAX_TEXTURE_IMAGE_UNITS
	 */
	public int maxTexUnits;

	public float shaderLangVer;

	public boolean useGeom = true;

	/*
	 * Non power of two textures
	 */
	public boolean nPoT = true;

	public int maxClrAttachments = 0;

	public int maxDrawBuffers = 0;

}
