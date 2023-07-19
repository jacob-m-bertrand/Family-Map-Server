package request;

/**
 * Stores data used during the person retrieval process in PersonService.
 */
public class PersonRequest extends AuthenticatedRequest {
    /** The personID of the person to find. */
    private final String personID;

    /**
     * Constructs the PersonRequest object using data passed in. Takes in an authtoken as well, as this is an
     * AuthenticatedRequest.
     * @param personID  The personID of the person to find.
     * @param authtoken The provided authtoken.
     */
    public PersonRequest(String personID, String authtoken) {
        super(authtoken);
        this.personID = personID;
    }

    public String getPersonID() {
        return personID;
    }
}
