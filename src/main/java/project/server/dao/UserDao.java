package project.server.dao;

import org.springframework.stereotype.Service;
import project.server.models.User;

import java.util.List;
@SuppressWarnings("unused")
public interface UserDao {

    User getUserById(Integer id);

    User getUserByUsernameOrEmail(String usernameOrEmail);

    Integer getUserIdByUsername(String username);

    Integer getUserIdByEmail(String email);

    Integer getUserIdByUsernameOrEmail(String usernameOrEmail);

    User setUser(User newUser);

    User updateUserPassword(User user, String password);

    User updateUserLogin(User user, String username);

    boolean isUsernameExists(String username);

    boolean isEmailExists(String email);

    boolean isIdExists(Integer id);

    boolean isExist(String usernameOrEmail);

    void deleteUser(Integer id);

    List<User> getAllUsers();

}
