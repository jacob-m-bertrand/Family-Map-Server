package result;

import model.Person;

import java.util.ArrayList;

/**
 * Stores the result of ListPersonsService (/person)
 */
public class ListPersonsResult extends Result {
    /** Store an ArrayList of all the persons in the database */
    private final ArrayList<Person> data;

    /** Take in result data while constructing the ListPersonsResult */
    public ListPersonsResult(String message, boolean success, ArrayList<Person> data) {
        super(message, success);
        this.data = data;
    }

    public ArrayList<Person> getData() {
        return data;
    }
}
