package controller;

import io.javalin.http.Context;
import model.Account;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AccountService;

public class AccountController {
    public static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final AccountService accountService;
    
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Todo check why it is not catching exceptions when field is null or empty.
     * Todo Also is not throwing exception when account already exists
     * Todo how to connect User class to register account and user, all at once????
     * Register a new user
     * Json body:
     * {
     * "email": "JohnDoe@mail.com",
     * "password": "password",
     * "role_id": 2
     * }
     * **/
    public void registerNewAccount(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);

        Account newAccount = null;

        try {
            newAccount = accountService.registerNewAccount(
                    account.getEmail(),
                    account.getPassword(),
                    account.getRole_id()
            );
        } catch (Exception e) {
            logger.error("User already exists");
        }

        if (newAccount != null) {
            ctx.status(201);

            ctx.json("{\"message\":\"Account created, please register User data to finish\",\n" +
                    "[\"endpoint\": \"/auth/register/user\", " +
                    "\n\"email\": \"" + newAccount.getEmail() + "\", " +
                    "\n\"account_id\": \"" + newAccount.getAccountId() + "\"]\n}");
        }
        else
            ctx.status(409).json("{\"error\": \"Missing username, password or role id\"}");
    }
}
