package dao;

import controller.AccountController;
import controller.ConnectionController;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AccountService;

import java.sql.*;

public class LoginDao {
    //Logger variable
    public static final Logger logger = LoggerFactory.getLogger(LoginDao.class);
    Connection conn = ConnectionController.getConnection();
    AccountDao accountDao = new AccountDao(conn);
    AccountService accountService = new AccountService(accountDao);
    AccountController accountController = new AccountController(accountService);

    public LoginDao() throws SQLException { }
}
