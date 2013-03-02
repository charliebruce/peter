package ember.engine.sound;

import ember.engine.CVar;
import ember.engine.sound.joal.JOALSound;
import ember.engine.sound.lwjgl.LWJGLSound;
import ember.util.Logger;

public class SoundDrivers {
	
	static Sound implementation;
	
	static {	    

			try {	    
			    Class.forName("ember.engine.sound.DummySound");
			    //Use for dedicated or null implementation.
			} catch (Throwable e) {
			    Logger.getInstance().debug("[SOUND] Dummy not found.");
			}
			
			try {
				Class.forName("org.lwjgl.openal.AL");
				Class.forName("ember.engine.sound.lwjgl.LWJGLSound");
			} catch (Throwable e) {
				//
				 Logger.getInstance().debug("[SOUND] OpenAL not found.");
			}
			
			// Preferred driver
			try {
				Class.forName("net.java.games.joal.AL");
				Class.forName("jake2.sound.joal.JOALSound");
			} catch (Throwable e) {
				// ignore the joal driver if runtime not in classpath
				 Logger.getInstance().debug("[SOUND] JOGL AL not found.");
			}
		
	};
	
	
	/**
	 * Switches to the specific sound driver.
	 */
	public static void useDriver(String driverName) {
		if (driverName.equals("dummy"))
		{
			implementation = new DummySound();
		}
		if (driverName.equals("lwjgl"))
		{
			implementation = new LWJGLSound();
		}
		if (driverName.equals("joal"))
		{
			implementation = new JOALSound();
		}
		
		
		
		
	}
	
	/**
	 * Initializes the sound module.
	 */
	public static void Initialise() {
		Logger l = Logger.getInstance();
		l.info("[SOUND] Initialising.");
		
		CVar cv = CVar.Get("s_soundenabled");
		if (cv.value == 0.0f) {
			l.info("[SOUND] Disabled by s_soundenabled");
			useDriver("dummy");
			return;			
		}
		

		// set the last registered driver as default
		String defaultDriver = "dummy";
		String s_impl = CVar.Get("s_implementation").string;
		
		if (s_impl.isEmpty()){s_impl=defaultDriver;}
		useDriver(s_impl);
		
		if (implementation.Initialise()) {
			// driver ok
			CVar.Set("s_implementation", implementation.getName());
		} else {
			l.warning("Error initialising " + s_impl  + ". Falling back to dummy.");
			useDriver("dummy");
			implementation.Initialise();
			CVar.Set("s_implementation",implementation.getName());
		}
		
		l.info("[SOUND] Using driver " + implementation.getName() + ".");
		//StopAllSounds();
	}
	

	
	/*	
	 * This is used, when resampling to this default sampling rate is activated 
	 * in the wave file loader. It is placed here that sound implementors can override 
	 * this one day.
	 */
	public static int getDefaultSampleRate()
	{
		return 44100;
	}
}