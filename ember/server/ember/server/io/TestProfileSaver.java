package ember.server.io;

import ember.server.game.Profile;
import ember.util.Logger;

public class TestProfileSaver implements ProfileSaver {

	@Override
	public boolean save(Profile p) {
		// TODO Auto-generated method stub
		Logger.getInstance().info("Nullsaver saving profile" + p.getUsername());
		return false;
	}



}
