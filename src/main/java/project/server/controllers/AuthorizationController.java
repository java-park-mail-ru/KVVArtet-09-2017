package project.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import project.server.models.ApiResponse;
import project.server.models.User;
import project.server.services.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@SuppressWarnings("unused")
@RestController
@CrossOrigin(origins = AuthorizationController.FRONTED_URL1)
public class AuthorizationController {
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final ObjectMapper objectMapper;
    @SuppressWarnings("WeakerAccess")
    static final String FRONTED_URL1 = "https://lands-dangeous.herokuapp.com";
    static final String FRONTED_URL2 = "https://dev-lands-dungeons.herokuapp.com";


    public AuthorizationController(@NotNull UserService userService, @NotNull PasswordEncoder encoder, ObjectMapper objectMapper) {
        super();
        this.userService = userService;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
    }


    @PostMapping("/signup")
    public @NotNull ResponseEntity<String> signUp(@RequestBody User user) {
        final String username = user.getUsername();
        final String email = user.getEmail();
        final String password = user.getPassword();

        if (username == null || email == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.FIELD_EMPTY.getResponse());
        }

        final Integer six = 6;

        if (username.length() < six || username.contains("@") || !email.contains("@")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.SIGNUP_VALIDATION_FAILED.getResponse());
        }

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
    public @NotNull ResponseEntity<String> signIn(@RequestBody User user, @NotNull HttpSession httpSession) {

        final Integer userIdInCurrentSession = (Integer) httpSession.getAttribute("id");

        if (userIdInCurrentSession != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.USER_ALREADY_AUTHORIZED.getResponse());
        }

        final String usernameOrEmail;

        if (user.getUsername() == null) {
            usernameOrEmail = user.getEmail();
        } else {
            usernameOrEmail = user.getUsername();
        }

        if (!userService.isExist(usernameOrEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.LOGIN_OR_EMAIL_NOT_EXIST.getResponse());
        }

        final User currentUser = userService.getUserByUsernameOrEmail(usernameOrEmail);

        final Integer currentUserId = Objects.requireNonNull(currentUser).getId();

        if (!encoder.matches(user.getPassword(), currentUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.PASSWORD_INCORRECT.getResponse());
        } else {
            httpSession.setAttribute("id", currentUserId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.SIGNIN_SUCCESS.getResponse());
        }
    }

    @DeleteMapping("/signout")
    public @NotNull ResponseEntity<String> signOut(@NotNull HttpSession httpSession) {
        final Integer userIdInCurrentSession = (Integer) httpSession.getAttribute("id");
        if (userIdInCurrentSession == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.USER_NOT_AUTHORIZED.getResponse());
        }

        httpSession.invalidate();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.SIGNOUT_SUCCESS.getResponse());
    }

    @GetMapping("/session")
    public @NotNull ResponseEntity<String> requestUserInCurrentSession(@NotNull HttpSession httpSession) {
        final Integer userIdInCurrentSession = (Integer) httpSession.getAttribute("id");

        if (userIdInCurrentSession == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.USER_NOT_AUTHORIZED.getResponse());
        }

        final User user = Objects.requireNonNull(userService
                .getUserById(userIdInCurrentSession));
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(objectMapper.writeValueAsString(user));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.INTERNAL_ERROR.getResponse());
        }
    }

    @PutMapping("/settings")
    public @NotNull ResponseEntity<String> changeUserProfile(@RequestBody User user,
                                                             @NotNull HttpSession httpSession) {
        final Integer id = (Integer) httpSession.getAttribute("id");

        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.USER_NOT_AUTHORIZED.getResponse());
        }

        final User currentUser = userService.getUserById(id);
        final String lastUsername = Objects.requireNonNull(currentUser).getUsername();
        final String lastPassword = currentUser.getPassword();

        final String username = user.getUsername();
        final String password = user.getPassword();

        if (userService.isUsernameExists(username) && !Objects.equals(lastUsername, username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.USERNAME_EXIST.getResponse());
        } else if (!Objects.equals(lastUsername, username)) {
            userService.updateUserLogin(currentUser, username);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.CHANGE_PROFILE_SUCCESS.getResponse());
        } else if (!encoder.matches(password, lastPassword)) {
            userService.updateUserPassword(currentUser, password);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.CHANGE_PROFILE_SUCCESS.getResponse());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.CHANGE_PROFILE_SUCCESS.getResponse());
    }
}
