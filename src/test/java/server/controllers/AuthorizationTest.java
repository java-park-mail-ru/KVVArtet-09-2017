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
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import server.dao.UserDao;
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
                        .content(mapper.writeValueAsString(new User("xyz", "xyz@mail.ru", "xyz"))))
                .andExpect(status().isOk());
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
                .content(mapper.writeValueAsString(new User("xyz", "xyz@mail.ru", "xyz"))))
                .andExpect(status().isOk());
    }

    @Test
    public void sessionTest() throws Exception{
        MockHttpSession session = new MockHttpSession();
        signIn(session);
        mockMvc
                .perform(post("/session")
                .session(session))
                .andExpect(status().isOk());
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
    public void changingUserUsernameTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        signIn(session);
        mockMvc
                .perform(post("/settings")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("zxy", "xyz@mail.ru", "xyz"))))
                .andExpect(status().isOk());
    }

    @Test
    public void changingUserPasswordTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        signIn(session);
        mockMvc
                .perform(post("/settings")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("xyz", "zxy@mail.ru", "xyz"))))
                .andExpect(status().isOk());
    }

    @Test
    public void changingUserLoginAndPasswordTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        signIn(session);
        mockMvc
                .perform(post("/settings")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("zzz", "zzz@mail.ru", "zzz"))))
                .andExpect(status().isOk());
    }
}
