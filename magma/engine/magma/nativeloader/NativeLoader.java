package magma.nativeloader;

import magma.logger.Log;

public class NativeLoader {

	public static void ensureNativeLibs(){
		Log.info("Running "+System.getProperty("os.name")+" "+System.getProperty("os.arch")+", version "+System.getProperty("os.version"));
		
	}
}
