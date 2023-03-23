package org.wso2.sample.user.store.manager;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PassWordUtil {

    public static final String ALGORITHM = "PBKDF2WithHmacSHA1";

    public static final int SALT_SIZE = 24;
    public static final int HASH_SIZE = 64;
    public static final int ITERATIONS = 1000;
    public static final int ITERATION_INDEX = 0;
    public static final int SALT_INDEX = 1;
    public static final int PBKDF2_INDEX = 2;
    

    public static String generateHashPwd(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        return generateHashPwd(password.toCharArray());
    }

    public static String generateHashPwd(char[] password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        random.nextBytes(salt);

        byte[] hash = pbkdf2(password, salt, ITERATIONS, HASH_SIZE);
        return ITERATIONS + ":" + toHex(salt) + ":" +  toHex(hash);
    }

    public static boolean validatePassword(String password, String correctHash)
            throws NoSuchAlgorithmException, InvalidKeySpecException
        {
            return validatePassword(password.toCharArray(), correctHash);
        }

    public static boolean validatePassword(char[] password, String correctHash)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String[] params = correctHash.split(":");
        int iterations = Integer.parseInt(params[ITERATION_INDEX]);

        byte[] salt = fromHex(params[SALT_INDEX]);
        byte[] hash = fromHex(params[PBKDF2_INDEX]);
        byte[] testHash = pbkdf2(password, salt, iterations, hash.length);

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
            diff |= hash[i] ^ testHash[i];
        
        return diff == 0;
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

    private static byte[] fromHex(String hex)
    {
        byte[] binary = new byte[hex.length() / 2];
        for(int i = 0; i < binary.length; i++)
        {
            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return binary;
    }

    private static String toHex(byte[] array)
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0) 
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }

    
    public static void main(String[] args)
    {
	try
	{
	    boolean valid = PassWordUtil.validatePassword(
		    "KleinEnGroot", "1000:d05e010458dec35e0129a8990c01c85a65e0a773aa32bf63:195182c10428bf72ede8c907b708e526e1fdc7fdb91effa958bd4f833ec9b67484bba78204a1b64030b0180fb1532ba11006b9ad96f619b353a89ca41a31ad9e");
	    System.out.println(valid);
	} catch (NoSuchAlgorithmException | InvalidKeySpecException e)
	{
	    e.printStackTrace();
	}
	
    }
}
