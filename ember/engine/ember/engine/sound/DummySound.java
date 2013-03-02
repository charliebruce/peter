package ember.engine.sound;

import ember.util.Logger;

public class DummySound implements Sound {
	
	public DummySound(){
	Logger.getInstance().info("[SOUND] Dummy sound driver created.");

}


	public String getName() {
		return "dummy";
	}


	public boolean Initialise() {
		return true;
	}
}
