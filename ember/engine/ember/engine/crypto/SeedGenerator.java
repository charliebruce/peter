package ember.engine.crypto;

import java.security.SecureRandom;

public class SeedGenerator {

	public static byte[] get64bit(){
		return SecureRandom.getSeed(64);
		
	}
}
