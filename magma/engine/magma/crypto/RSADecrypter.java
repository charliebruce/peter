package magma.crypto;

import java.io.FileInputStream;

import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.RSAPrivateKeySpec;

import javax.crypto.Cipher;

/**
 * Serves to load the private key and decrypt login block.
 * @author Charlie
 *
 */

public class RSADecrypter {

	    private PrivateKey key;
	    private Cipher cipher;

	    public RSADecrypter() throws Exception
	    {
	        FileInputStream in = new FileInputStream("data/private.key");
	        ObjectInputStream oin = new ObjectInputStream(in);
	        BigInteger modulus = (BigInteger) oin.readObject();
	        BigInteger exponent = (BigInteger) oin.readObject();
	        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
	        key = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
	        cipher = Cipher.getInstance("RSA");
	        cipher.init(Cipher.DECRYPT_MODE, key);
	    }

	    public byte[] decrypt(byte[] source) throws Exception
	    {
	        return cipher.doFinal(source);
	    }
	
}
