package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.AccountDao;
import dao.LoanDao;
import dao.UserDao;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AccountService;
import service.LoanService;
import service.UserService;

import java.sql.Connection;
import java.sql.SQLException;

public class StartController {
    public static final Logger logger = LoggerFactory.getLogger(StartController.class);
    Connection conn = ConnectionController.getConnection();
    AccountDao accountDao = new AccountDao(conn);
    AccountService accountService = new AccountService(accountDao);
    AccountController accountController = new AccountController(accountService);
    UserDao userDao = new UserDao();
    UserService userService = new UserService(userDao);
    UserController userController = new UserController(userService);
    LoanDao loanDao = new LoanDao();
    LoanService loanService = new LoanService(loanDao);
    LoanController loanController = new LoanController(loanService);
    LoginController loginController = new LoginController();

    public StartController() throws SQLException {
    }

    /**
     * Entry point for the application
     * Endpoints:
     * /auth/login login user
     * /auth/register/account register a new account
     * /auth/register/user register a new user
     * After each endpoint, a Javalin exception may be arised
     *
     * @return app
     */
    public Javalin startApi() {
        Javalin app = Javalin.create();
        app.post("/auth/register/account", accountController::registerNewAccount);
        app.post("/auth/register/user", userController::registerNewUser);
        app.post("/auth/login", loginController::loginUser);
        app.post("/auth/logout", loginController::logoutUser);
        app.get("/users/{id}", userController::getUserById);
        app.put("/users/{id}", userController::updateUser);
        app.post("/loans", loanController::createNewLoan);
        app.get("/loans/{id}", loanController::getLoanById);


        app.beforeMatched("users*", this::checkLogin);
        app.beforeMatched("loans*", this::checkLogin);

        return app;
    }

    private void checkLogin(Context ctx) throws SQLException {
        LoginController loginController = new LoginController();
//        boolean isLogged = loginController.checkLoginReturn(ctx);
//        if (!isLogged) {
        if (!loginController.checkLoginReturn(ctx)) {
            ctx.status(401);
            throw new UnauthorizedResponse();
        }
    }
}
