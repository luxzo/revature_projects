package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.AccountDao;
import dao.UserDao;
import dto.LoginRequestDto;
import io.javalin.Javalin;
import io.javalin.http.Context;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AccountService;
import service.HashService;
import service.UserService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class LoginController {
    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    //This map stores session data
    private static final Map<String, Map<String, Object>> sessionStore = new HashMap<>();
    //HttpSession session;

    Connection conn = ConnectionController.getConnection();
    AccountDao accountDao = new AccountDao(conn);
    AccountService accountService = new AccountService(accountDao);
    AccountController accountController = new AccountController(accountService);
    UserDao userDao = new UserDao();
    UserService userService = new UserService(userDao);
    UserController userController = new UserController(userService);
    //Map<String, HttpSession> httpSessionMap = new HashMap<>();

    public LoginController() throws SQLException {
    }

    /**
     * Login a user.
     * Email and password must match, otherwise it will show an error
     * If previous step is correct, creates a new session
     * @param ctx
     */
    public void loginUser(Context ctx) {
        LoginRequestDto loginRequest = ctx.bodyAsClass(LoginRequestDto.class);

        //If something is missing in payload
        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null)
            ctx.status(400).json("{\"error\":\"Missing username or password\"}");

        //Variable that stores the returned value from getUserFromDb(emailFromDto).
        LoginRequestDto userDb = getUserFromDb(loginRequest.getEmail());

        //If no data retrieved
        if (userDb == null)
            ctx.status(401).json("{\"error\":\"Invalid credentials\"}");

        //Compare passwords
        String hashedPasswordFromDb = userDb.getPassword();
        HashService hashService = new HashService();
        String newHashedPassword = "";
        try {
            newHashedPassword = hashService.verifyPassword(loginRequest.getPassword(), hashedPasswordFromDb);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error(e.getMessage());
        }
        //If password does not match
        if (!hashedPasswordFromDb.equals(newHashedPassword)) {
            ctx.status(401).json("{\"error\":\"Invalid credentials\"}");
        }
        //If password match, start a new session
        else {
            HttpSession session = ctx.req().getSession(true);
            session.setAttribute("user", userDb);
//            httpSessionMap.put(userDb.getEmail(), session);
            ctx.status(200);
            ctx.json("{\"message\": \"Login successful\"}");
            logger.info("User login: " + userDb.getEmail());
            logger.info("Role id: " + userDb.getRole_id());
        }
    }


    /**
     * Retrieve user from db using its email as user
     * @param email
     * @return LoginRequestDto object
     */
    public LoginRequestDto getUserFromDb(String email) {
//        String sql = "SELECT email, password, role_id FROM public.accounts WHERE email = ?";
        String sql = "SELECT email, password, role_id, user_id FROM public.accounts JOIN users ON users.account_id = accounts.account_id  WHERE email = ?";
        try (Connection conn = ConnectionController.getConnection()) {
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, email);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                LoginRequestDto loginDb = new LoginRequestDto();
                loginDb.setEmail(rs.getString("email"));
                loginDb.setPassword(rs.getString("password"));
                loginDb.setRole_id(rs.getInt("role_id"));
                loginDb.setUser_id(rs.getInt("user_id"));
                return loginDb;
            }
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return null;
    }


    /**
     * Close session for user, if no session found, will show an error
     * @param ctx
     */
    public void logoutUser(Context ctx) {
//        LoginRequestDto loginRequestDto = ctx.bodyAsClass(LoginRequestDto.class);
//        String emailRequest = loginRequestDto.getEmail();
//        if (httpSessionMap.containsKey(emailRequest)) {
            //Close session
//            HttpSession session = ctx.req().getSession(false);
//            HttpSession session = ctx.req().getSession(false);
//            session.invalidate();
            //httpSessionMap.remove(emailRequest);
//            ctx.json("{\"info\": \"User logout\"}");
//            logger.info("Session closed for " + emailRequest);
//        }
//        else {
//            ctx.status(400).json("{\"error\":\"Not logged in\"}");
//            logger.error("Session not found: " + emailRequest);
//        }
        HttpSession session = ctx.req().getSession(false);
        if (session != null)
            session.invalidate();
        ctx.status(200).json("{\"message\":\"Logged out\"}");
    }


    /**
     * Check if there is an open session
     * @return
     */
    public boolean checkLoginReturn(Context ctx) {
        HttpSession session = ctx.req().getSession(false);
        return session != null && session.getAttribute("user") != null;
    }


    /**
     * Entry point for the application
     * Endpoints:
     * post /auth/login login user
     * post /auth/register/account register a new account
     * post /auth/register/user register a new user
     * post /auth/login user login
     * post /auth/logout user logout
     * get /users/{id} find user by id
     * put /users/{id} Update user data
     * After each endpoint, a Javalin exception may be arised
     *
     * @return app
     */
    public Javalin startApi() {
        Javalin app = Javalin.create();
        app.post("/auth/register/account", accountController::registerNewAccount);
        app.post("/auth/register/user", userController::registerNewUser);
        app.post("/auth/login", this::loginUser);
        app.post("/auth/logout", this::logoutUser);
        app.get("/users/{id}", userController::getUserById);
        app.put("/users/{id}", userController::updateUser);

        return app;
    }
}
