package server.dao;

import server.models.User;
import java.util.*;

public interface UserDao {

        String getUserEmail(String username);
        String getUserUsername(String email);

        String getUserPassword(String loginOrEmail);

        User getUserById(Integer id);

        Integer getUserIdByLoginOrEmail(String loginOrEmail);

        void setUser(User newUser);

        void updateUser(Integer id, String username, String password);

        boolean isUsernameExists(String username);

        boolean isEmailExists(String email);

        boolean isIdExists(Integer id);

        boolean isExist(String usernameOrEmail);

        void deleteUser(Integer id);

        List<User> getAllUsers();

}
