package service;

import dao.AccountDao;
import model.Account;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AccountService {
    private final AccountDao accountDao;
    public static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Account registerNewAccount(String email, String password, int roleId) throws PSQLException {
        String hashedPass = "";
        Account newAccount = new Account();
        try {
            hashedPass = hashPassword(password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error(e.getMessage());
        }
        if ((email != null || !email.equals(""))  && (password != null || !password.equals("")) && roleId != 0) {
            newAccount.setEmail(email);
            newAccount.setPassword(hashedPass);
            newAccount.setRole_id(roleId);
            return accountDao.registerNewAccount(newAccount);
        }
        else {
            logger.error("\"error\": \"Missing email, password or role id\"");
        }
//        return accountDao.registerNewAccount(newAccount);
        return null;
    }


    /*
     * Encrypt password using 256 algorithm
     * iterationCount could be less than 65536, but also less secure
     * @param byte[] retrieves encoded password, it is returned
     */
    public String hashPassword(String pass) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, 65536, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] hashed = factory.generateSecret(spec).getEncoded();

        //Todo delete 2 following lines since they were for developing purpose
        String hashedPass = Base64.getEncoder().encodeToString(hashed);
        logger.info(hashedPass);

        return Base64.getEncoder().encodeToString(hashed);
    }
}
