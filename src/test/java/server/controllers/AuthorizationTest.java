package server.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import server.dao.UserDao;
import server.models.ApiResponse;
import server.models.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@Transactional
public class AuthorizationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserDao dao;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void signUpTest() throws Exception {
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("testusername", "testemail@mail.ru", "testpassword"))))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string(ApiResponse.SIGNUP_SUCCESS.getResponse()));
    }

    @Test
    public void signUpWithoutUsernameTest() throws Exception {
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User(null, "testemail@mail.ru", "testpassword"))))
                .andExpect(status().is(400)).andExpect(MockMvcResultMatchers.content().string(ApiResponse.FIELD_EMPTY.getResponse()));
    }

    @Test
    public void signUpWithoutEmailTest() throws Exception {
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("testusername", null, "testpassword"))))
                .andExpect(status().is(400)).andExpect(MockMvcResultMatchers.content().string(ApiResponse.FIELD_EMPTY.getResponse()));
    }

    @Test
    public void signUpWithoutPasswordTest() throws Exception {
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("testusername", "testemail@mail.ru", null))))
                .andExpect(status().is(400)).andExpect(MockMvcResultMatchers.content().string(ApiResponse.FIELD_EMPTY.getResponse()));
    }

    @Test
    public void signUpWithUsernameThatExistTest() throws Exception {
        dao.setUser(new User("reallytestusername", "reallytestemail@mail.ru", "reallytestpassword"));
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("reallytestusername", "testemail@mail.ru", "testpassword"))))
                .andExpect(status().is(400)).andExpect(MockMvcResultMatchers.content().string(ApiResponse.USERNAME_EXIST.getResponse()));
    }

    @Test
    public void signUpWithEmailThatExistTest() throws Exception {
        dao.setUser(new User("reallytestusername", "reallytestemail@mail.ru", "reallytestpassword"));
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("testusername", "reallytestemail@mail.ru", "testpassword"))))
                .andExpect(status().is(400)).andExpect(MockMvcResultMatchers.content().string(ApiResponse.EMAIL_EXIST.getResponse()));
    }

    @Test
    public void signUpWithUsernameThatNotValidTest() throws Exception {
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("test@username", "testemail@mail.ru", "testpassword"))))
                .andExpect(status().is(400)).andExpect(MockMvcResultMatchers.content().string(ApiResponse.SIGNUP_VALIDATION_FAILED.getResponse()));
    }

    @Test
    public void signUpWithEmailhatNotValidTest() throws Exception {
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("testusername", "testemailmail.ru", "testpassword"))))
                .andExpect(status().is(400)).andExpect(MockMvcResultMatchers.content().string(ApiResponse.SIGNUP_VALIDATION_FAILED.getResponse()));
    }

    @Test
    public void signInTest() throws Exception {
        signIn(null);
    }

    private void signIn(MockHttpSession session) throws Exception {
        signUpTest();
        MockHttpServletRequestBuilder post = post("/signin");
        if (session != null) {
            post.session(session);
        }
        mockMvc.perform(post
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new User("testusername", "testemail@mail.ru", "testpassword"))))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string(ApiResponse.SIGNIN_SUCCESS.getResponse()));
    }

    @Test
    public void signInWithOnlyEmailTest() throws Exception {
        signUpTest();
        MockHttpServletRequestBuilder post = post("/signin");
        mockMvc.perform(post
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new User(null, "testemail@mail.ru", "testpassword"))))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string(ApiResponse.SIGNIN_SUCCESS.getResponse()));
    }

    @Test
    public void signInWithNotExistUserTest() throws Exception {
        signUpTest();
        MockHttpServletRequestBuilder post = post("/signin");
        mockMvc.perform(post
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new User("randomTestusername", "randomTestemail@mail.ru", "newTestpassword"))))
                .andExpect(status().is(403)).andExpect(MockMvcResultMatchers.content().string(ApiResponse.LOGIN_OR_EMAIL_NOT_EXIST.getResponse()));
    }

    @Test
    public void signInWithOnlyUsernameTest() throws Exception {
        signUpTest();
        MockHttpServletRequestBuilder post = post("/signin");
        mockMvc.perform(post
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new User("testusername", null, "testpassword"))))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string(ApiResponse.SIGNIN_SUCCESS.getResponse()));
    }

    @Test
    public void signInWithWrongPasswordTest() throws Exception {
        signUpTest();
        MockHttpServletRequestBuilder post = post("/signin");
        mockMvc.perform(post
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new User("testusername", "testemail@mail.ru", "mistakepassword"))))
                .andExpect(status().is(403)).andExpect(MockMvcResultMatchers.content().string(ApiResponse.PASSWORD_INCORRECT.getResponse()));
    }

    @Test
    public void sessionTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        signIn(session);
        mockMvc
                .perform(post("/session")
                        .session(session))
                .andExpect(status().isOk());
    }

    @Test
    public void sessionWithoutSessionIdTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        signIn(null);
        mockMvc
                .perform(post("/session")
                        .session(session))
                .andExpect(status().is(401)).andExpect(MockMvcResultMatchers.content().string(ApiResponse.USER_NOT_AUTHORIZED.getResponse()));
    }

    @Test
    public void signOutTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        signIn(session);
        mockMvc
                .perform(post("/signout")
                        .session(session))
                .andExpect(status().isOk());
    }

    @Test
    public void signOutWithoutSessionIdThatMeansYouAreNotAuthorizedTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        signIn(null);
        mockMvc
                .perform(post("/signout")
                        .session(session))
                .andExpect(status().is(401)).andExpect(MockMvcResultMatchers.content().string(ApiResponse.USER_NOT_AUTHORIZED.getResponse()));
    }

    @Test
    public void changingUserUsernameTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        signIn(session);
        mockMvc
                .perform(post("/settings")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("testusername1", "testemail@mail.ru", "testpassword"))))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string(ApiResponse.CHANGE_PROFILE_SUCCESS.getResponse()));
    }

    @Test
    public void changingUserUsernameWithExistUsernameTest() throws Exception {
        dao.setUser(new User("reallytestusername", "reallytestemail@mail.ru", "reallytestpassword"));
        MockHttpSession session = new MockHttpSession();
        signIn(session);
        mockMvc
                .perform(post("/settings")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("reallytestusername", "testemail@mail.ru", "testpassword"))))
                .andExpect(status().is(403)).andExpect(MockMvcResultMatchers.content().string(ApiResponse.USERNAME_EXIST.getResponse()));
    }

    @Test
    public void changingUserPasswordTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        signIn(session);
        mockMvc
                .perform(post("/settings")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("testusername", "testemail@mail.ru", "sometestpassword"))))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string(ApiResponse.CHANGE_PROFILE_SUCCESS.getResponse()));
    }

    @Test
    public void changingUserNotChangeAnyTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        signIn(session);
        mockMvc
                .perform(post("/settings")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("testusername", "testemail@mail.ru", "testpassword"))))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string(ApiResponse.CHANGE_PROFILE_SUCCESS.getResponse()));
    }
}
