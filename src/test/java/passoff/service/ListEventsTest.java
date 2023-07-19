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
import request.AuthenticatedRequest;
import result.ListEventsResult;
import service.ListEventsService;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class for the {@link ListEventsService}.
 */
public class ListEventsTest {
    /** Data Access Object for the Event table. */
    private EventDAO eventDAO;

    /** Data Access Object for the Authtoken table. */
    private AuthtokenDAO authtokenDAO;

    /** Sample data used for testing. See {@link SampleTestData}. */
    private SampleTestData testData;

    /**
     * Sets up the testing environment.
     */
    @BeforeEach
    void setup() {
        // Initialize DAOs
        eventDAO = new EventDAO();
        authtokenDAO = new AuthtokenDAO();

        // Initialize test data
        testData = new SampleTestData();

        // Each test will rely on having data in the table, so load it with sample events
        try {
            for (Event e : testData.getEvents()) {
                eventDAO.insert(e);
            }
        } catch (DataAccessException d) {
            fail("Populating event table threw: " + d.getMessage());
        }
    }

    /**
     * Cleans up the testing environment after each test.
     */
    @AfterEach
    void cleanup() {
        try {
            eventDAO.clear();
            authtokenDAO.clear();
        }
        catch(DataAccessException d) {
            fail("Clearing event table threw: " + d.getMessage());
        }
    }

    /**
     * Positive test case which tests simple listing of a specific user's events.
     */
    @Test
    @DisplayName("Listing User Events")
    void listUserEventsTest() {
        // Pull a username from the testData
        String username = testData.getUsers().get(0).getUsername();

        // Initialize an array to store all the events found for the user
        ArrayList<Event> userEvents = new ArrayList<Event>();

        // Get all the events associated with the specified user
        for (Event e : testData.getEvents()) {
            if (Objects.equals(e.getAssociatedUsername(), username)) {
                userEvents.add(e);
            }
        }

        // Get a new authtoken and insert it into the database so we can authenticate
        Authtoken authtoken = new Authtoken(username);

        try {
            authtokenDAO.insert(authtoken);
        }
        catch(DataAccessException d) {
            fail("Inserting authtoken threw: " + d.getMessage());
        }

        // Create the request and service
        AuthenticatedRequest request = new AuthenticatedRequest(authtoken.getAuthtoken());
        ListEventsService listEventsService = new ListEventsService(request);

        // List the events, store the result
        ListEventsResult result = listEventsService.listEvents();

        /* The result success should be true, and the previously built userEvents list should match the found list in
           the result */
        assertTrue(result.isSuccess());
        assertEquals(userEvents, result.getData());
    }

    /**
     * Negative test case which tests listing the events of a user who is not in the table.
     */
    @Test
    @DisplayName("Non-existent User")
    void nonExistentUserTest() {
        // Get an authtoken for the non-existent user
        String username = "thetimetraveller";
        Authtoken authtoken = new Authtoken(username);

        // Insert the authtoken so we can authenticate
        try {
            authtokenDAO.insert(authtoken);
        }
        catch(DataAccessException d) {
            fail("Inserting authtoken threw: " + d.getMessage());
        }

        // Create the request and the service
        AuthenticatedRequest request = new AuthenticatedRequest(authtoken.getAuthtoken());
        ListEventsService listEventsService = new ListEventsService(request);

        // Execute the listing
        ListEventsResult result = listEventsService.listEvents();

        // The result success should be false, and the result should store 'null' for the list
        assertFalse(result.isSuccess());
        assertNull(result.getData());
    }

    /**
     * Negative test case which tests a user requesting a list without an authtoken.
     */
    @Test
    @DisplayName("Unauthenticated User")
    void unauthenticatedUserTest() {
        // The username of the un-authenticated user
        String username = "thetimetraveller";
        Authtoken authtoken = new Authtoken(username);

        // Create the request with the authtoken (which hasnt been put into the database)
        AuthenticatedRequest request = new AuthenticatedRequest(authtoken.getAuthtoken());

        // Create and run the service
        ListEventsService listEventsService = new ListEventsService(request);
        ListEventsResult result = listEventsService.listEvents();

        // The result success should be false, and the result message should say the authtoken is invalid
        assertFalse(result.isSuccess());
        assertEquals("Error: Invalid auth token.", result.getMessage());
        assertNull(result.getData());
    }
}

