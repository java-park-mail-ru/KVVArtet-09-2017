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
@CrossOrigin(origins = AuthorizationController.FRONTED_URL)
public class AuthorizationController {
    private final UserService userService;
    @SuppressWarnings("WeakerAccess")
    static final String FRONTED_URL = "http://KVVArtet-09-2017.herokuapp.com";

  

    public AuthorizationController(UserService userService) {
        super();
        this.userService = userService;
    }


    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody User user) {
        String username = user.getLogin();
        String email = user.getEmail();

        if (userService.isUsernameExists(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.USERNAME_EXIST.getResponse());
        } else if (userService.isEmailExists(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.EMAIL_EXIST.getResponse());
        } else {
            userService.setUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.SIGNUP_SUCCESS.getResponse());
        }
    }

    @PostMapping("/signin")
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

        if (!userService.isExist(usernameOrEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.LOGIN_OR_EMAIL_NOT_EXIST.getResponse());
        }

        Integer currentUserId = userService.getUserIdByUsernameOrEmail(usernameOrEmail);

        if (!Objects.equals(userService.getUserById(currentUserId).getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.PASSWORD_INCORRECT.getResponse());
        } else {
            httpSession.setAttribute("id", currentUserId);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.SIGNIN_SUCCESS.getResponse());
        }
    }

    @PostMapping("/signout")
    public ResponseEntity signOut(HttpSession httpSession) {
        Integer userIdInCurrentSession = (Integer) httpSession.getAttribute("id");
        if (userIdInCurrentSession == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.USER_NOT_AUTHORIZED.getResponse());
        }

        httpSession.invalidate();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.SIGNOUT_SUCCESS.getResponse());
    }

    @PostMapping("/session")
    public ResponseEntity requestUserInCurrentSession(HttpSession httpSession) {
        Integer userIdInCurrentSession = (Integer) httpSession.getAttribute("id");

        if (userIdInCurrentSession == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.USER_NOT_AUTHORIZED.getResponse());
        }

        String username = userService.getUserById(userIdInCurrentSession).getLogin();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.REQUEST_FROM_SESSION_SUCCESSFUL.getResponse()
                + " " + userIdInCurrentSession + "  Your login is " + username);
    }

    @PostMapping("/settings")
    public ResponseEntity changeUserProfile(@RequestBody User user, HttpSession httpSession) {
        Integer id = (Integer) httpSession.getAttribute("id");

        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.USER_NOT_AUTHORIZED.getResponse());
        }

        User currentUser = userService.getUserById(id);
        String lastUsername = currentUser.getLogin();
        String lastPassword = currentUser.getPassword();

        String username = user.getLogin();
        String password = user.getPassword();

        if (userService.isUsernameExists(username) && !Objects.equals(lastUsername, username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.USERNAME_EXIST.getResponse());
        } else if (!Objects.equals(lastUsername, username) && !(Objects.equals(password, password))) {
            userService.updateUserLogin(currentUser, username);
            userService.updateUserPassword(currentUser, password);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.CHANGE_PROFILE_SUCCESS.getResponse());
        } else if (!Objects.equals(lastUsername, username)) {
            userService.updateUserLogin(currentUser, username);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.CHANGE_PROFILE_SUCCESS.getResponse());
        } else if (!Objects.equals(lastPassword, password)) {
            userService.updateUserPassword(currentUser, password);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.CHANGE_PROFILE_SUCCESS.getResponse());
        }
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.CHANGE_PROFILE_SUCCESS.getResponse());
    }
}
