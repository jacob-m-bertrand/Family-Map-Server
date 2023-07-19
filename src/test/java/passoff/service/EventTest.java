package passoff.service;

import dao.AuthtokenDAO;
import dao.EventDAO;
import exception.DataAccessException;
import model.Authtoken;
import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.SampleTestData;
import request.EventRequest;
import result.EventResult;
import service.EventService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class for the {@link EventService}.
 */
public class EventTest {
    /** The Data Access Object for the Event table. */
    private EventDAO eventDAO;

    /** The Data Access Object for the Authtoken table. */
    private AuthtokenDAO authtokenDAO;

    /** The sample data used for this test. See {@link SampleTestData}. */
    private SampleTestData testData;

    /**
     * Sets up the testing environment.
     */
    @BeforeEach
    void setup() {
        // Initialize the DAOs
        eventDAO = new EventDAO();
        authtokenDAO = new AuthtokenDAO();

        // Initialize sample data
        testData = new SampleTestData();

        // Each test relies on the Event table having data, so insert sample data into the table before each test
        try {
            for (Event e : testData.getEvents()) {
                eventDAO.insert(e);
            }
        } catch (DataAccessException d) {
            fail("Inserting event threw: " + d.getMessage());
        }
    }

    /**
     * Cleans up the testing environment after each test by clearing the table each time.
     */
    @AfterEach
    void cleanup() {
        try {
            eventDAO.clear();
            authtokenDAO.clear();

        } catch (Exception e) {
            fail("Clearing Events threw: " + e.getMessage());
        }
    }

    /**
     * Positive test case which tests finding a single event.
     */
    @Test
    @DisplayName("Finding Event")
    void eventFindTest() {
        // Get an authtoken so we can make the request, then pull an event to find
        Authtoken authtoken = testData.getAuthtokens().get(0);
        Event eventToFind = testData.getEvents().get(0);

        // Insert our authtoken into the database so we can authenticate
        try {
            authtokenDAO.insert(authtoken);

        } catch (DataAccessException d) {
            fail("Inserting authtoken threw: " + d.getMessage());
        }

        // Create our request
        EventRequest request = new EventRequest(eventToFind.getEventID(), authtoken.getAuthtoken());

        // Create and run the service, then store the result
        EventService eventService = new EventService(request);
        EventResult result = eventService.findEvent();

        // The result success should be true, and the found event should match the one we pulled
        assertTrue(result.isSuccess());
        assertEquals(eventToFind, result.toEvent());
    }

    /**
     * Negative test case which tests finding an event which is not in the table
     */
    @Test
    @DisplayName("Finding Non-Existent Event")
    void eventDoesNotExistTest() {
        // Pull an authtoken so we can authenticate, and create an event which isn't in the table
        Authtoken authtoken = testData.getAuthtokens().get(0);
        Event eventNotInTable = new Event("theevilone", "randompersonid", 7f, 9f, "United States", "Ghost Town",
                "birth?", 1656);

        // Insert the authtoken into the database for authentication
        try {
            authtokenDAO.insert(authtoken);

        } catch (DataAccessException d) {
            fail("Inserting authtoken threw: " + d.getMessage());
        }

        // Create the request
        EventRequest request = new EventRequest(eventNotInTable.getEventID(), authtoken.getAuthtoken());

        // Create and run the service, and store the result
        EventService eventService = new EventService(request);
        EventResult result = eventService.findEvent();

        // The result success should be false, and the result should store null values
        assertFalse(result.isSuccess());
        assertNull(result.toEvent());
    }

    /**
     * Negative test case which tests finding an event which does not belong to the same user as the authtoken.
     */
    @Test
    @DisplayName("Finding Other User's Event")
    void eventDoesNotBelongTest() {
        // Pull an authtoken for one user, and an event of another (see SampleTestData)
        Authtoken authtoken = testData.getAuthtokens().get(0);
        Event otherUsersEvent = testData.getEvents().get(2);

        // Insert the authtoken into the database for authentication
        try {
            authtokenDAO.insert(authtoken);

        } catch (DataAccessException d) {
            fail("Inserting authtoken threw: " + d.getMessage());
        }

        // Create the request
        EventRequest request = new EventRequest(otherUsersEvent.getEventID(), authtoken.getAuthtoken());

        // Create and run the service, and store the result
        EventService eventService = new EventService(request);
        EventResult result = eventService.findEvent();

        // The result success should be false and the result should store a specific error message
        assertFalse(result.isSuccess());
        assertEquals("Error: Requested event does not belong to this user.", result.getMessage());
    }

    /**
     * Negative test case which tests finding with an invalid authtoken.
     */
    @Test
    @DisplayName("Un-Authenticated User")
    void unAuthenticatedUserTest() {
        // Get an authtoken which we will not insert into the database, and pull an event to find
        Authtoken badAuthtoken = new Authtoken("thetimetraveller");
        Event eventToFind = testData.getEvents().get(0);

        // Create the request
        EventRequest request = new EventRequest(eventToFind.getEventID(), badAuthtoken.getAuthtoken());

        // Create and run the service, store the result
        EventService eventService = new EventService(request);
        EventResult result = eventService.findEvent();

        // The result success should be false and the result should store an "Invalid auth token" message
        assertFalse(result.isSuccess());
        assertEquals("Error: Invalid auth token.", result.getMessage());
    }
}
