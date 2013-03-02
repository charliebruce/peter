package ember.engine.crypto;

public abstract class StreamCipher {

	public abstract void seed(byte[] seed);
	
	public abstract void crypt(byte[] input);
	
}
