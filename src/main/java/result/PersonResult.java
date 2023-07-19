package result;

import model.Person;

/**
 * Stores the result of the person retrieval process in PersonService.
 */
public class PersonResult extends Result {
    /** The ID of the person from the database. Must be unique. */
    private final String personID;

    /** The username associated with this person. */
    private final String associatedUsername;

    /** The first name of the person. */
    private final String firstName;

    /** The last name of the person. */
    private final String lastName;

    /** The gender of the person. */
    private final String gender;

    /** The personID of this person's father. May be null. */
    private final String fatherID;

    /** The personID of this person's mother. May be null. */
    private final String motherID;

    /** The personID of this person's spouse. May be null. */
    private final String spouseID;

    /**
     * Constructs a new PersonResult object using the provided data.
     * @param success The success status of the result.
     * @param message The message to be returned.
     */
    public PersonResult(boolean success, String message) {
        super(success, message);
        personID = null;
        associatedUsername = null;
        firstName = null;
        lastName = null;
        gender = null;
        fatherID = null;
        motherID = null;
        spouseID = null;
    }

    /**
     * Constructs a new PersonResult object using the provided data.
     * @param success The success status of the result.
     * @param person  A {@link Person} object with data of the person found by {@link service.PersonService}.
     */
    public PersonResult(boolean success, Person person) {
        super(success);
        personID = person.getPersonID();
        associatedUsername = person.getAssociatedUsername();
        firstName = person.getFirstName();
        lastName = person.getLastName();
        gender = person.getGender();
        fatherID = person.getFatherID();
        motherID = person.getMotherID();
        spouseID = person.getSpouseID();
    }

    /**
     * Turns this result into a person object, for testing purposes.
     * @return A person with the data contained in this result.
     */
    public Person toPerson() {
        if (personID == null) {
            return null;
        } else {
            return new Person(
                    personID,
                    associatedUsername,
                    firstName,
                    lastName,
                    gender,
                    fatherID,
                    motherID,
                    spouseID
            );
        }
    }
}
