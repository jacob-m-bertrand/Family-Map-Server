package request;

import result.PersonResult;

/**
 * Stores data which will be passed to the PersonService (/person/[personID])
 */
public class PersonRequest {
    private final String personID;

    public PersonRequest(String personID) {
        this.personID = personID;
    }

    public String getPersonID() { return personID; }
}
