package result;

import model.Authtoken;

/**
 * Stores the result of RegisterService (/user/register)
 */
public class RegisterResult extends Result {
    /** Store a LoginResult, as a successful register is a login */
    private final LoginResult result;

    /**
     * Take in result data while constructing the RegisterResult
     */
    public RegisterResult(String message, boolean success, Authtoken authtoken, String username, String personID) {
        super(message, success);
        result = new LoginResult(message, success, authtoken, username, personID);
    }
}
