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

        List<String> responseBody = new LinkedList<>();

        String username = user.getLogin();

        if (userController.isUsernameExists(username)) {
            responseBody.add("Unable to sign up. User with that name is already exist.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        } else if (userController.isEmailExists(username)) {
            responseBody.add("Unable to sign up. User with that email is already exist.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
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

        List<String> responseBody = new LinkedList<>();

        String username = user.getLogin();
        String email = user.getEmail();
        String password = user.getPassword();

        String userInCurrentSession = (String) httpSession.getAttribute("username");

        if(userInCurrentSession == null){
            responseBody.add("You already authorized :)");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }

        if (!userController.isUsernameExists(username) && !userController.isEmailExists(email)) {
            responseBody.add("Your login or email doesn't exist");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        } else if (!Objects.equals(userController.getUserPassword(username), password)) {
            responseBody.add("Your password incorrect");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @RequestMapping(
            path = {"/signout"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity signOut(@RequestBody HttpSession httpSession) {

        List<String> responseBody = new LinkedList<>();

        String userInCurrentSession = (String) httpSession.getAttribute("username");
        if(userInCurrentSession == null){
            responseBody.add("You are already log out, my friend");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }

        httpSession.removeAttribute(userInCurrentSession);

        responseBody.add("Now you out of session");
        return ResponseEntity.ok(responseBody);
    }

    @RequestMapping(
            path = {"/session"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity requestUserInCurrentSession(@RequestBody User user, HttpSession httpSession) {

        List<String> responseBody = new LinkedList<>();

        String userInCurrentSession = (String) httpSession.getAttribute("username");

        if(!userController.isUsernameExists(userInCurrentSession) || userInCurrentSession == null){
            responseBody.add("You need to authorized first, my friend");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        return ResponseEntity.ok(user);
    }

    @RequestMapping(
            path = {"/settings"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity changeUserProfile(@RequestBody User user, HttpSession httpSession) {

        List<String> responseBody = new LinkedList<>();

        String lastUsername = (String) httpSession.getAttribute("username");

        if(lastUsername == null){
            responseBody.add("You need to authorized first, my friend");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }

        if(Objects.equals(lastUsername, user.getLogin())){
            responseBody.add("Your login is the same");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }

        userController.updateUser(lastUsername, user);
        httpSession.setAttribute("username", user.getLogin());
        return ResponseEntity.ok(user);
    }


}
