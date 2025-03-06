package service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class HashService {

    public HashService() {
    }

    /**
     * Convert rawPassword into hash using random salt
     * Salt is added at the end of the password so it is possible to generate same hash when verifying password
     * @param rawPassword
     * @return encodedHash + encodedSalt
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public String hashPassword(String rawPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(rawPassword.toCharArray(), salt, 65536, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] hashed = factory.generateSecret(spec).getEncoded();

        //Convert to Base64 and store hash and salt
        String encodedHash = Base64.getEncoder().encodeToString(hashed);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);

        return encodedHash + encodedSalt;
    }

    /**
     * Convert rawPassword to hash using hashedPasswordFromDb salt
     * @param rawPassword = raw password
     * @param hashedPasswordFromDb = hashed password in db
     * @return encodedHash + encodedSalt
     * @throws Exception
     */
    public String verifyPassword(String rawPassword, String hashedPasswordFromDb) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //Split password to get encoded salt
        String encodedSalt = hashedPasswordFromDb.split("=", 2)[1];

        byte[] salt = Base64.getDecoder().decode(encodedSalt); //Decode salt

        KeySpec spec = new PBEKeySpec(rawPassword.toCharArray(), salt, 65536, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        //Generate hash with stored salt
        byte[] hashed = factory.generateSecret(spec).getEncoded();
        String encodedHash = Base64.getEncoder().encodeToString(hashed);

        return encodedHash + encodedSalt;
    }
}
