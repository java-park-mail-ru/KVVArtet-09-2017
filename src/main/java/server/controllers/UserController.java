package server.controllers;

import server.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
class UserController {
    private final Map<String, User> allUsersByLogin = new HashMap<>();
    private final Map<String, User> allUsersByEmail = new HashMap<>();
    private final Map<Integer, User> allUsersById = new HashMap<>();

    UserController() { }

    public String getUserEmail(String username) {
        return allUsersByLogin.get(username).getEmail();
    }

    public String getUserUsername(String email) {
        return allUsersByEmail.get(email).getLogin();
    }

    String getUserPassword(String loginOrEmail) {
        if (isUsernameExists(loginOrEmail)) {
            return allUsersByLogin.get(loginOrEmail).getPassword();
        }
        return allUsersByEmail.get(loginOrEmail).getPassword();
    }

    User getUserById(Integer id) {
        return allUsersById.get(id);
    }

    Integer getUserIdByLoginOrEmail(String loginOrEmail) {
        if (allUsersByLogin.containsKey(loginOrEmail)) {
            return allUsersByLogin.get(loginOrEmail).getId();
        }
        return allUsersByEmail.get(loginOrEmail).getId();
    }

    void setUserByParam(String username, String email, String password) {
        User newUser = new User(username, email, password);
        Integer id = newUser.getId();
        allUsersByLogin.put(username, newUser);
        allUsersByLogin.put(email, newUser);
        allUsersById.put(id, newUser);
    }

    void setUser(User newUser) {
        allUsersByLogin.put(newUser.getLogin(), newUser);
        allUsersByEmail.put(newUser.getEmail(), newUser);
        allUsersById.put(newUser.getId(), newUser);
    }

    void updateUser(Integer id, String username, String password) {
        User updatedUser = allUsersById.get(id);
        String lastUsername = updatedUser.getLogin();
        String lastPassword = updatedUser.getPassword();

        if (!Objects.equals(lastUsername, username)) {
            updatedUser.setLogin(username);
        }
        if (!Objects.equals(lastPassword, password)) {
            updatedUser.setPassword(password);
        }

        deleteUser(id);
        setUser(updatedUser);
    }

    boolean isUsernameExists(String username) {
        return allUsersByLogin.containsKey(username);
    }

    boolean isEmailExists(String email) {
        return allUsersByEmail.containsKey(email);
    }

    boolean isIdExists(Integer id) {
        return allUsersById.containsKey(id);
    }

    boolean isExist(String usernameOrEmail) {
        return isUsernameExists(usernameOrEmail) || isEmailExists(usernameOrEmail);
    }

    private void deleteUser(Integer id) {
        User deletedUser = allUsersById.get(id);
        String username = deletedUser.getLogin();
        String email = deletedUser.getEmail();

        allUsersByEmail.remove(email);
        allUsersByLogin.remove(username);
        allUsersById.remove(id);
    }

    public ArrayList<User> getAllUsers() {
        return (ArrayList<User>) allUsersByLogin.values();
    }
}
