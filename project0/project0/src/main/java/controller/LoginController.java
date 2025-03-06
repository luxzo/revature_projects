package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.AccountDao;
import dao.UserDao;
import dto.LoginRequestDto;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AccountService;
import service.HashService;
import service.UserService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoginController {
    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    //This map stores session data
    private static final Map<String, Map<String, Object>> sessionStore = new HashMap<>();

    Connection conn = ConnectionController.getConnection();
    AccountDao accountDao = new AccountDao(conn);
    AccountService accountService = new AccountService(accountDao);
    AccountController accountController = new AccountController(accountService);
    UserDao userDao = new UserDao();
    UserService userService = new UserService(userDao);
    UserController userController = new UserController(userService);

    public LoginController() throws SQLException {
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
        app.post("auth/register/user", userController::registerNewUser);
        //app.before()
        //Session handling middleware
        app.before(ctx -> {
            String sessionId = ctx.cookie("sessionId");
            if (sessionId == null || !sessionStore.containsKey(sessionId)) {
                sessionId = UUID.randomUUID().toString(); //Create a new session Id
                ctx.cookie("sessionId", sessionId); //Set cookie
                sessionStore.put(sessionId, new HashMap<>()); //Create new session
            }
            ctx.attribute("session", sessionStore.get(sessionId)); //Attach session to context
        });

        //Login endpoint
        app.post("/auth/login", ctx -> {
            LoginRequestDto loginRequest = objectMapper.readValue(ctx.body(), LoginRequestDto.class); //Read payload
            String email = loginRequest.getEmail();
            String rowPassword = loginRequest.getPassword();
            String newHashedPassword = "";
            HashService hashService = new HashService();

            try (Connection conn = ConnectionController.getConnection()) {
                String sql = "SELECT password FROM accounts WHERE email = ?";
                PreparedStatement pstm = pstm = conn.prepareStatement(sql);
                pstm.setString(1, email);

                ResultSet rs = pstm.executeQuery();
                if (rs.next()) {
                    String hashedPassFromDb = rs.getString("password"); //This password comes from database
                    //encodes rowPassword with hashedPasswordFromDb salt
                    newHashedPassword = hashService.verifyPassword(rowPassword, hashedPassFromDb);

                    //Compare if both passwords match
                    if (hashedPassFromDb.equals(newHashedPassword)) {
                        //Valid credentials
                        Map<String, Object> session = ctx.attribute("session");
                        session.put("email", email); //Store email in session
                        //Todo delete this following 3 lines
                        String sessionId = ctx.cookie("sessionId");
                        logger.info("Session Id: " + sessionId);
                        session.forEach((key, value) -> logger.info("Session map: [" + key + "]: [" + value + "]"));

                        ctx.status(200);
                        ctx.json("{\"login\": \"Login successful\"}");
                    } else { //Wrong password
                        ctx.status(401);
                        ctx.json("{\"login\": \"Invalid credentials\"}");
                    }
                } else { //User not found
                    ctx.status(401);
                    ctx.json("{\"login\": \"Invalid credentials\"}");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        //Todo Close individual session in case there is more than one open, use email to do so
        //Logout endpoint
        app.get("/auth/logout", ctx -> {
            String sessionId = ctx.cookie("sessionId");
            if (sessionId != null) {
                sessionStore.remove(sessionId); //Delete session
                ctx.removeCookie("sessionId"); //Delete cookie
                ctx.status(200);
                ctx.json("{\"session\": \"Closed\"}");
                logger.info("Logout successful");
            }
            else {
                ctx.status(404);
                ctx.json("{\"session\": \"Not found\"}");
                logger.info("No session found, cannot close");
            }
        });

        return app;
    }
}
