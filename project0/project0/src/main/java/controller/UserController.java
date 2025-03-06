package controller;

import io.javalin.http.Context;
import dto.UserDto;
import model.User;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.sql.SQLException;

public class UserController {
    public static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
//    ConnectionController connController = new ConnectionController();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*
     * Get a user by id handler
     * Todo: user must watch its own user, managaer can watch any user by id
     */
    public void getUserById(Context ctx) throws SQLException {
//        LoginController loginController = new LoginController();
            int userId = Integer.parseInt(ctx.pathParam("id"));
            UserDto user;

            //In case userId is null, will catch NullPointerException
            try {
                user = userService.getUserById(userId);
                ctx.json(user);
                logger.info("User id: " + userId + " found");
            }
            catch (NullPointerException e) {
                logger.error(e.getMessage());
                ctx.status(404); //This is the status response, it is shown in Postman
                ctx.json("{\n\"user_id\": \"" + userId + "\"\n}");
            }
    }


    /*
     * Post a new user
     * First must be an AccountController object to create a user
     *  Todo add exception handling
     */
    public void registerNewUser(Context ctx) {
        User user = ctx.bodyAsClass(User.class);
        User newUser = null;

        try {
            newUser = userService.registerNewUser(
                    user.getName(),
                    user.getLast_name(),
                    user.getPhone(),
                    user.getAccount_id()
            );
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        if (newUser != null) {
            ctx.status(201);
            ctx.json("{\"message\":\"User created successfully!\"}");
        }
    }

    public void updateUser(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("id"));
        User user = ctx.bodyAsClass(User.class);
        User updatedUser = null;
        try {
            updatedUser = userService.updateUser(
                    user.getName(),
                    user.getLast_name(),
                    user.getPhone(),
                    userId
            );
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        ctx.json(updatedUser);
    }
}
