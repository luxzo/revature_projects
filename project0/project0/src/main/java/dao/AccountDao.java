package dao;

import controller.ConnectionController;
import model.Account;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class AccountDao {

    Connection conn;

    public AccountDao(Connection conn) {
        this.conn = conn;
    }

    //Logger variable
    public static final Logger logger = LoggerFactory.getLogger(AccountDao.class);

    /*
    * Register a new user in database
    * Password is encrypted using PBKDF2 since Bcrypt and Scrypt are not supported but Spring
    * Hashing is 256, which is done in AccountService class
    *
    * Todo handle exceptions
     */
    public Account registerNewAccount(Account newAccount) throws PSQLException {
        String newAccountSql = ("INSERT INTO public.accounts(email, password, role_id) VALUES (?, ?, ?)");

        try (Connection conn = ConnectionController.getConnection()) {
            PreparedStatement pstmNewAccount = conn.prepareStatement(newAccountSql, Statement.RETURN_GENERATED_KEYS);
            pstmNewAccount.setString(1, newAccount.getEmail());
            pstmNewAccount.setString(2, newAccount.getPassword());
            pstmNewAccount.setInt(3, newAccount.getRole_id());
            pstmNewAccount.execute();

            logger.info("INSERT INTO accounts(email, password, role_id) VALUES ?, ?, ?");

            //Retrieve auto generated accountId
            int newAccountId;
            try (ResultSet generatedAccountId = pstmNewAccount.getGeneratedKeys()) {
                if (generatedAccountId.next()) {
                    newAccountId = generatedAccountId.getInt(1);
                    newAccount.setAccountId(newAccountId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (newAccount != null)
            return newAccount;
        return null;
    }
}
