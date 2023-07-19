package request;

import model.Event;
import model.Person;
import model.User;

import java.util.ArrayList;

/**
 * Stores data which is used while loading the database in LoadService
 */
public class LoadRequest {
    /** A list of users to add to the database. */
    private final ArrayList<User> users;

    /** A list of people to add to the database. */
    private final ArrayList<Person> persons;

    /** A list of events to add to the database. */
    private final ArrayList<Event> events;


    /**
     * Constructs the LoadRequest object with the provided lists.
     * @param users     A list of users to add to the database.
     * @param persons   A list of people to add to the database.
     * @param events    A list of events to add to the database.
     */
    public LoadRequest(ArrayList<User> users, ArrayList<Person> persons, ArrayList<Event> events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public ArrayList<User> getUsers() {
       return users;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

}
