package magma.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

/**
 * Loads the public key and encrypt the login information.
 * @author Charlie
 *
 */
public class RSAEncrypter{

    private Key key;
    private Cipher cipher;
    
    public RSAEncrypter() throws Exception {
    	File f = new File("data/public.key");
    	FileInputStream in;
    	if (f.exists()){
    		in = new FileInputStream("data/public.key");
        }
    	else {
    		in = new FileInputStream("../data/public.key");
            
    	}
    	//TODO HACKHACKHACK
        ObjectInputStream oin = new ObjectInputStream(in);
        BigInteger modulus = (BigInteger) oin.readObject();
        BigInteger exponent = (BigInteger) oin.readObject();
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
        key = KeyFactory.getInstance("RSA").generatePublic(keySpec);
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
    }
    
    public byte[] encrypt(byte[] in){
    	
    	try {
			return cipher.doFinal(in);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return null;
		} catch (BadPaddingException e) {
			e.printStackTrace();
			return null;
		}
    }
}