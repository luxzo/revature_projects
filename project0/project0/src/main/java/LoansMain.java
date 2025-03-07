import controller.AccountController;
import controller.ConnectionController;
import controller.LoginController;
import controller.StartController;
import dao.AccountDao;
import dao.AccountRoleDao;
import dao.UserDao;
import io.javalin.Javalin;
import service.AccountService;

import java.sql.Connection;
import java.sql.SQLException;

public class LoansMain {
    public static void main(String[] args) throws SQLException {
        Connection conn = ConnectionController.getConnection();

        AccountDao accountDao = new AccountDao(conn);
        UserDao userDao = new UserDao();
        AccountRoleDao accountRoleDao = new AccountRoleDao();
        AccountService accountService = new AccountService(accountDao);
        AccountController accountController = new AccountController(accountService);

        StartController startController = new StartController();
        Javalin app = startController.startApi();
        app.start(8080);
    }
}