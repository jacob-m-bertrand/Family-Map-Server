package request;

import model.Event;
import model.Person;
import model.User;

import java.util.ArrayList;

/**
 * Stores data which will be passed to the LoadService (/load)
 */
public class LoadRequest {
    /** Store the data used to fill the database after its cleared */
    private final ArrayList<User> users;
    private final ArrayList<Person> persons;
    private final ArrayList<Event> events;

    /** Take in the fields while constructing the LoadRequest */
    public LoadRequest(ArrayList<User> users, ArrayList<Person> persons, ArrayList<Event> events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public ArrayList<User> getUsers() { return users; }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

}
