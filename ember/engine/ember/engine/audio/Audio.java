package ember.engine.audio;

import ember.engine.audio.openal.OpenALSystem;

public class Audio {
private static AudioSystem instance;


	

	public static AudioSystem getAudioSystem() {
		if (instance == null)
			instance = new OpenALSystem();
		return instance;
	}
}
