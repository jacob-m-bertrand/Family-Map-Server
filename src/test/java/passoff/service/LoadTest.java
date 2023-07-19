package passoff.service;

import dao.EventDAO;
import dao.PersonDAO;
import dao.UserDAO;
import dao.tools.TableTools;
import exception.DataAccessException;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.SampleTestData;
import request.LoadRequest;
import result.Result;
import service.LoadService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.testng.AssertJUnit.assertFalse;

/**
 * Testing class for the {@link LoadTest}.
 */
public class LoadTest {
    /** Data Access Object for the Event table. */
    private EventDAO eventDAO;

    /** Data Access Object for the User table. */
    private UserDAO userDAO;

    /** Data Access Object for the Person table. */
    private PersonDAO personDAO;

    /** Sample data to be used for testing. */
    private SampleTestData testData;

    /**
     * Sets up the testing environment.
     */
    @BeforeEach
    void setup() {
        try {
            // Initialize DAOs
            eventDAO = new EventDAO();
            userDAO = new UserDAO();
            personDAO = new PersonDAO();

            // Clear the database
            eventDAO.clear();
            userDAO.clear();
            personDAO.clear();

            // Generate sample test data
            testData = new SampleTestData();
        } catch (DataAccessException d) {
            fail("Clearing and repopulating database threw: " + d.getMessage());
        }
    }

    /**
     * Cleans up the testing environment after each test.
     */
    @AfterEach
    void cleanup() {
        try {
            // Clear the database
            eventDAO.clear();
            userDAO.clear();
            personDAO.clear();

        } catch (DataAccessException d) {
            fail("Clearing database threw: " + d.getMessage());
        }
    }

    /**
     * Positive test case which tests the load service in totality.
     */
    @Test
    @DisplayName("Load Testing")
    void loadTest() {
        // Build three arrays with the objects to load into the database
        ArrayList<Event> eventsToLoad = new ArrayList<Event>();
        ArrayList<User> usersToLoad = new ArrayList<User>();
        ArrayList<Person> peopleToLoad = new ArrayList<Person>();

        for (int i = 3; i < testData.getEvents().size(); ++i) {
            eventsToLoad.add(testData.getEvents().get(i));
        }
        for (int i = 3; i < testData.getUsers().size(); ++i) {
            usersToLoad.add(testData.getUsers().get(i));
        }
        for (int i = 3; i < testData.getPeople().size(); ++i) {
            peopleToLoad.add(testData.getPeople().get(i));
        }

        // Build the request
        LoadRequest request = new LoadRequest(usersToLoad, peopleToLoad, eventsToLoad);
        LoadService service = new LoadService(request);
        Result result = service.load();

        // The result success should be true
        assertTrue(result.isSuccess());

        // The tables should not be empty
        try {
            assertFalse(TableTools.tableIsEmpty("Event"));
            assertFalse(TableTools.tableIsEmpty("User"));
            assertFalse(TableTools.tableIsEmpty("Person"));
        } catch (DataAccessException d) {
            fail("Checking table status threw: " + d.getMessage());
        }
    }

    /**
     * Negative test case which tests loading with an empty array.
     */
    @Test
    @DisplayName("One Empty Array Testing")
    void oneEmptyArrayTest() {
        // Build three arrays, and add sample data to two of them
        ArrayList<Event> eventsToLoad = new ArrayList<Event>();
        ArrayList<User> usersToLoad = new ArrayList<User>();
        ArrayList<Person> peopleToLoad = new ArrayList<Person>();

        for (int i = 3; i < testData.getEvents().size(); ++i) {
            eventsToLoad.add(testData.getEvents().get(i));
        }
        for (int i = 3; i < testData.getUsers().size(); ++i) {
            usersToLoad.add(testData.getUsers().get(i));
        }

        // Create the request and service, then execute the load
        LoadRequest request = new LoadRequest(usersToLoad, peopleToLoad, eventsToLoad);
        LoadService service = new LoadService(request);
        Result result = service.load();

        // The result success should be false
        assertFalse(result.isSuccess());
    }
}
