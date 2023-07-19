package passoff.service;

import dao.AuthtokenDAO;
import dao.PersonDAO;
import exception.DataAccessException;
import model.Authtoken;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.SampleTestData;
import request.AuthenticatedRequest;
import result.ListPersonsResult;
import service.ListPersonsService;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class for the {@link ListPersonsService}.
 */
public class ListPersonsTest {
    /** Data Access Object for the Person table. */
    private PersonDAO personDAO;

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
        personDAO = new PersonDAO();
        authtokenDAO = new AuthtokenDAO();

        // Initialize test data
        testData = new SampleTestData();

        // Each test will rely on having data in the table, so load it with sample people
        try {
            for (Person e : testData.getPeople()) {
                personDAO.insert(e);
            }
        } catch (DataAccessException d) {
            fail("Populating person table threw: " + d.getMessage());
        }
    }

    /**
     * Cleans up the testing environment after each test.
     */
    @AfterEach
    void cleanup() {
        try {
            personDAO.clear();
            authtokenDAO.clear();
        }
        catch(DataAccessException d) {
            fail("Clearing person table threw: " + d.getMessage());
        }
    }

    /**
     * Positive test case which tests simple listing of a specific user's associated people.
     */
    @Test
    @DisplayName("Listing User Persons")
    void listUserPersonsTest() {
        // Pull a username from the testData
        String username = testData.getUsers().get(0).getUsername();

        // Initialize an array to store all the people found for the user
        ArrayList<Person> userPersons = new ArrayList<Person>();

        // Get all the people associated with the specified user
        for (Person e : testData.getPeople()) {
            if (Objects.equals(e.getAssociatedUsername(), username)) {
                userPersons.add(e);
            }
        }

        // Get a new authtoken and insert it into the database, so we can authenticate
        Authtoken authtoken = new Authtoken(username);

        try {
            authtokenDAO.insert(authtoken);
        }
        catch(DataAccessException d) {
            fail("Inserting authtoken threw: " + d.getMessage());
        }

        // Create the request and service
        AuthenticatedRequest request = new AuthenticatedRequest(authtoken.getAuthtoken());
        ListPersonsService listPersonsService = new ListPersonsService(request);

        // List the people, store the result
        ListPersonsResult result = listPersonsService.listPersons();

        /* The result success should be true, and the previously built userPersons list should match the found list in
           the result */
        assertTrue(result.isSuccess());
        assertEquals(userPersons, result.getData());
    }

    /**
     * Negative test case which tests listing the people of a user who is not in the table.
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
        ListPersonsService listPersonsService = new ListPersonsService(request);

        // Execute the listing
        ListPersonsResult result = listPersonsService.listPersons();

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

        // Create the request with the authtoken (which hasn't been put into the database)
        AuthenticatedRequest request = new AuthenticatedRequest(authtoken.getAuthtoken());

        // Create and run the service
        ListPersonsService listPersonsService = new ListPersonsService(request);
        ListPersonsResult result = listPersonsService.listPersons();

        // The result success should be false, and the result message should say the authtoken is invalid
        assertFalse(result.isSuccess());
        assertEquals("Error: Invalid auth token.", result.getMessage());
        assertNull(result.getData());
    }
}

