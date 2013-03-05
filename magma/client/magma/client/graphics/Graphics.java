package magma.client.graphics;

import magma.client.graphics.opengl.GLCamera;

public class Graphics {


	public static Display display;
	
	//public static Renderer renderer;

	public static boolean isFullscreen;

	//The specification in use.
	public static GraphicsOptions current;
	
	//the maximum spec supported by hardware.
	public static GraphicsOptions hwspec = new GraphicsOptions();
	//New is allowed - loaded by hardware checker.
	
	public static Camera camera;
	
	public static boolean wireframe = false;

	public static boolean captureNextFrame = false;
	
	public static void goWireframe(){
		wireframe = true;
	}
	public static void noWireframe(){
		wireframe = false;
	}
	
	public static void initialise(){

		current = hwspec;
		camera = new GLCamera();
		
	}
	
	
	public static boolean normify = false;

	public static boolean depthify = false;

	public static boolean showdebug = true;
	
}
