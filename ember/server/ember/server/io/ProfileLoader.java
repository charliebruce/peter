package ember.server.io;

import ember.server.exceptions.BannedException;
import ember.server.game.Profile;
import ember.server.game.ProfileDetails;

public interface ProfileLoader {
	
	public abstract Profile load(ProfileDetails d) throws InvalidDetailsException, BannedException;
	
	
}
