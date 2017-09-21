package server.controllers;

import server.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class UserController {
    private Map<String, User> allUsers = new HashMap<>();
    private Map<String, String> allUsersEmail = new HashMap<>();

    UserController() { }

    public String getUserEmail(String username) {
        return allUsers.get(username).getEmail();
    }

    public String getUserUsername(String email) {
        return allUsersEmail.get(email);
    }

    String getUserPassword(String loginOrEmail) {
        if (isUsernameExists(loginOrEmail)) {
            return allUsers.get(loginOrEmail).getPassword();
        }
        String username = allUsersEmail.get(loginOrEmail);
        return allUsers.get(username).getPassword();
    }

    void setUser(User newUser) {
        allUsers.put(newUser.getLogin(), newUser);
        allUsersEmail.put(newUser.getEmail(), newUser.getLogin());
    }

    void updateUser(String lastUsername, User user) {
        deleteUser(lastUsername);
        setUser(user);
    }

    boolean isUsernameExists(String username) {
        return allUsers.containsKey(username);
    }

    boolean isEmailExists(String email) {
        return allUsersEmail.containsKey(email);
    }

    boolean isExist(String usernameOrEmail) {
        return isUsernameExists(usernameOrEmail) || isEmailExists(usernameOrEmail);
    }

    private void deleteUser(String username) {
        String email = allUsers.get(username).getEmail();
        allUsersEmail.remove(email);
        allUsers.remove(username);
    }

    public ArrayList<User> getAllUsers() {
        return (ArrayList<User>) allUsers.values();
    }
}
