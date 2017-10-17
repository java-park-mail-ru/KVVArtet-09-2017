package server.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
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
        signUpTest();
        mockMvc
                .perform(post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("xyz", "xyz@mail.ru", "xyz"))))
                .andExpect(status().isOk());
    }

    @Test
    public void sessionTest() throws Exception{
        mockMvc
                .perform(post("/session"))
                .andExpect(status().isOk());
    }

//    @Test
//    public void signOutTest() throws Exception {
//
//        mockMvc
//                .perform(post("/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(new User("xxx", "xxx@mail.ru", "xxx"))))
//                .andExpect(status().isOk());
//        mockMvc
//                .perform(post("/signin")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(new User("xxx", "xxx@mail.ru", "xxx"))))
//                .andExpect(status().isOk());
//        mockMvc
//                .perform(post("/signout"))
//                .andExpect(status().isOk());
//    }

    @Test
    public void changingUserUsernameTest() throws Exception {
        signUpTest();
        signInTest();
        mockMvc
                .perform(post("/settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("zxy", "xyz@mail.ru", "xyz"))))
                .andExpect(status().isOk());
    }

    @Test
    public void changingUserPasswordTest() throws Exception {
        signUpTest();
        signInTest();
        mockMvc
                .perform(post("/settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("xyz", "zxy@mail.ru", "xyz"))))
                .andExpect(status().isOk());
    }

    @Test
    public void changingUserLoginAndPasswordTest() throws Exception {
        signUpTest();
        signInTest();
        mockMvc
                .perform(post("/settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User("zzz", "zzz@mail.ru", "zzz"))))
                .andExpect(status().isOk());
    }
}
