package server.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import server.models.User;

public class UserController {
    private Map<String, User> allUsers = new HashMap<>();
    private Map<String, String> allUsersEmail = new HashMap<>();

    UserController() {}

    public String getUserEmail(String username) {
        return ((User)allUsers.get(username)).getEmail();
    }

    public String getUserUsername(String email) {
        return (String)allUsersEmail.get(email);
    }

    String getUserPassword(String username) {
        return ((User)allUsers.get(username)).getPassword();
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

    private void deleteUser(String username) {
        String email = ((User)allUsers.get(username)).getEmail();
        allUsersEmail.remove(email);
        allUsers.remove(username);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        allUsers.forEach((key, value) -> {
            users.add(value);
        });
        return users;
    }
}
