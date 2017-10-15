package server.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class User {
    private String login;
    private String email;
    private String password;
    private Integer id;

    @JsonCreator
    public User(@JsonProperty("username") String login, @JsonProperty("email") String email, @JsonProperty("password") String password) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.id = 0;
    }

    public User() {

    }

    public Integer getId() {
        return this.id;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
