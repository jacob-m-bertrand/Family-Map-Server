package result;

import model.Person;

/**
 * Stores the result of PersonService (/person/[personID])
 */
public class PersonResult extends Result {
    private final Person person;

    public PersonResult(String message, boolean success, Person person) {
        super(message, success);
        this.person = person;
    }
}
