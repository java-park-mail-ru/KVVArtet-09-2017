package project.server.services;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.server.dao.UserDao;
import project.server.models.User;

import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

@SuppressWarnings("unused")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class UserServiceTest {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserDao dao;

    @SuppressWarnings("PublicField")
    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void testSetUser() {
        final User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        final User user = dao.setUser(newUser);
        assertTrue(user.getId() > 0);
        assertEquals("testname", user.getUsername());
        assertEquals("testemail@mail.ru", user.getEmail());
        assertTrue(encoder.matches("testpassword",user.getPassword()));
        assertNotNull(dao.getAllUsers());
    }

    @Test
    public void testGetUserById() {
        final User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        final User settedUser = dao.setUser(newUser);
        final User user = dao.getUserById(settedUser.getId());
        assertTrue(Objects.requireNonNull(user).getId() > 0);
        assertEquals("testname", user.getUsername());
        assertEquals("testemail@mail.ru", user.getEmail());
        assertTrue(encoder.matches("testpassword",user.getPassword()));
    }

    @Test
    public void testGetUserByUsernameOrEmail() {
        final User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        final User settedUser = dao.setUser(newUser);
        final User user = dao.getUserByUsernameOrEmail(settedUser.getUsername());
        //noinspection ConstantConditions,ConstantConditions
        assertTrue(user.getId() > 0);
        assertEquals("testname", user.getUsername());
        assertEquals("testemail@mail.ru", user.getEmail());
        assertTrue(encoder.matches("testpassword",user.getPassword()));
    }

    @Test
    public void testGetUserIdByUsername() {
        final User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        final User settedUser = dao.setUser(newUser);
        final Integer id = dao.getUserIdByUsername(settedUser.getUsername());
        assertTrue(id > 0);
        assertEquals(settedUser.getId(), id);
    }

    @Test
    public void testGetUserIdByEmail() {
        final User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        final User settedUser = dao.setUser(newUser);
        final Integer id = dao.getUserIdByEmail(settedUser.getEmail());
        assertTrue(id > 0);
        assertEquals(settedUser.getId(), id);
    }

    @Test
    public void testGetUserIdByUsernameOrEmail() {
        final User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        final User settedUser = dao.setUser(newUser);
        final Integer idFromLogin = dao.getUserIdByUsernameOrEmail(settedUser.getUsername());
        final Integer idFromEmail = dao.getUserIdByUsernameOrEmail(settedUser.getEmail());
        assertTrue(idFromLogin > 0);
        assertEquals(settedUser.getId(), idFromLogin);
        assertTrue(idFromEmail > 0);
        assertEquals(settedUser.getId(), idFromEmail);
    }

    @Test
    public void testUpdateUserLogin() {
        final User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        final User settedUser = dao.setUser(newUser);
        final User user = dao.updateUserLogin(settedUser, "testname2");

        assertTrue(user.getId() > 0);
        assertEquals("testname2", user.getUsername());
        assertEquals("testemail@mail.ru", user.getEmail());
        assertTrue(encoder.matches("testpassword",user.getPassword()));
        assertNotNull(dao.getAllUsers());
    }

    @Test
    public void testUpdateUserPassword() {
        final User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        final User settedUser = dao.setUser(newUser);
        final User user = dao.updateUserPassword(settedUser, "testpassword2");

        assertTrue(user.getId() > 0);
        assertEquals("testname", user.getUsername());
        assertEquals("testemail@mail.ru", user.getEmail());
        assertTrue(encoder.matches("testpassword2",user.getPassword()));
        assertNotNull(dao.getAllUsers());
    }

    @Test
    public void testIsEmailExists() {
        final User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        final User settedUser = dao.setUser(newUser);
        assertTrue(dao.isEmailExists(settedUser.getEmail()));
    }

    @Test
    public void testIsUsernameExists() {
        final User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        final User settedUser = dao.setUser(newUser);
        assertTrue(dao.isUsernameExists(settedUser.getUsername()));
    }

    @Test
    public void testIsIdExist() {
        final User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        final User settedUser = dao.setUser(newUser);
        assertTrue(dao.isIdExists(settedUser.getId()));
    }

    @Test
    public void testIsExists() {
        final User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        final User settedUser = dao.setUser(newUser);
        assertTrue(dao.isExist(settedUser.getUsername()));
        assertTrue(dao.isExist(settedUser.getEmail()));
    }

    @Test
    public void testDeleteUser() {
        final User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        final User settedUser = dao.setUser(newUser);
        dao.deleteUser(settedUser.getId());
        assertFalse(dao.isIdExists(settedUser.getId()));
    }

    @Test
    public void testGetAllUsers() {
        final User newUser1 = new User("testname1", "testemail1@mail.ru", "testpassword1");
        final User settedUser1 = dao.setUser(newUser1);
        final User newUser2 = new User("testname2", "testemail2@mail.ru", "testpassword2");
        final User settedUser2 = dao.setUser(newUser2);
        final List<User> users = dao.getAllUsers();
        assertEquals(users.get(users.size() - 2), settedUser1);
        assertEquals(users.get(users.size() - 1), settedUser2);
    }
}
