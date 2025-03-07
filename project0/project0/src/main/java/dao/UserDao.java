package dao;

import controller.ConnectionController;
import dto.UserDto;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class UserDao {
    public static final Logger logger = LoggerFactory.getLogger(UserDao.class);
    private int accountId;

    public UserDao() {}

    public UserDao(int accountId) { this.accountId = accountId; }

    /*
    * Register a new user Dao
    *  This class persists into the database the new user created
    */
    public User registerNewUser(User newUser) {
        String newUserSql = "INSERT INTO public.users(name, last_name, phone, account_id) VALUES(?, ?, ?, ?)";
        try (Connection conn = ConnectionController.getConnection()) {
            PreparedStatement pstmNewUser = conn.prepareStatement(newUserSql, Statement.RETURN_GENERATED_KEYS);
            pstmNewUser.setString(1, newUser.getName());
            pstmNewUser.setString(2, newUser.getLast_name());
            pstmNewUser.setString(3, newUser.getPhone());
            pstmNewUser.setInt(4, newUser.getAccount_id());
            pstmNewUser.execute();
            logger.info("INSERT INTO users(name, last_name, phone, account_id) VALUES(?, ?, ?, ?)");

            int newUserId;
            try (ResultSet generatedUserId = pstmNewUser.getGeneratedKeys()){
                if (generatedUserId.next()) {
                    newUserId = generatedUserId.getInt(1);
                    newUser.setUserId(newUserId);
                }
            }
        } catch (Exception e) {
           logger.error(e.getMessage());
        }
        return newUser;
    }

    public User updateUser(User updatedUser) {
        String updateUserSql = "UPDATE users SET name = ?, last_name = ?, phone = ? WHERE user_id = ?";
        try (Connection conn = ConnectionController.getConnection()) {
            PreparedStatement pstm = conn.prepareStatement(updateUserSql);
            pstm.setString(1, updatedUser.getName());
            pstm.setString(2, updatedUser.getLast_name());
            pstm.setString(3, updatedUser.getPhone());
            pstm.setInt(4, updatedUser.getUserId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return updatedUser;
    }

    /**
     * Get a user by Id, will show name, lastName and phone.
     * If user does not exist, will prompt an error in console and show a Json object with user Id, as well
     * status code will be 404
     */
    public UserDto getUserById(int userId) {
        try (Connection conn = ConnectionController.getConnection()) {
            String sql = "SELECT name, last_name, phone FROM users WHERE user_id = ?";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setInt(1, userId);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                return new UserDto(
                        rs.getString("name"),
                        rs.getString("last_name"),
                        rs.getString("phone")
                );
            }
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
