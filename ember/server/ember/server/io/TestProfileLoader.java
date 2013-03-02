package ember.server.io;

import ember.server.game.Profile;
import ember.server.game.ProfileDetails;
import ember.util.Logger;

public class TestProfileLoader implements ProfileLoader {

	@Override
	public Profile load(ProfileDetails d) throws InvalidDetailsException {
		// TODO Auto-generated method stub
		Logger.getInstance().info("Nulloader loading profile "+d.getUsername());
		Profile pr = new Profile(d);
		return pr;
	}

}
