package passoff.dao;

import dao.EventDAO;
import dao.tools.TableTools;
import exception.DataAccessException;
import exception.NotFoundException;
import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.SampleTestData;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class for the {@link EventDAO}.
 */
public class EventDAOTest {
    /** The Data Access Object being tested. */
    private static EventDAO dao;

    /** A list of sample events which can be used for testing
     * @see SampleTestData
     * */
    private static ArrayList<Event> events;

    /**
     * Sets up the testing environment.
     */
    @BeforeAll
    static void setup() {
        // Initialize the array of events we can use throughout the tests
        SampleTestData testData = new SampleTestData();
        events = testData.getEvents();

        // Initialize the dao object
        try {
            dao = new EventDAO();
            dao.clear(); // Clear any existing data, so we can have a clean run

        } catch (Exception e) {
            fail("Database connection failed!");
        }
    }

    /**
     * Cleans up the testing environment between runs by clearing the Event table.
     */
    @AfterEach
    void cleanup() {
        try {
            dao.clear();
        }
        catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Positive test case testing insertion of a single event into the table.
     */
    @Test
    @DisplayName("Inserting Single Event")
    void insertSingleTest() {
        // Get a single event from the Array
        Event singleEvent = events.get(1);
        Event found = null;

        try {
            // Insert the event, store the results of trying to find it in the table
            dao.insert(singleEvent);
            found = dao.find(singleEvent.getEventID());

        } catch (Exception e) {
            fail("Inserting single row threw: " + e.getMessage());
        }

        // Make sure that it could actually find the event and instantiate it properly
        assertNotNull(found);

        // Check that the objects are equal
        assertEquals(found, singleEvent);
    }

    /**
     * Positive test case testing insertion of multiple events into the table.
     */
    @Test
    @DisplayName("Bulk Insertion")
    void insertBulkTest() {
        // Create a new Array for the events which we will find
        ArrayList<Event> findResults = new ArrayList<Event>();

        try {
            // Insert all the events in the events array
            for (Event e : events) {
                dao.insert(e);
            }

            // Find all the events by their event id
            for (Event e : events) {
                String idToFind = e.getEventID();
                findResults.add(dao.find(idToFind));
            }
        } catch (Exception e) {
            fail("Bulk insertion row threw: " + e.getMessage());
        }

        // Make sure none of the events are null
        for (Event e : findResults) {
            assertNotNull(e);
        }

        // The original array should equal the results array if all of them were added and found
        assertEquals(events, findResults);
    }

    /**
     * Negative test case testing an event being inserted twice.
     */
    @Test
    @DisplayName("Duplicate Insertion Testing")
    void duplicateInsertionTest() {
        // Get an event from the array
        Event singleEvent = events.get(1);

        try {
            dao.insert(singleEvent);

        } catch (Exception e) {
            fail("Inserting single row threw: " + e.getMessage());
        }

        // This should produce an error, since the Event was already added before and the table rows must be unique
        assertThrows(DataAccessException.class, () -> dao.insert(singleEvent));
    }

    /**
     * Positive test case testing finding one event in the table.
     */
    @Test
    @DisplayName("Find Testing")
    void findTest() {
        // Get an event from the array
        Event toFind = events.get(3);

        try {
            // Insert all the sample events into the table
            for (Event e : events) {
                dao.insert(e);
            }

            // Find the event by it's ID
            Event returned = dao.find(toFind.getEventID());
            assertEquals(toFind, returned);

        } catch (Exception e) {
            fail("Inserting single row threw: " + e.getMessage());
        }
    }

    /**
     * Negative test case testing the finding of an event which is not in the table.
     */
    @Test
    @DisplayName("Finding Non-Existent Event")
    void doesNotExistTest() {
        // This event should not be in the table
        Event toFind = new Event("Time Traveler", UUID.randomUUID().toString(), 48.8588897f, 2.320041f, "France",
                "Paris", "born", 1767);

        try {
            // Insert all sample events into the table (does not include toFind)
            for (Event e : events) {
                dao.insert(e);
            }

            // Attempting to find the non-existent event should throw a NotFoundException
            assertThrows(NotFoundException.class, () -> dao.find(toFind.getEventID()));

        } catch (Exception e) {
            fail("Finding non-existent event threw: " + e.getMessage());
        }
    }

    /**
     * Positive test case which tests listing a user's events.
     */
    @Test
    @DisplayName("List By Username")
    void listByUsernameTest() {
        // This array will store the events associated with one user
        ArrayList<Event> bestEvents = new ArrayList<Event>();

        try {
            // Iterate through the events, and insert all events into the table. Add the events by jmbertrand to bestEvents
            for(Event e : events) {
                dao.insert(e);

                if (e.getAssociatedUsername().equals("jmbertrand")) {
                    bestEvents.add(e);
                }
            }

            // bestEvents and the array created by the list function should be equal
            assertEquals(bestEvents, dao.listUserEvents("jmbertrand"));

        } catch (Exception e) {
            fail("Listing by username threw: " + e.getMessage());
        }
    }

    /**
     * Negative test case which tests listing of a user which is not in the database.
     */
    @Test
    @DisplayName("List Not Found")
    void doesNotExistListTest(){
        try {
            // Insert all sample events into the table
            for (Event e : events) {
                dao.insert(e);
            }

            // Time traveler shouldn't be in the database, make sure the list function throws a NotFoundException
            assertThrows(NotFoundException.class, () -> dao.listUserEvents("Time Traveler"));

        } catch (Exception e) {
            fail("Listing non-existent username threw: " + e.getMessage());
        }
    }

    /**
     * Positive test case testing the clear functionality.
     */
    @Test
    @DisplayName("Clear Testing")
    void clearTesting() {
        try {
            // Bulk insert items, then clear, and make sure we have an empty table
            for (Event e : events) {
                dao.insert(e);
            }

            dao.clear();
            assertTrue(TableTools.tableIsEmpty("Event"));

        } catch (Exception e) {
            fail("Clearing table threw: " + e.getMessage());
        }

    }
}
