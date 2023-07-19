package result;

import model.Person;

import java.util.ArrayList;

/**
 * Stores the result of the person listing process in {@link service.ListPersonsService}.
 */
public class ListPersonsResult extends Result {
    /** The list of {@link Person} objects returned by the service. */
    private final ArrayList<Person> data;

    /**
     * Constructs the ListPersonsResult with a message.
     * @param success The success status of the service.
     * @param message The message returned by the service.
     */
    public ListPersonsResult(boolean success, String message) {
        super(success, message);
        data = null;
    }

    /**
     * Constructs the ListPersonsResult with the requested list of people.
     * @param success    The success status of the service.
     * @param data The requested list of {@link Person} objects.
     */
    public ListPersonsResult(boolean success, ArrayList<Person> data) {
        super(success);
        this.data = data;
    }

    public ArrayList<Person> getData() {
        return data;
    }
}
