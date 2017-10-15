package server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.models.ApiResponse;
import server.models.User;
import server.services.UserService;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController
public class AuthorizationController {
    private final UserService userController = new UserService();
    private final String frontendUrl = "http://KVVArtet-09-2017.herokuapp.com";

    AuthorizationController() {
    }

    @CrossOrigin(origins = frontendUrl)
    @RequestMapping(
            path = {"/signup"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity signUp(@RequestBody User user) {
        String username = user.getLogin();
        String email = user.getEmail();

        if (userController.isUsernameExists(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.USERNAME_EXIST.getResponse());
        } else if (userController.isEmailExists(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.EMAIL_EXIST.getResponse());
        } else {
            userController.setUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.SIGNUP_SUCCESS.getResponse());
        }
    }

    @CrossOrigin(origins = frontendUrl)
    @RequestMapping(
            path = {"/signin"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity signIn(@RequestBody User user, HttpSession httpSession) {

        Integer userIdInCurrentSession = (Integer) httpSession.getAttribute("id");
      
        if (userIdInCurrentSession != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.USER_ALREADY_AUTHORIZED.getResponse());
        }

        String usernameOrEmail;

        if (user.getLogin() == null) {
            usernameOrEmail = user.getEmail();
        } else {
            usernameOrEmail = user.getLogin();
        }

        if (!userController.isExist(usernameOrEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.LOGIN_OR_EMAIL_NOT_EXIST.getResponse());
        }

        Integer currentUserId = userController.getUserIdByUsernameOrEmail(usernameOrEmail);

        if (!Objects.equals(userController.getUserById(currentUserId).getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.PASSWORD_INCORRECT.getResponse());
        } else {
            httpSession.setAttribute("id", currentUserId);
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
            method = {RequestMethod.POST}
    )
    public ResponseEntity requestUserInCurrentSession(HttpSession httpSession) {
        Integer userIdInCurrentSession = (Integer) httpSession.getAttribute("id");
        String username = userController.getUserById(userIdInCurrentSession).getLogin();

        if (userIdInCurrentSession == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.USER_NOT_AUTHORIZED.getResponse());
        }
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.REQUEST_FROM_SESSION_SUCCESSFUL.getResponse()
                + " " + userIdInCurrentSession + "  Your login is " + username);
    }

    @CrossOrigin(origins = frontendUrl)
    @RequestMapping(
            path = {"/settings"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity changeUserProfile(@RequestBody User user, HttpSession httpSession) {
        Integer id = (Integer) httpSession.getAttribute("id");
        String lastUsername = userController.getUserById(id).getLogin();
        String lastPassword = userController.getUserById(id).getPassword();

        if (lastUsername == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.USER_NOT_AUTHORIZED.getResponse());
        }

        String username = user.getLogin();
        String password = user.getPassword();

        if (userController.isUsernameExists(username) && !Objects.equals(lastUsername, username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.USERNAME_EXIST.getResponse());
        } else if (!Objects.equals(lastUsername, username) && !(Objects.equals(password, password))) {
            userController.updateUserLogin(id, username);
            userController.updateUserPassword(id, password);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.CHANGE_PROFILE_SUCCESS.getResponse());
        } else if (!Objects.equals(lastUsername, username)) {
            userController.updateUserLogin(id, username);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.CHANGE_PROFILE_SUCCESS.getResponse());
        } else if (!Objects.equals(lastPassword, password)) {
            userController.updateUserPassword(id, password);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.CHANGE_PROFILE_SUCCESS.getResponse());
        }
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.CHANGE_PROFILE_SUCCESS.getResponse());
    }
}
