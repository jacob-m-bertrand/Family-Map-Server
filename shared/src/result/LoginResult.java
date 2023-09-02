package result;

/**
 * Stores the result of the LoginService (/user/login)
 */
public class LoginResult extends Result {
    /** The authorization token for this session, used later to validate user while accessing data. */
    private final String authtoken;

    /** The user's username, which will also be used to validate the user while accessing data. */
    private final String username;

    /** The user's personID, used to connect them to their family tree. */
    private final String personID;

    /**
     * Constructs the LoginResult from data provided by the service.
     * @param success   The success status of the login process.
     * @param authtoken The authtoken used for this session.
     * @param username  The username of the user.
     * @param personID  The user's personID, used to situate them on the family map.
     */
    public LoginResult(boolean success, String authtoken, String username, String personID) {
            super(success);
            this.authtoken = authtoken;
            this.username = username;
            this.personID = personID;
    }

    /**
     * Constructs the LoginResult with a message.
     * @param success The success status of the login process.
     * @param message The message returned by the login service.
     */
    public LoginResult(boolean success, String message) {
            super(success, message);
            authtoken = null;
            username = null;
            personID = null;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public String getUsername() {
        return username;
    }

    public String getPersonID() {
        return personID;
    }
}
