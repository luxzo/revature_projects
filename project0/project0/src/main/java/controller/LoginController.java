package controller;

import dao.AccountDao;
import dao.UserDao;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AccountService;
import service.UserService;

import java.sql.Connection;
import java.sql.SQLException;

public class LoginController {
    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    Connection conn = ConnectionController.getConnection();
    AccountDao accountDao = new AccountDao(conn);
    AccountService accountService = new AccountService(accountDao);
    AccountController accountController = new AccountController(accountService);
    UserDao userDao = new UserDao();
    UserService userService = new UserService(userDao);
    UserController userController = new UserController(userService);

    public LoginController() throws SQLException {
    }

    public Javalin startApi() {
        Javalin app = Javalin.create();
        app.post("/auth/register/account", accountController::registerNewAccount);
//        app.post("/auth/register", this::registerNewAccount);
        app.post("auth/register/user", userController::registerNewUser);

        app.after("/auth/register/user", ctx -> {
            System.out.println(ctx.statusCode());
        });
        return app;
    }

    private void registerNewAccount(Context ctx) {

    }
}

    //private final LoginService loginService;

    /* Postman params:
{
    "email": "esme@mail.com",
    "password": "password",
    "role": "USER",
    "name": "Esmeralda",
    "last_name": "V",
    "phone": "12345"
}
     */

   // public LoginController(LoginService loginService) {
       // this.loginService = loginService;


    /*public Javalin startApi() {
        Javalin app = Javalin.create();
        app.post("/auth/register", this::registerNewAccount);
        return app;
    }*/

//    AccountController accountController = new AccountController();
/*
    private void registerNewAccount(Context ctx) {
        RegisterUserDto rud = ctx.bodyAsClass(RegisterUserDto.class);
        RegisterUserDto newRegister = null;
        try {
            newRegister = loginService.registerNewAccount(rud);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (newRegister != null) {
            ctx.status(201);

            ctx.json("{\"message\":\"Change this message\",\n" +
                    "[\"endpoint\": \"/auth/register\", " +
                    "\n\"email\": \"email here\", " +
                    "\n\"account_id\": \"accountId here \"]\n}");
        }
    }*/
