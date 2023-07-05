package request;

import result.LoginResult;

/**
 * Stores data which will be passed to the LoginService (/login)
 */
public class LoginRequest {
    /** Store the username and password of the user who wants to login */
    private final String username;
    private final String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
