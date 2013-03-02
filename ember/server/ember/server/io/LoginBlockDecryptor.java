package ember.server.io;

import ember.engine.crypto.RSADecrypter;
import ember.util.Logger;

public class LoginBlockDecryptor {

	String username;
	String password;
	byte[] seed;
	
	/**
	 * Feed this the raw, encrypted login block and it makes username, password and seed easily accessible.
	 * @param data
	 * @throws Exception
	 */
	LoginBlockDecryptor(byte[] data) throws Exception {
		
		byte[] theBlock;
		
		RSADecrypter rsad = new RSADecrypter();
		theBlock = rsad.decrypt(data);
		
		Logger.getInstance().info("Received encrypted data successfully.");
		
		int unlength = theBlock[0];
		byte[] temp = new byte[unlength];
		for (int i = 0; i<unlength;i++){
			temp[i] = theBlock[i+1];
		}
		username = new String(temp);
		
		int pwlength=theBlock[unlength+1];
		
		temp = new byte[pwlength];
		for (int i = 0; i<pwlength;i++){
			temp[i] = theBlock[i+2+unlength];
		}
		password = new String(temp);
		//TODO check characters. 
		seed = new byte[64];
		for (int i = 0; i<seed.length;i++){
			seed[i] = theBlock[i+2+unlength+pwlength];
		}

		
	}
	
	public String getUsername(){
		return username;
	}
	public String getPassword(){
		return password;
	}

	public byte[] getSeed() {
		return seed;
	}
	
}
