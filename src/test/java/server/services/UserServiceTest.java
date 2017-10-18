package server.services;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import server.dao.UserDao;
import server.models.User;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class UserServiceTest {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserDao dao;

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void testSetUser() {
        User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        User user = dao.setUser(newUser);
        assertTrue(user.getId() > 0);
        assertEquals("testname", user.getUsername());
        assertEquals("testemail@mail.ru", user.getEmail());
        assertTrue(encoder.matches("testpassword",user.getPassword()));
        assertNotNull(dao.getAllUsers());
    }

    @Test
    public void testGetUserById() {
        User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        User settedUser = dao.setUser(newUser);
        User user = dao.getUserById(settedUser.getId());
        assertTrue(user.getId() > 0);
        assertEquals("testname", user.getUsername());
        assertEquals("testemail@mail.ru", user.getEmail());
        assertTrue(encoder.matches("testpassword",user.getPassword()));
    }

    @Test
    public void testGetUserByUsernameOrEmail() {
        User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        User settedUser = dao.setUser(newUser);
        User user = dao.getUserByUsernameOrEmail(settedUser.getUsername());
        assertTrue(user.getId() > 0);
        assertEquals("testname", user.getUsername());
        assertEquals("testemail@mail.ru", user.getEmail());
        assertTrue(encoder.matches("testpassword",user.getPassword()));
    }

    @Test
    public void testGetUserIdByUsername() {
        User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        User settedUser = dao.setUser(newUser);
        Integer id = dao.getUserIdByUsername(settedUser.getUsername());
        assertTrue(id > 0);
        assertEquals(settedUser.getId(), id);
    }

    @Test
    public void testGetUserIdByEmail() {
        User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        User settedUser = dao.setUser(newUser);
        Integer id = dao.getUserIdByEmail(settedUser.getEmail());
        assertTrue(id > 0);
        assertEquals(settedUser.getId(), id);
    }

    @Test
    public void testGetUserIdByUsernameOrEmail() {
        User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        User settedUser = dao.setUser(newUser);
        Integer idFromLogin = dao.getUserIdByUsernameOrEmail(settedUser.getUsername());
        Integer idFromEmail = dao.getUserIdByUsernameOrEmail(settedUser.getEmail());
        assertTrue(idFromLogin > 0);
        assertEquals(settedUser.getId(), idFromLogin);
        assertTrue(idFromEmail > 0);
        assertEquals(settedUser.getId(), idFromEmail);
    }

    @Test
    public void testUpdateUserLogin() {
        User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        User settedUser = dao.setUser(newUser);
        User user = dao.updateUserLogin(settedUser, "testname2");

        assertTrue(user.getId() > 0);
        assertEquals("testname2", user.getUsername());
        assertEquals("testemail@mail.ru", user.getEmail());
        assertTrue(encoder.matches("testpassword",user.getPassword()));
        assertNotNull(dao.getAllUsers());
    }

    @Test
    public void testUpdateUserPassword() {
        User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        User settedUser = dao.setUser(newUser);
        User user = dao.updateUserPassword(settedUser, "testpassword2");

        assertTrue(user.getId() > 0);
        assertEquals("testname", user.getUsername());
        assertEquals("testemail@mail.ru", user.getEmail());
        assertTrue(encoder.matches("testpassword2",user.getPassword()));
        assertNotNull(dao.getAllUsers());
    }

    @Test
    public void testIsEmailExists() {
        User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        User settedUser = dao.setUser(newUser);
        assertTrue(dao.isEmailExists(settedUser.getEmail()));
    }

    @Test
    public void testIsUsernameExists() {
        User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        User settedUser = dao.setUser(newUser);
        assertTrue(dao.isUsernameExists(settedUser.getUsername()));
    }

    @Test
    public void testIsIdExist() {
        User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        User settedUser = dao.setUser(newUser);
        assertTrue(dao.isIdExists(settedUser.getId()));
    }

    @Test
    public void testIsExists() {
        User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        User settedUser = dao.setUser(newUser);
        assertTrue(dao.isExist(settedUser.getUsername()));
        assertTrue(dao.isExist(settedUser.getEmail()));
    }

    @Test
    public void testDeleteUser() {
        User newUser = new User("testname", "testemail@mail.ru", "testpassword");
        User settedUser = dao.setUser(newUser);
        dao.deleteUser(settedUser.getId());
        assertFalse(dao.isIdExists(settedUser.getId()));
    }

    @Test
    public void testGetAllUsers() {
        User newUser1 = new User("testname1", "testemail1@mail.ru", "testpassword1");
        User settedUser1 = dao.setUser(newUser1);
        User newUser2 = new User("testname2", "testemail2@mail.ru", "testpassword2");
        User settedUser2 = dao.setUser(newUser2);
        List<User> users= dao.getAllUsers();
        assertEquals(users.get(0), settedUser1);
        assertEquals(users.get(1), settedUser2);
    }
}
