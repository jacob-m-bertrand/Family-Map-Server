package request;

/**
 * Base class for all requests which require an authtoken.
 */
public class AuthenticatedRequest {
    /** The authtoken provided for authentication. */
    private String authtoken;

    /**
     * Construct a new AuthenticatedRequest using the provided authtoken.
     * @param authtoken The authtoken stored in the request.
     */
    public AuthenticatedRequest(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }
}
