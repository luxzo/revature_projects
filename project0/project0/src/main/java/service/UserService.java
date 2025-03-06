package service;

import dao.UserDao;
import dto.UserDto;
import model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
    private final UserDao userDao;
    public static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    //Todo si el valor de UserDao es null, cacha la validación
    public UserDto getUserById(int userId) throws NullPointerException {
        if (userDao.getUserById(userId) == null)
            logger.error("User id: " + userId + " not found");
        return userDao.getUserById(userId);
    }

    public Users registerNewUser(String name, String lastName, String phone, int accountId) {
        Users newUser = new Users();
        newUser.setName(name);
        newUser.setLast_name(lastName);
        newUser.setPhone(phone);
        newUser.setAccount_id(accountId);
        return userDao.registerNewUser(newUser);
    }
}
