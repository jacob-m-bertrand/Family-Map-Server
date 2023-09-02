package request;

/**
 * Stores user-provided data which will be used to log them in.
 */
public class LoginRequest {
    /** The provided username */
    private final String username;

    /** The provided password */
    private final String password;

    /**
     * Constructs the LoginRequest from user-provided data
     * @param username The user-provided username
     * @param password The user-provided password
     */
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
