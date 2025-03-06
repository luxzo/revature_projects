package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.AccountDao;
import dao.UserDao;
import dto.LoginRequestDto;
import io.javalin.Javalin;
import io.javalin.http.Context;
import jakarta.servlet.http.HttpSession;
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

    private Map<String, Object> sessionMap = new HashMap<>();

    Connection conn = ConnectionController.getConnection();
    AccountDao accountDao = new AccountDao(conn);
    AccountService accountService = new AccountService(accountDao);
    AccountController accountController = new AccountController(accountService);
    UserDao userDao = new UserDao();
    UserService userService = new UserService(userDao);
    UserController userController = new UserController(userService);

    //Map<email, session>
//    Map<List<String>, List<HttpSession>> httpSessionMap = new HashMap();
    Map<String, HttpSession> httpSessionMap = new HashMap<>();

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
        //Starts a new session
        else {
            HttpSession session = ctx.req().getSession(true);
            session.setAttribute("user", userDb);
            httpSessionMap.put(userDb.getEmail(), session);
            ctx.status(200);
            ctx.json("{\"message\": \"Login successful\"}");
            logger.info("User login: " + userDb.getEmail());
        }
    }

    /**
     * Retrieve user from db using its email as user
     * @param email
     * @return LoginRequestDto object
     */
    public LoginRequestDto getUserFromDb(String email) {
        String sql = "SELECT email, password FROM public.accounts WHERE email = ?";
        try (Connection conn = ConnectionController.getConnection()) {
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, email);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                LoginRequestDto loginDb = new LoginRequestDto();
                loginDb.setEmail(rs.getString("email"));
                loginDb.setPassword(rs.getString("password"));
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
        LoginRequestDto loginRequestDto = ctx.bodyAsClass(LoginRequestDto.class);
        String emailRequest = loginRequestDto.getEmail();
        if (httpSessionMap.containsKey(emailRequest)) {
            //Close session
            HttpSession session = ctx.req().getSession(false);
            session.invalidate();
            httpSessionMap.remove(emailRequest);
            ctx.json("{\"info\": \"User logout\"}");
            logger.info("Session closed for " + emailRequest);
        }
        else {
            ctx.status(400).json("{\"error\":\"Not logged in\"}");
            logger.error("Session not found: " + emailRequest);
        }
    }

    /**
     * Check if there is a session opened
     * @param ctx
     * @return
     */
    public boolean checkLogin(Context ctx) {
        return !httpSessionMap.isEmpty();
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
        app.post("/auth/login", this::loginUser);
        app.post("/auth/logout", this::logoutUser);
/*
        //Session handling middleware
        app.before(ctx -> {
            String sessionId = ctx.cookie("sessionId");
            if (sessionId == null || !sessionStore.containsKey(sessionId)) {
                sessionId = UUID.randomUUID().toString(); //Create a new session Id
                ctx.cookie("sessionId", sessionId); //Set cookie
                sessionStore.put(sessionId, new HashMap<>()); //Create new session
            }
            ctx.attribute("session", sessionStore.get(sessionId)); //Attach session to context
            sessionStore.forEach((key, value) -> System.out.println("key " + key + " " + "value " + value));
        });
*//*
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
                        sessionMap = session;
//                        sessionMap.forEach((key, value) -> System.out.println("SMapKey " + key + " SMapValue " + value));
                        //Todo delete these following 3 lines
//                        String sessionId = ctx.cookie("sessionId");
//                        logger.info("Session Id: " + sessionId);
//                        session.forEach((key, value) -> logger.info("Session map: [" + key + "]: [" + value + "]"));

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
*/
/*
        //Todo Close individual session in case there is more than one open, use email to do so
        //Logout endpoint
        app.post("/auth/logout", ctx -> {
//            LogoutRequest logoutRequest = ctx.bodyAsClass(LogoutRequest.class);
//            String receivedEmail = logoutRequest.getEmail();
//            sessionStore.forEach((key, value) -> System.out.println("stored session: " + key + " value: " + value));
//            String email;
//            if (sessionMap.get("email").equals(receivedEmail))
//                System.out.println("AQuí está el email");
////                email = sessionMap.ge
//            System.out.println(sessionStore.keySet());
//            System.out.println(sessionStore.values());
//
//
//
//            if (sessionStore.containsValue(sessionMap.get("email"))) {
//                System.out.println("Sí existe la sessión");
//            }
/*
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
        });*/

        return app;
    }
}
