package server.models;

@SuppressWarnings("unused")
public enum ApiResponse {
    USERNAME_EXIST(0, "User with that name is already exist"),
    EMAIL_EXIST(1, "User with that email is already exist"),
    USER_ALREADY_AUTHORIZED(2, "User is already authorized"),
    LOGIN_OR_EMAIL_NOT_EXIST(3, "User's login or email doesn't exist"),
    PASSWORD_INCORRECT(4, "User's password incorrect"),
    SIGNUP_SUCCESS(5, "SignUp is successful"),
    SIGNIN_SUCCESS(6, "SignIp is successful"),
    USER_NOT_AUTHORIZED(7, "You need to authorized first, my friend"),
    SIGNOUT_SUCCESS(8, "SignOut is successful"),
    LOGIN_IS_THE_SAME(9, "User login is the same"),
    CHANGE_PROFILE_SUCCESS(10, "Profile successfully changed"),
    REQUEST_FROM_SESSION_SUCCESSFUL(11, "User successfully requested, your ID is:"),
    PASSWORD_NOT_MATCH(12, "Password and password confirmation not match");

    private final String response;

    ApiResponse(Integer status, String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
