package server.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class SignUp {
    private final String login;
    private final String email;
    private final String password;
    private final String passwordConfirm;

    @JsonCreator
    public SignUp(@JsonProperty("username") String login, @JsonProperty("email") String email,
                  @JsonProperty("password") String password, @JsonProperty("passwordConfirm") String passwordConfirm) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public String getLogin() {
        return this.login;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getPasswordConfirm() {
        return this.passwordConfirm;
    }

}
