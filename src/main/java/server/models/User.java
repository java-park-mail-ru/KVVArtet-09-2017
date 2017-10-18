package server.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSetter;

@SuppressWarnings("unused")
public class User {
    private String username;
    private String email;
    private String password;
    private Integer id;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.id = 0;
    }

    public User(Integer id, String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.id = id;
    }

    @JsonCreator
    public User() {

    }

    public Integer getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    @JsonSetter("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonSetter("username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonSetter("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonSetter("password")
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

        return (username != null ? username.equals(user.username) : user.username == null)
                && (email != null ? email.equals(user.email) : user.email == null)
                && (password != null ? password.equals(user.password) : user.password == null)
                && (id != null ? id.equals(user.id) : user.id == null);
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        final int magicValue = 31;
        result = magicValue * result + (email != null ? email.hashCode() : 0);
        result = magicValue * result + (password != null ? password.hashCode() : 0);
        result = magicValue * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
