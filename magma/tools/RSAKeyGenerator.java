
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
* Generates RSA key pairs.
*
*/
public class RSAKeyGenerator
{
    
    private static KeyPair keyPair;
    private static KeyPairGenerator keyGen;
    private static FileOutputStream publicKeyStream;
	private static FileOutputStream privateKeyStream;
    
    public static void main (String[] args) throws Exception
    {
        keyGen = KeyPairGenerator.getInstance("RSA");
        System.out.println("Generating RSA key pair...");
        //skeyGen.initialize(1024);
        keyGen.initialize(1024, new SecureRandom());
        keyPair = keyGen.generateKeyPair();
        RSAPublicKeySpec publicSpec = KeyFactory.getInstance("RSA").getKeySpec(keyPair.getPublic(), RSAPublicKeySpec.class);
        RSAPrivateKeySpec privateSpec = KeyFactory.getInstance("RSA").getKeySpec(keyPair.getPrivate(), RSAPrivateKeySpec.class);
        publicKeyStream = new FileOutputStream("data/public.key.new");
        privateKeyStream = new FileOutputStream("data/private.key.new");
        save(publicKeyStream, publicSpec.getModulus(), publicSpec.getPublicExponent());
        save(privateKeyStream, privateSpec.getModulus(), privateSpec.getPrivateExponent());
        publicKeyStream.close();
        privateKeyStream.close();
        System.out.println("Done.");
    }
    
    private static void save(FileOutputStream out, BigInteger modulus, BigInteger exponent) throws Exception
    {
        ObjectOutputStream oout = new ObjectOutputStream(out);
        oout.writeObject(modulus);
        oout.writeObject(exponent);
        oout.close();
    }

}