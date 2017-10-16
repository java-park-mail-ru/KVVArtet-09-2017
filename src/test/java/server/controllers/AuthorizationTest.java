package server.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import server.dao.UserDao;
import server.models.User;
import static org.junit.Assert.assertEquals;
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

	// @Test
	// public void signInTest() {
	// ResponseEntity responseEntity = authorizationController.signUp(user);
	// assertEquals("sign up shall return 200 code", HttpStatus.OK,
	// responseEntity.getStatusCode());
	//
	// mockHttpSession.setAttribute("id", null);
	// responseEntity = authorizationController.signIn(user, mockHttpSession);
	// assertEquals("sign in shall return 200 code", HttpStatus.OK,
	// responseEntity.getStatusCode());
	// }
	//
	// @Test
	// public void signOutTest() {
	//
	// ResponseEntity responseEntity = authorizationController.signUp(user);
	// assertEquals("sign up shall return 200 code", HttpStatus.OK,
	// responseEntity.getStatusCode());
	//
	// responseEntity = authorizationController.signIn(user, mockHttpSession);
	// assertEquals("sign in shall return 200 code", HttpStatus.OK,
	// responseEntity.getStatusCode());
	//
	// responseEntity = authorizationController.signOut(mockHttpSession);
	// assertEquals("sign out shall return 200 code", HttpStatus.OK,
	// responseEntity.getStatusCode());
	// }
	//
	// @Test
	// public void changingUserUsernameTest() {
	//
	// ResponseEntity responseEntity = authorizationController.signUp(user);
	// assertEquals("sign up shall return 200 code", HttpStatus.OK,
	// responseEntity.getStatusCode());
	//
	// responseEntity = authorizationController.signIn(user, mockHttpSession);
	// assertEquals("sign in shall return 200 code", HttpStatus.OK,
	// responseEntity.getStatusCode());
	//
	// User changedUserLogin = new User("xxx", "xyz@mail.ru", "xyz");
	// responseEntity =
	// authorizationController.changeUserProfile(changedUserLogin,
	// mockHttpSession);
	// assertEquals("change login shall return 200 code, body is "
	// + responseEntity.getBody(), HttpStatus.OK,
	// responseEntity.getStatusCode());
	// }
	//
	// @Test
	// public void changingUserPasswordTest() {
	// ResponseEntity responseEntity = authorizationController.signUp(user);
	// assertEquals("sign up shall return 200 code", HttpStatus.OK,
	// responseEntity.getStatusCode());
	//
	// responseEntity = authorizationController.signIn(user, mockHttpSession);
	// assertEquals("sign in shall return 200 code", HttpStatus.OK,
	// responseEntity.getStatusCode());
	//
	// User changedUserPassword = new User("xyz", "xyz@mail.ru", "xxx");
	// responseEntity =
	// authorizationController.changeUserProfile(changedUserPassword,
	// mockHttpSession);
	// assertEquals("change password shall return 200 code, body is "
	// + responseEntity.getBody(), HttpStatus.OK,
	// responseEntity.getStatusCode());
	// }
	//
	// @Test
	// public void changingUserLoginAndPasswordTest() {
	// ResponseEntity responseEntity = authorizationController.signUp(user);
	// assertEquals("sign up shall return 200 code", HttpStatus.OK,
	// responseEntity.getStatusCode());
	//
	// responseEntity = authorizationController.signIn(user, mockHttpSession);
	// assertEquals("sign in shall return 200 code", HttpStatus.OK,
	// responseEntity.getStatusCode());
	//
	// User changedUserLoginAndPassword = new User("xyz", "xyz@mail.ru", "zxy");
	// responseEntity =
	// authorizationController.changeUserProfile(changedUserLoginAndPassword,
	// mockHttpSession);
	// assertEquals("change login and password shall return 200 code, body is "
	// + responseEntity.getBody(), HttpStatus.OK,
	// responseEntity.getStatusCode());
	// }
}
