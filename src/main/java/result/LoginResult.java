package result;

import model.Authtoken;

/**
 * Stores the result of the LoginService (/user/login)
 */
public class LoginResult extends Result {
    /** Store the results of the login, which are final */
    private final Authtoken authtoken;
    private final String username;
    private final String personID;


    /** Constructor if a success */
    public LoginResult(String message, boolean success, Authtoken authtoken, String username, String personID) {
            super(message, success);
            this.authtoken = authtoken;
            this.username = username;
            this.personID = personID;
    }

    /** Constructor if fail */
    public LoginResult(String errorMessage) {
            super("Error:" + errorMessage, false);
            authtoken = null;
            username = null;
            personID = null;
    }

    public Authtoken getAuthtoken() {
        return authtoken;
    }

    public String getUsername() {
        return username;
    }

    public String getPersonID() {
        return personID;
    }
}
