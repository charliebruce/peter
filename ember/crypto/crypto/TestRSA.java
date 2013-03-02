package crypto;

import ember.engine.crypto.RSADecrypter;
import ember.engine.crypto.RSAEncrypter;

public class TestRSA {

	public static void main(String[] args) throws Exception{
		
		byte[] plaintext = new byte[]{(byte)43, (byte)27};
		
		System.out.println("Plaintext:" + plaintext[0]+","+plaintext[1]);
		RSAEncrypter enc = new RSAEncrypter();
		
		byte[] encrypted = enc.encrypt(plaintext);
		System.out.println("Encrypted:" + encrypted[0]);
		
		RSADecrypter dec = new RSADecrypter();
		
		byte[] decrypted = dec.decrypt(encrypted);
		System.out.println("Plaintext again:" + decrypted[0]+","+decrypted[1]);
		
		
		
	}
}
