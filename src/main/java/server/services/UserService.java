package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import server.dao.UserDao;
import server.models.User;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.*;

public class UserService extends JdbcDaoSupport implements UserDao {
    private final Map<String, User> allUsersByLogin = new HashMap<>();
    private final Map<String, User> allUsersByEmail = new HashMap<>();
    private final Map<Integer, User> allUsersById = new HashMap<>();

    public UserService() { }

    private DataSource dataSource;

    @PostConstruct
    private void initialize(){
        setDataSource(dataSource);
    }

    @Override
    public String getUserEmail(String username) {
        return allUsersByLogin.get(username).getEmail();
    }
    @Override
    public String getUserUsername(String email) {
        return allUsersByEmail.get(email).getLogin();
    }
    @Override
    public String getUserPassword(String loginOrEmail) {
        if (isUsernameExists(loginOrEmail)) {
            return allUsersByLogin.get(loginOrEmail).getPassword();
        }
        return allUsersByEmail.get(loginOrEmail).getPassword();
    }
    @Override
    public User getUserById(Integer id) {
        return allUsersById.get(id);
    }
    @Override
    public Integer getUserIdByLoginOrEmail(String loginOrEmail) {
        if (allUsersByLogin.containsKey(loginOrEmail)) {
            return allUsersByLogin.get(loginOrEmail).getId();
        }
        return allUsersByEmail.get(loginOrEmail).getId();
    }
    @Override
    public void setUser(User newUser) {
        allUsersByLogin.put(newUser.getLogin(), newUser);
        allUsersByEmail.put(newUser.getEmail(), newUser);
        allUsersById.put(newUser.getId(), newUser);
    }
    @Override
    public void updateUser(Integer id, String username, String password) {
        User updatedUser = allUsersById.get(id);
        String lastUsername = updatedUser.getLogin();
        String lastPassword = updatedUser.getPassword();

        if (!Objects.equals(lastUsername, username)) {
            updatedUser.setLogin(username);
        }
        if (!Objects.equals(lastPassword, password)) {
            updatedUser.setPassword(password);
        }

        allUsersByLogin.remove(lastUsername);
        allUsersByLogin.put(username, updatedUser);
    }
    @Override
    public boolean isUsernameExists(String username) {
        return allUsersByLogin.containsKey(username);
    }
    @Override
    public boolean isEmailExists(String email) {
        return allUsersByEmail.containsKey(email);
    }
    @Override
    public boolean isIdExists(Integer id) {
        return allUsersById.containsKey(id);
    }
    @Override
    public boolean isExist(String usernameOrEmail) {
        return isUsernameExists(usernameOrEmail) || isEmailExists(usernameOrEmail);
    }
    @Override
    public void deleteUser(Integer id) {
        User deletedUser = allUsersById.get(id);
        String username = deletedUser.getLogin();
        String email = deletedUser.getEmail();

        allUsersByEmail.remove(email);
        allUsersByLogin.remove(username);
        allUsersById.remove(id);
    }
    @Override
    public List<User> getAllUsers() {
        return (ArrayList<User>) allUsersByLogin.values();
    }
}
