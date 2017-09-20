package server.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpSession;
import server.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {
    private UserController userController = new UserController();

    public AuthorizationController() {
    }

    @RequestMapping(
            path = {"/signup"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity signUp(@RequestBody User user) {

        List<String> logger = new LinkedList<>();

        String username = user.getLogin();

        if (userController.isUsernameExists(username)) {
            logger.add("Unable to sign up. User with that name is already exist.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(logger);
        } else if (userController.isEmailExists(username)) {
            logger.add("Unable to sign up. User with that email is already exist.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(logger);
        } else {
            userController.setUser(user);
            return ResponseEntity.ok(user);
        }
    }

    @RequestMapping(
            path = {"/"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity signIn(@RequestBody User user, HttpSession httpSession) {

        List<String> logger = new LinkedList<>();

        String username = user.getLogin();
        String email = user.getEmail();
        String password = user.getPassword();

        String userInCurrentSession = (String) httpSession.getAttribute("username");

        if(userInCurrentSession == null){
            logger.add("You already authorized :)");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(logger);
        }

        if (!userController.isUsernameExists(username) && !userController.isEmailExists(email)) {
            logger.add("Your login or email doesn't exist");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(logger);
        } else if (!Objects.equals(userController.getUserPassword(username), password)) {
            logger.add("Your password incorrect");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(logger);
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @RequestMapping(
            path = {"/signout"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity signOut(@RequestBody HttpSession httpSession) {

        List<String> logger = new LinkedList<>();

        String userInCurrentSession = (String) httpSession.getAttribute("username");
        if(userInCurrentSession == null){
            logger.add("You are already log out, my friend");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(logger);
        }

        httpSession.removeAttribute(userInCurrentSession);

        logger.add("Now you out of session");
        return ResponseEntity.ok(logger);
    }

    @RequestMapping(
            path = {"/session"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity requestUserInCurrentSession(@RequestBody User user, HttpSession httpSession) {

        List<String> logger = new LinkedList<>();

        String userInCurrentSession = (String) httpSession.getAttribute("username");

        if(!userController.isUsernameExists(userInCurrentSession) || userInCurrentSession == null){
            logger.add("You need to authorized first, my friend");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(logger);
        }
        return ResponseEntity.ok(user);
    }

    @RequestMapping(
            path = {"/settings"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity changeUserProfile(@RequestBody User user, HttpSession httpSession) {

        List<String> logger = new LinkedList<>();

        String lastUsername = (String) httpSession.getAttribute("username");

        if(lastUsername == null){
            logger.add("You need to authorized first, my friend");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(logger);
        }

        if(Objects.equals(lastUsername, user.getLogin())){
            logger.add("Your login is the same");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(logger);
        }

        userController.updateUser(lastUsername, user);
        httpSession.setAttribute("username", user.getLogin());
        return ResponseEntity.ok(user);
    }


}
