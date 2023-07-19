package passoff.service;

import dao.EventDAO;
import dao.PersonDAO;
import dao.UserDAO;
import exception.DataAccessException;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.SampleTestData;
import request.FillRequest;
import result.Result;
import service.FillService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class for the {@link FillService}.
 */
public class FillTest {
    /** Data Access Object for the Event table. */
    private EventDAO eventDAO;

    /** Data Access Object for the User table. */
    private UserDAO userDAO;

    /** Data Access Object for the Person table. */
    private PersonDAO personDAO;

    /** Sample user for use during the test */
    private User user;


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

            // Clear all tables
            eventDAO.clear();
            userDAO.clear();
            personDAO.clear();

            // Get sample test data, and insert a user from it into the user table
            SampleTestData testData = new SampleTestData();
            user = testData.getUsers().get(0);

            userDAO.insert(user);

        } catch (DataAccessException d) {
            fail("Setting up threw: " + d.getMessage());
        }
    }

    /**
     * Cleans up the testing environment after each test.
     */
    @AfterEach
    void cleanup() {
        try {
            // Clear all tables
            userDAO.clear();
            eventDAO.clear();
            personDAO.clear();

        } catch (DataAccessException d) {
            fail("Tearing down threw: " + d.getMessage());
        }
    }

    /**
     * Positive test case which tests a very large fill (6 generations).
     */
    @Test
    @DisplayName("Large Fill")
    void largeFillTest() {
        // Create the fill request and service
        FillRequest request = new FillRequest(user.getUsername(), 6);
        FillService service = new FillService(request);

        // Execute the fill and store the result
        Result result = service.generateFamilyHistoryData();

        assertTrue(result.isSuccess());

        try {
            /*
            * How to calculate expected number of events and people:
            * People: 1 + sigma(1,num_generations) 2^n
            * Events 1 + sigma(1, num_generations) 3*(2^n)
            */
            ArrayList<Event> userEvents = eventDAO.listUserEvents(user.getUsername());
            assertEquals(379, userEvents.size());

            ArrayList<Person> userPeople = personDAO.listUserPersons(user.getUsername());
            assertEquals(127 , userPeople.size());

        } catch (Exception e) {
            fail("fill service threw while listing: " + e.getMessage());
        }
    }

    /**
     * Positive test case which tests a small fill (2 generations).
     */
    @Test
    @DisplayName("Small Fill")
    void smallFillTest() {
        // Create the fill request and service
        FillRequest request = new FillRequest(user.getUsername(), 2);
        FillService service = new FillService(request);

        // Execute the fill and store the result
        Result result = service.generateFamilyHistoryData();

        assertTrue(result.isSuccess());

        try {
            // Using the formulas above (in large fill), check expecteds vs actuals
            ArrayList<Event> userEvents = eventDAO.listUserEvents(user.getUsername());
            assertEquals(19, userEvents.size());

            ArrayList<Person> userPeople = personDAO.listUserPersons(user.getUsername());
            assertEquals(7 , userPeople.size());
        } catch (Exception e) {
            fail("fill service threw while listing: " + e.getMessage());
        }
    }

    /**
     * Negative test case which tests an invalid number of generations.
     * No generation number above 7 is accepted, to prevent server overload (8 generations produces 1531 events!)
     */
    @Test
    @DisplayName("Invalid generation number")
    void invalidGenerationTest() {
        // Create the request and service
        FillRequest request = new FillRequest(user.getUsername(), 8);
        FillService service = new FillService(request);

        // Execute the fill
        Result fillResult = service.generateFamilyHistoryData();

        // The result success should be false and the error message should match the problem
        assertFalse(fillResult.isSuccess());
        assertEquals("Error: Invalid request (Number of generations is outside of range. Valid numbers are between 0 " +
                "and 6, inclusive.).", fillResult.getMessage());
    }

    /**
     * Negative test case which tests an invalid username (one that is not in the database).
     */
    @Test
    @DisplayName("Invalid username")
    void invalidUsernameTest() {
        // Create the request and service
        FillRequest request = new FillRequest("thetimetraveller", 4);
        FillService service = new FillService(request);

        // Execute the fill
        Result fillResult = service.generateFamilyHistoryData();

        // The result success should be false and the error message should match the problem
        assertFalse(fillResult.isSuccess());
        assertEquals("Error: Invalid request (user does not exist).",
                fillResult.getMessage());
    }
}
