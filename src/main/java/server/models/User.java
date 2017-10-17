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
    public User(@JsonProperty("username") String login, @JsonProperty("email") String email,
                @JsonProperty("password") String password) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.id = 0;
    }

    @JsonCreator
    public User(@JsonProperty("id") Integer id, @JsonProperty("username") String login,
                @JsonProperty("email") String email, @JsonProperty("password") String password) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.id = id;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        User user = (User) obj;

        return (login != null ? login.equals(user.login) : user.login == null) &&
                (email != null ? email.equals(user.email) : user.email == null) &&
                (password != null ? password.equals(user.password) : user.password == null) &&
                (id != null ? id.equals(user.id) : user.id == null);
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        final int magicValue = 31;
        result = magicValue * result + (email != null ? email.hashCode() : 0);
        result = magicValue * result + (password != null ? password.hashCode() : 0);
        result = magicValue * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
