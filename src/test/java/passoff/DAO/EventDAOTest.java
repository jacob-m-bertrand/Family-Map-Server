package passoff.DAO;

import dao.*;

import dao.TableTools;
import model.Event;
import model.User;
import model.Person;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class EventDAOTest {
    /** Stores data which will be needed throughout the tests */
    private EventDAO dao;
    private ArrayList<Event> events;
    private static final User user = new User("jmbertrand", "cs240isgreat", "jacobbertrand22@gmail.com", "Jacob", "Bertrand", "m");
    private static final User otherUser = new User("BaguetteBird", "cs240isnotgreat", "baguette@bird.com", "Baguette", "Bird", "f");
    private static final Person userPerson = new Person(user);

    @BeforeEach
    @DisplayName("Initializing DAO with sample data")
    void setup() {
        // Initialize the array of events we can use throughout the tests
        events = new ArrayList<Event>();
        events.add(new Event(user.getUsername(), user.getPersonID(), 38.974f, -94.683f, "United States", "Overland Park", "lived", 2023));
        events.add(new Event(user.getUsername(), user.getPersonID(), 33.836594f, -117.914299f, "United States", "Anaheim", "born", 2001));
        events.add(new Event(otherUser.getUsername(), otherUser.getPersonID(), 40.712776f, -74.005974f, "United States", "New York", "lived", 1988));
        events.add(new Event(otherUser.getUsername(), otherUser.getPersonID(), 28.538336f, -81.379234f, "United States", "Orlando", "born", 2001));
        events.add(new Event(otherUser.getUsername(), otherUser.getPersonID(), 48.8588897f, 2.320041f, "France", "Paris", "lived", 2008));
        events.add(new Event(user.getUsername(), user.getPersonID(), -19.6808372f, 63.422113f, "Rodrigues", "Port Mathurin", "lived", 2023));
        events.add(new Event(user.getUsername(), user.getPersonID(), -20.881933212280273f, 55.4539680480957f, "Reunion Island", "Saint-Denis", "lived", 2022));

        // Initialize the dao object
        try {
            Connection conn = Database.getConnection();

            dao = new EventDAO(conn);
            dao.clear(); // Clear any existing data, so we can have a clean run
        }
        catch(Exception e) {
            fail("Database connection failed!");
        }
    }

    @AfterEach
    void tearDown() {
        try {
            dao.clear(); // Clear all testing data so it doesn't affect production later
        }
        catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    @DisplayName("Inserting Single Row")
    void insertSingleTest() {
        // Get a single event from the Array
        Event singleEvent = events.get(1);
        Event found = null;

        try {
            // Insert the event, store the results of trying to find it in the table
            dao.insert(singleEvent);
            found = dao.find(singleEvent.getEventID());
        }
        catch(Exception e) {
            fail("Inserting single row threw: " + e.getMessage());
        }

        // Make sure that it could actually find the event and instantiate it properly
        assertNotNull(found);

        // Check that the objects are equal
        assertEquals(found, singleEvent);
    }


    @Test
    @DisplayName("Bulk Insertion")
    void insertBulkTest() {
        ArrayList<Event> findResults = new ArrayList<Event>();

        try {
            // Insert all the events in the events array, then store the results of finding them
            for(Event e : events) dao.insert(e);
            for(Event e : events) {
                String idToFind = e.getEventID();
                findResults.add(dao.find(idToFind));
            }
        }
        catch(Exception e) {
            fail("Bulk insertion row threw: " + e.getMessage());
        }

        // Make sure none of the events are null
        for(Event e : findResults) assertNotNull(e);

        // The original array should equal the results array if all of them were added and found
        assertEquals(events, findResults);
    }


    @Test
    @DisplayName("Duplicate Insertion Testing")
    void duplicateInsertionTest() {
        Event singleEvent = events.get(1);

        try {
            dao.insert(singleEvent);
        }
        catch(Exception e) {
            fail("Inserting single row threw: " + e.getMessage());
        }

        // This should produce an error, since the Event was already added before and the table rows must be unique
        assertThrows(DataAccessException.class, () -> dao.insert(singleEvent));
    }


    @Test
    @DisplayName("Find Testing")
    void findTest() {
        Event toFind = events.get(3);
        // Event to find - events.add(new Event(otherUser.getUsername(), otherUser.getPersonID(), 48.8588897f, 2.320041f, "France", "Paris", "lived", 2008));

        try {
            for(Event e : events) dao.insert(e);
            Event returned = dao.find(toFind.getEventID());

            assertEquals(toFind, returned);
        }
        catch(Exception e) {
            fail("Inserting single row threw: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Finding Non-Existent Event")
    void dneTest() {
        // This event (of an evil time traveler!) should not be in the table. This should throw a NotFoundException
        Event toFind = new Event("Time Traveler", UUID.randomUUID().toString(), 48.8588897f, 2.320041f, "France", "Paris", "born", 1767);

        try {
            for(Event e : events) dao.insert(e);

            assertThrows(NotFoundException.class, () -> dao.find(toFind.getEventID()));
        }
        catch(Exception e) {
            fail("Finding non-existent event threw: " + e.getMessage());
        }
    }


    @Test
    @DisplayName("List By Username")
    void listByUsernameTest() {
        // Only store some events - the *best* events - so that we can test the filtering capablities of the list function
        ArrayList<Event> bestEvents = new ArrayList<Event>();

        try {
            for(Event e : events) {
                dao.insert(e);
                if(e.getAssociatedUsername().equals("jmbertrand")) bestEvents.add(e);
            }

            // bestEvents and the array created by the list function should be equal
            assertEquals(bestEvents, dao.list("associatedUsername", "jmbertrand"));

        }
        catch(Exception e) {
            fail("Listing by username threw: " + e.getMessage());
        }
    }


    @Test
    @DisplayName("List By Person")
    void listByPersonIDTest() {
        // Same as above, except we filter by personID instead of username
        ArrayList<Event> bestEvents = new ArrayList<Event>();

        try {
            for(Event e : events) {
                dao.insert(e);
                if(e.getPersonID().equals(userPerson.getPersonID())) bestEvents.add(e);
            }

            assertEquals(bestEvents.size(), dao.list("personID", userPerson.getPersonID()).size());

        }
        catch(Exception e) {
            fail("Listing by username threw: " + e.getMessage());
        }
    }


    @Test
    @DisplayName("List Not Found")
    void dneListTest(){
        try {
            for(Event e : events) {
                dao.insert(e);
            }

            // Time traveler shouldn't be in the database, make sure the list function throws a NotFoundException
            assertThrows(NotFoundException.class, () -> dao.list("associatedUsername", "Time Traveler"));

        }
        catch(Exception e) {
            fail("Listing non-existent username threw: " + e.getMessage());
        }
    }


    @Test
    @DisplayName("Clear Testing")
    void clearTesting() {
        try {
            // Bulk insert items, then clear, and make sure we have an empty table
            for(Event e : events) dao.insert(e);

            dao.clear();
            assertTrue(TableTools.tableIsEmpty("Event"));
        }
        catch(Exception e) {
            fail("Clearing table threw: " + e.getMessage());
        }

    }
}
