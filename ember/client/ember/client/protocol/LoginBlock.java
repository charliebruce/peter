package ember.client.protocol;

import ember.engine.crypto.RSAEncrypter;
import ember.engine.crypto.SeedGenerator;
import ember.util.Logger;

public class LoginBlock {
	
	public byte[] data;

	public LoginBlock(String username, String password) {
		

		//Make them into bytes for encryption.
		byte[] undata = username.getBytes();
		byte[] pwdata = password.getBytes();
		
		//2 bytes for length, 64 for cipher key, remainder for un+pw;
		byte[] plaintextLoginBlock = new byte[2+64+undata.length+pwdata.length];
		
		//Write the username and its length.
		plaintextLoginBlock[0] = (byte) username.length();
		for (int i = 0; i<undata.length;i++){
			plaintextLoginBlock[i+1]=undata[i];
		}
		
		//Write the password and its length.
		plaintextLoginBlock[username.length()+1] = (byte) pwdata.length;
		for (int i = 0; i<pwdata.length;i++){
			plaintextLoginBlock[i+2+username.length()]=pwdata[i];
		}
		
		//Generate a cipher seed.
		byte[] seed = SeedGenerator.get64bit();
		
		//Append the seed to the login block.
		for (int i = 0; i<seed.length;i++){
			plaintextLoginBlock[undata.length+pwdata.length+2+i] = seed[i];
		}
		
		//Logging to check transfer correct.
		Logger.getInstance().info("Seed :"+seed[0]);
		//TODO Stream Cipher experimental. 

		//Prepare to encrypt the login block.
		data = null;
		RSAEncrypter rsae = null;
		
		//Encrypt the block and save it.
		try {
			rsae = new RSAEncrypter();
			data = rsae.encrypt(plaintextLoginBlock);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
