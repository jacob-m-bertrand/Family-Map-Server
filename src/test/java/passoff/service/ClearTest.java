package passoff.service;

import dao.AuthtokenDAO;
import dao.EventDAO;
import dao.PersonDAO;
import dao.UserDAO;
import dao.tools.TableTools;
import model.Authtoken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.SampleTestData;
import result.Result;
import service.ClearService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Testing class for the {@link ClearService}.
 */
public class ClearTest {
    /** The ClearService used for testing. */
    private static ClearService clearService;

    /** The Data Access Object for the Person table. */
    private static PersonDAO personDAO;

    /** The Data Access Object for the User table. */
    private static UserDAO userDAO;

    /** The Data Access Object for the Authtoken table. */
    private static AuthtokenDAO authtokenDAO;

    /** The Data Access Object for the Event table. */
    private static EventDAO eventDAO;

    /** The sample test data used for this test. See {@link SampleTestData}. */
    private static SampleTestData testData;

    /**
     * Sets up the testing environment.
     */
    @BeforeAll
    static void setup() {
        // Initialize DAO's
        userDAO = new UserDAO();
        personDAO = new PersonDAO();
        authtokenDAO = new AuthtokenDAO();
        eventDAO = new EventDAO();

        // Initialize the clear service
        clearService = new ClearService();

        // Initialize the test data
        testData = new SampleTestData();

        try {
            // Clear the database for a clean test run
            personDAO.clear();
            userDAO.clear();
            authtokenDAO.clear();
            eventDAO.clear();

        } catch (Exception e) {
            fail("Error thrown while clearing: " + e.getMessage());
        }

    }

    /**
     * Positive test case which tests clearing a database in which all tables have data
     */
    @Test
    @DisplayName("Clearing Full Database")
    void clearTest() {
        try {
            // Insert sample data into the tables
            for (User u : testData.getUsers()) {
                userDAO.insert(u);
            }
            for(Person p : testData.getPeople()) {
                personDAO.insert(p);
            }
            for(Authtoken a : testData.getAuthtokens()) {
                authtokenDAO.insert(a);
            }
            for(Event e : testData.getEvents()) {
                eventDAO.insert(e);
            }

            // Run the clear service and store the result
            Result result = clearService.clear();

            // The success of the service should be true
            assertTrue(result.isSuccess());

            // Ensure that the clear itself worked (all tables are empty
            assertTrue(TableTools.tableIsEmpty("User"));
            assertTrue(TableTools.tableIsEmpty("Person"));
            assertTrue(TableTools.tableIsEmpty("Event"));
            assertTrue(TableTools.tableIsEmpty("Authtoken"));

        } catch (Exception e) {
            fail("Error thrown while clearing: " + e.getMessage());
        }
    }

    /**
     * Positive test case which test clearing an empty database (which should still work, though will do nothing).
     */
    @Test
    @DisplayName("Clearing Empty Database")
    void clearEmptyDatabaseTest() {
        try {
            // Run the clear service
            Result clearResult = clearService.clear();

            // Assert that the clear was a success
            assertTrue(clearResult.isSuccess());

        } catch (Exception e) {
            fail("Error thrown while clearing: " + e.getMessage());
        }
    }

    /**
     * Positive test case which test clearing a database in which only one table has data.
     */
    @Test
    @DisplayName("Clearing Partially Empty Database")
    void clearPartialDatabaseTest() {
        try {
            // Insert all sample users into the table
            for (User u : testData.getUsers()) {
                userDAO.insert(u);
            }

            // Run the clear service and store the result
            Result result = clearService.clear();

            // The result should be one of success, and the user table should be empty
            assertTrue(result.isSuccess());
            assertTrue(TableTools.tableIsEmpty("User"));

        } catch (Exception e) {
            fail("Error thrown while clearing: " + e.getMessage());
        }
    }
}
