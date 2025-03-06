import controller.AccountController;
import controller.ConnectionController;
import controller.LoginController;
import dao.AccountDao;
import dao.AccountRoleDao;
import dao.UserDao;
import io.javalin.Javalin;
import service.AccountService;

import java.sql.Connection;
import java.sql.SQLException;

public class LoansMain {
    public static void main(String[] args) throws SQLException {
        /*ConnectionController connController = new ConnectionController();
        List<Object> list = new ArrayList<>();
        try {
            Connection myConn = connController.getConnection();
            String sql = """
                    select name, last_name, phone, email, role from users
                    join accounts on users.account_id = accounts.account_id
                    join account_roles on accounts.role_id = account_roles.role_id
                    where user_id = 1;
                    """;
            PreparedStatement pstm = myConn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Users users = new Users(
                        rs.getString("name"),
                        rs.getString("last_name"),
                        rs.getString("phone")
                );
                Accounts accounts = new Accounts(
                        rs.getString("email")
                );
                AccountRoles roles = new AccountRoles(
                        rs.getString("role")
                );
                list.add(users);
                list.add(accounts);
                list.add(roles);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        for (Object element : list)
            System.out.println(element);*/
//        UserDao ud = new UserDao();
//        UserService us = new UserService(ud);
//        UserController uc = new UserController(us);
//        Javalin app = uc.startApi();

//        AccountDao ad = new AccountDao();
//        AccountService as = new AccountService(ad);
//        AccountController ac = new AccountController(as);
//        Javalin app = ac.startApi();

        //LoginDao ld = new LoginDao();


        Connection conn = ConnectionController.getConnection();

        AccountDao accountDao = new AccountDao(conn);
        UserDao userDao = new UserDao();
        AccountRoleDao accountRoleDao = new AccountRoleDao();
        AccountService accountService = new AccountService(accountDao);
        AccountController accountController = new AccountController(accountService);

//        LoginService ls = new LoginService(accountDao, userDao, accountRoleDao);
//        LoginController lc = new LoginController(ls);
        //        Javalin app = accountController.startApi();

        LoginController loginController = new LoginController();
        Javalin app = loginController.startApi();
        app.start(8080);

    }
}
