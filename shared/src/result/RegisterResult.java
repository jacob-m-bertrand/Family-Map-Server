package result;

/**
 * Stores the result of the registration process in {@link service.RegisterService}. This data is then used to log the user in.
 */
public class RegisterResult extends Result {
    /** The authorization token for this session, used later to validate user while accessing data. */
    private final String authtoken;

    /** The user's username, which will also be used to validate the user while accessing data. */
    private final String username;

    /** The user's personID, used to connect them to their family tree. */
    private final String personID;

    /**
     * Constructs a new RegisterResult with a message, intended to be used when the process fails.
     * @param success The success status of the registration.
     * @param message The message returned by the RegisterService.
     */
    public RegisterResult(boolean success, String message) {
        super(success, message);
        this.authtoken = null;
        this.username = null;
        this.personID = null;
    }

    /**
     * Constructs a new RegisterResult with the information required to log the user in.
     * @param success   The success status of the registration.
     * @param authtoken The authtoken for this session.
     * @param username  The user's username.
     * @param personID  The user's personID.
     */
    public RegisterResult(boolean success, String authtoken, String username, String personID) {
        super(success);
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
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
