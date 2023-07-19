package passoff;

import model.Authtoken;
import model.Event;
import model.Person;
import model.User;

import java.util.ArrayList;

/**
 * Contains sample data used during testing.
 */
public class SampleTestData {
    /** An ArrayList containing sample events. */
    private final ArrayList<Event> events;

    /** An ArrayList containing sample users. */
    private final ArrayList<User> users;

    /** An ArrayList containing sample people. */
    private final ArrayList<Person> people;

    /** An ArrayList containing sample authtokens. */
    private final ArrayList<Authtoken> authtokens;

    /**
     * Constructs the SampleTestData, and adds sample data to the arrays.
     */
    public SampleTestData() {
        // Sample users
        users = new ArrayList<User>();
        users.add(new User("jmbertrand", "CS240isgreat", "jacobbertrand22@gmail.com", "Jacob", "Bertrand", "m"));
        users.add(new User("baguettebird", "CS240isnotgreat", "baguette@bird.com", "Baguette", "Bird", "m"));
        users.add(new User("theevilone", "iamevil", "timetraveller@gmail.com", "Time", "Traveller", "f"));
        users.add(new User("tomscott", "bumblebee", "tom.scott@youtube.com", "Tom", "Scott", "m"));
        users.add(new User("alienbro", "spaceiscool", "alienbro@mars.com", "Alien", "X", "m"));

        // Store data on two users for whom we will make events
        User user = users.get(0);
        User otherUser = users.get(1);

        // Sample events
        events = new ArrayList<Event>();
        events.add(new Event(user.getUsername(), user.getPersonID(), 38.974f, -94.683f, "United States", "Overland Park", "lived", 2023));
        events.add(new Event(user.getUsername(), user.getPersonID(), 33.836594f, -117.914299f, "United States", "Anaheim", "born", 2001));
        events.add(new Event(otherUser.getUsername(), otherUser.getPersonID(), 40.712776f, -74.005974f, "United States", "New York", "lived", 1988));
        events.add(new Event(otherUser.getUsername(), otherUser.getPersonID(), 28.538336f, -81.379234f, "United States", "Orlando", "born", 2001));
        events.add(new Event(otherUser.getUsername(), otherUser.getPersonID(), 48.8588897f, 2.320041f, "France", "Paris", "lived", 2008));
        events.add(new Event(user.getUsername(), user.getPersonID(), -19.6808372f, 63.422113f, "Rodrigues", "Port Mathurin", "lived", 2023));
        events.add(new Event(user.getUsername(), user.getPersonID(), -20.881933212280273f, 55.4539680480957f, "Reunion Island", "Saint-Denis", "lived", 2022));

        // Sample person
        people = new ArrayList<Person>();
        people.add(new Person(user.getUsername(), "Bob", "Smith", "m"));
        people.add(new Person(user.getUsername(), "Sam", "Smith", "f"));
        people.add(new Person(user.getUsername(), "Helen", "Grey", "f"));
        people.add(new Person(user.getUsername(), "Todd", "White", "m"));
        people.add(new Person(otherUser.getUsername(), "Walker", "Jones", "m"));
        people.add(new Person(otherUser.getUsername(), "Alan", "Walker", "m"));
        people.add(new Person(otherUser.getUsername(), "Avicii", "Alanson", "m"));

        // Sample authtoken
        authtokens = new ArrayList<Authtoken>();
        authtokens.add(new Authtoken(user.getUsername()));
        authtokens.add(new Authtoken(user.getUsername()));
        authtokens.add(new Authtoken(otherUser.getUsername()));
        authtokens.add(new Authtoken(otherUser.getUsername()));
        authtokens.add(new Authtoken("tomscott"));
        authtokens.add(new Authtoken("tomscott"));
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public ArrayList<Authtoken> getAuthtokens() {
        return authtokens;
    }
}
