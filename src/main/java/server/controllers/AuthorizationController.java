package server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.models.ApiResponse;
import server.models.SignUp;
import server.models.User;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController
public class AuthorizationController {
    private final UserController userController = new UserController();
    private final String frontendUrl = "http://KVVArtet-09-2017.herokuapp.com";

    AuthorizationController() {
    }

    @CrossOrigin(origins = frontendUrl)
    @RequestMapping(
            path = {"/signup"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity signUp(@RequestBody SignUp signUp) {
        String username = signUp.getLogin();
        String email = signUp.getEmail();
        String password = signUp.getPassword();
        String passwordConfirm = signUp.getPasswordConfirm();

        if (!Objects.equals(password, passwordConfirm)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.PASSWORD_NOT_MATCH.getResponse());
        }

        if (userController.isUsernameExists(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.USERNAME_EXIST.getResponse());
        } else if (userController.isEmailExists(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.EMAIL_EXIST.getResponse());
        } else {
            User user = new User(username, email, password);
            userController.setUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.SIGNUP_SUCCESS.getResponse());
        }
    }

    @CrossOrigin(origins = frontendUrl)
    @RequestMapping(
            path = {"/login"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity signIn(@RequestBody User user, HttpSession httpSession) {
        String username = user.getLogin();
        String email = user.getEmail();
        String password = user.getPassword();

        Integer userIdInCurrentSession = (Integer) httpSession.getAttribute("id");
      
        if (userIdInCurrentSession != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.USER_ALREADY_AUTHORIZED.getResponse());
        }

        String loginOrEmail;

        if (username == null) {
            loginOrEmail = email;
        } else {
            loginOrEmail = username;
        }

        if (!userController.isExist(loginOrEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.LOGIN_OR_EMAIL_NOT_EXIST.getResponse());
        } else if (!Objects.equals(userController.getUserPassword(loginOrEmail), password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.PASSWORD_INCORRECT.getResponse());
        } else {
            Integer id = userController.getUserIdByLoginOrEmail(loginOrEmail);
            httpSession.setAttribute("id", id);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.SIGNIN_SUCCESS.getResponse());
        }
    }

    @CrossOrigin(origins = frontendUrl)
    @RequestMapping(
            path = {"/signout"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity signOut(HttpSession httpSession) {
        Integer userIdInCurrentSession = (Integer) httpSession.getAttribute("id");
        if (userIdInCurrentSession == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.USER_NOT_AUTHORIZED.getResponse());
        }

        httpSession.removeAttribute("id");
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.SIGNOUT_SUCCESS.getResponse());
    }

    @CrossOrigin(origins = frontendUrl)
    @RequestMapping(
            path = {"/session"},
            method = {RequestMethod.GET}
    )
    public ResponseEntity requestUserInCurrentSession(HttpSession httpSession) {
        Integer userIdInCurrentSession = (Integer) httpSession.getAttribute("id");
        String username = userController.getUserById(userIdInCurrentSession).getLogin();

        if (userIdInCurrentSession == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.USER_NOT_AUTHORIZED.getResponse());
        }
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.REQUEST_FROM_SESSION_SUCCESSFUL.getResponse()
                + " " + userIdInCurrentSession + " " + username);
    }

    @CrossOrigin(origins = frontendUrl)
    @RequestMapping(
            path = {"/settings"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity changeUserProfile(@RequestBody SignUp signUp, HttpSession httpSession) {
        Integer id = (Integer) httpSession.getAttribute("id");
        String lastUsername = userController.getUserById(id).getLogin();

        if (lastUsername == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.USER_NOT_AUTHORIZED.getResponse());
        }

        String username = signUp.getLogin();
        String password = signUp.getPassword();
        String passwordConfirm = signUp.getPasswordConfirm();

        if (userController.isUsernameExists(username) && !Objects.equals(lastUsername, username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.USERNAME_EXIST.getResponse());
        } else if (!Objects.equals(passwordConfirm, userController.getUserById(id).getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.PASSWORD_NOT_MATCH.getResponse());
        } else {
            userController.updateUser(id, username, password);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.CHANGE_PROFILE_SUCCESS.getResponse());
        }
    }
}
