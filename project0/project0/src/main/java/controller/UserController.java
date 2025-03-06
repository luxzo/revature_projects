package controller;

import io.javalin.http.Context;
import dto.UserDto;
import model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

public class UserController {
    public static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
//    ConnectionController connController = new ConnectionController();

    public UserController(UserService userService) {
        this.userService = userService;
    }




    //Todo this endpoints must be created

    //        app.post("/auth/register", this::createNewUserHandler);
//        app.post("/auth/login", this::login);
//        app.post("/auth/logout", this::logout);
//        app.get("/users/{id}", this::getUserById); In progress
//        app.put("/users/{id}", this::updateUser);


    /*
     * Get a user by id handler
     * Todo: user must watch its own user, managaer can watch any user by id
     */
    private void getUserById(Context ctx) {
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
     * Post a new user handler
     * Todo terminar metodo
     */
    public void registerNewUser(Context ctx) {
        Users user = ctx.bodyAsClass(Users.class);
        Users newUser = null;

        try {
            newUser = userService.registerNewUser(
                    user.getName(),
                    user.getLast_name(),
                    user.getPhone(),
                    user.getAccount_id()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (newUser != null) {
            ctx.status(201);
            ctx.json("{\"message\":\"User created successfully!\"}");
        }
    }
}
