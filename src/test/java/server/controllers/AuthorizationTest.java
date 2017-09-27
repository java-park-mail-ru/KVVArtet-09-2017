package server.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import server.models.User;

import static org.junit.Assert.*;

public class AuthorizationTest {
    private MockHttpSession mockHttpSession;
    private AuthorizationController authorizationController;

    @Before
    public void init() {
        mockHttpSession = new MockHttpSession();
        authorizationController = new AuthorizationController();
    }

    @Test
    public void signUpTest(){
        final User user = new User("xyz", "xyz@mail.ru", "xyz");
        ResponseEntity responseEntity = authorizationController.signUp(user);
        assertEquals("sign up shall return 200 code", HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void signInTest(){

        final User user = new User("xyz", "xyz@mail.ru", "xyz");
        ResponseEntity responseEntity =  authorizationController.signUp(user);
        assertEquals("sign up shall return 200 code", HttpStatus.OK, responseEntity.getStatusCode());

        mockHttpSession.setAttribute("id", null);
        responseEntity = authorizationController.signIn(user, mockHttpSession);
        assertEquals("sign in shall return 200 code", HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    public void signOutTest(){
        final User user = new User("xyz", "xyz@mail.ru", "xyz");

        ResponseEntity responseEntity = authorizationController.signUp(user);
        assertEquals("sign up shall return 200 code", HttpStatus.OK, responseEntity.getStatusCode());

        responseEntity = authorizationController.signIn(user, mockHttpSession);
        assertEquals("sign in shall return 200 code", HttpStatus.OK, responseEntity.getStatusCode());

        responseEntity = authorizationController.signOut(mockHttpSession);
        assertEquals("sign out shall return 200 code", HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void settingsTest() {
        User user = new User("xyz", "xyz@mail.ru", "xyz");
        ResponseEntity responseEntity = authorizationController.signUp(user);
        assertEquals("sign up shall return 200 code", HttpStatus.OK, responseEntity.getStatusCode());

        responseEntity = authorizationController.signIn(user, mockHttpSession);
        assertEquals("sign in shall return 200 code", HttpStatus.OK, responseEntity.getStatusCode());

        User changedUserLogin = new User("xxx", "xyz@mail.ru", "xyz");
        responseEntity = authorizationController.changeUserProfile(changedUserLogin, mockHttpSession);
        assertEquals("change login shall return 200 code", HttpStatus.OK, responseEntity.getStatusCode());

        User changedUserEmail = new User("xxx", "yxz@mail.ru", "xyz");
        responseEntity = authorizationController.changeUserProfile(changedUserEmail, mockHttpSession);
        assertEquals("change email shall return 200 code", HttpStatus.OK, responseEntity.getStatusCode());

        User changedUserPassword = new User("xxx", "yxz@mail.ru", "zyx");
        responseEntity = authorizationController.changeUserProfile(changedUserPassword, mockHttpSession);
        assertEquals("change password shall return 200 code", HttpStatus.OK, responseEntity.getStatusCode());

        User changedUserEmailAndLogin = new User("yyy", "yyy@mail.ru", "xyz");
        responseEntity = authorizationController.changeUserProfile(changedUserEmailAndLogin, mockHttpSession);
        assertEquals("change email and login shall return 200 code", HttpStatus.OK, responseEntity.getStatusCode());

        User changedUserLoginAndPassword = new User("zzz", "yyy@mail.ru", "zxy");
        responseEntity = authorizationController.changeUserProfile(changedUserLoginAndPassword, mockHttpSession);
        assertEquals("change login and password shall return 200 code", HttpStatus.OK, responseEntity.getStatusCode());

        User changedUserEverything = new User("xxx", "yxz@mail.ru", "xyz");
        responseEntity = authorizationController.changeUserProfile(changedUserEverything, mockHttpSession);
        assertEquals("change everything shall return 200 code", HttpStatus.OK, responseEntity.getStatusCode());

    }

}
