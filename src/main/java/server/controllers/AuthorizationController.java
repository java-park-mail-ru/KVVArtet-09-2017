package server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.models.ApiResponse;
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
    public ResponseEntity signUp(@RequestBody User user, HttpSession httpSession) {
        String username = user.getLogin();
        String email = user.getEmail();

        if (userController.isUsernameExists(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.USERNAME_EXIST);
        } else if (userController.isEmailExists(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.EMAIL_EXIST);
        } else {
            userController.setUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.SIGNUP_SUCCESS);
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

        String userInCurrentSession = (String) httpSession.getAttribute("username");
      
        if (userInCurrentSession == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.USER_ALREADY_AUTHORIZED);
        }

        String loginOrEmail;

        if (username == null) {
            loginOrEmail = email;
        } else {
            loginOrEmail = username;
        }

        if (!userController.isExist(loginOrEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.LOGIN_OR_EMAIL_NOT_EXIST);
        } else if (!Objects.equals(userController.getUserPassword(loginOrEmail), password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.PASSWORD_INCORRECT);
        } else {
            httpSession.setAttribute("username", username);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.SIGNIN_SUCCESS);
        }
    }

    @CrossOrigin(origins = frontendUrl)
    @RequestMapping(
            path = {"/signout"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity signOut(HttpSession httpSession) {
        String userInCurrentSession = (String) httpSession.getAttribute("username");
        if (userInCurrentSession == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.USER_NOT_AUTHORIZED);
        }

        httpSession.removeAttribute("username");
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.SIGNOUT_SUCCESS);
    }

    @CrossOrigin(origins = frontendUrl)
    @RequestMapping(
            path = {"/session"},
            method = {RequestMethod.GET}
    )
    public ResponseEntity requestUserInCurrentSession(HttpSession httpSession) {
        String userInCurrentSession = (String) httpSession.getAttribute("username");

        if (userInCurrentSession == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.USER_NOT_AUTHORIZED);
        }
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.REQUEST_FROM_SESSION_SUCCESSFUL + httpSession.getId());
    }

    @CrossOrigin(origins = frontendUrl)
    @RequestMapping(
            path = {"/settings"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity changeUserProfile(@RequestBody User user, HttpSession httpSession) {
        String lastUsername = (String) httpSession.getAttribute("username");

        if (lastUsername == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.USER_NOT_AUTHORIZED);
        }

        String username = user.getLogin();
        String email = user.getEmail();

        if (userController.isUsernameExists(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.USERNAME_EXIST);
        } else if (userController.isEmailExists(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.EMAIL_EXIST);
        } else {
            userController.updateUser(lastUsername, user);
            httpSession.setAttribute("username", user.getLogin());
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.CHANGE_PROFILE_SUCCESS);
        }
    }
}
