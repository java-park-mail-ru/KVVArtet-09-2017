package server.dao;

import server.models.User;
import java.util.*;

@SuppressWarnings("unused")
public interface UserDao {

        User getUserById(Integer id);

        Integer getUserIdByUsername(String username);

        Integer getUserIdByEmail(String email);

        Integer getUserIdByUsernameOrEmail(String usernameOrEmail);

        User setUser(User newUser);

        User updateUserPassword(Integer id, String password);

        User updateUserLogin(Integer id, String username);

        boolean isUsernameExists(String username);

        boolean isEmailExists(String email);

        boolean isIdExists(Integer id);

        boolean isExist(String usernameOrEmail);

        void deleteUser(Integer id);

        List<User> getAllUsers();

}
