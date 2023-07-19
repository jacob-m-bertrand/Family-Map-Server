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
import request.PersonRequest;
import result.PersonResult;
import service.PersonService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class for the {@link PersonService}.
 */
public class PersonTest {
    /** The Data Access Object for the People table. */
    private PersonDAO personDAO;

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
        personDAO = new PersonDAO();
        authtokenDAO = new AuthtokenDAO();

        // Initialize sample data
        testData = new SampleTestData();

        // Each test relies on the Person table having data, so insert sample data into the table before each test
        try {
            for (Person e : testData.getPeople()) {
                personDAO.insert(e);
            }
        } catch (DataAccessException d) {
            fail("Inserting person threw: " + d.getMessage());
        }
    }

    /**
     * Cleans up the testing environment after each test by clearing the table each time.
     */
    @AfterEach
    void cleanup() {
        try {
            personDAO.clear();
            authtokenDAO.clear();

        } catch (Exception e) {
            fail("Clearing Persons threw: " + e.getMessage());
        }
    }

    /**
     * Positive test case which tests finding a single person.
     */
    @Test
    @DisplayName("Finding Person")
    void personFindTest() {
        // Get an authtoken, so we can make the request, then pull a person to find
        Authtoken authtoken = testData.getAuthtokens().get(0);
        Person personToFind = testData.getPeople().get(0);

        // Insert our authtoken into the database so we can authenticate
        try {
            authtokenDAO.insert(authtoken);

        } catch (DataAccessException d) {
            fail("Inserting authtoken threw: " + d.getMessage());
        }

        // Create our request
        PersonRequest request = new PersonRequest(personToFind.getPersonID(), authtoken.getAuthtoken());

        // Create and run the service, then store the result
        PersonService personService = new PersonService(request);
        PersonResult result = personService.findPerson();

        // The result success should be true, and the found people should match the one we pulled
        assertTrue(result.isSuccess());
        assertEquals(personToFind, result.toPerson());
    }

    /**
     * Negative test case which tests finding person which is not in the table
     */
    @Test
    @DisplayName("Finding Non-Existent Person")
    void personDoesNotExistTest() {
        // Pull an authtoken, so we can authenticate, and create a personwhich isn't in the table
        Authtoken authtoken = testData.getAuthtokens().get(0);
        Person personNotInTable = new Person("jmbertrand", "Time", "Traveller", "m");

        // Insert the authtoken into the database for authentication
        try {
            authtokenDAO.insert(authtoken);

        } catch (DataAccessException d) {
            fail("Inserting authtoken threw: " + d.getMessage());
        }

        // Create the request
        PersonRequest request = new PersonRequest(personNotInTable.getPersonID(), authtoken.getAuthtoken());

        // Create and run the service, and store the result
        PersonService personService = new PersonService(request);
        PersonResult result = personService.findPerson();

        // The result success should be false, and the result should store null values
        assertFalse(result.isSuccess());
        assertNull(result.toPerson());
    }

    /**
     * Negative test case which tests finding a person which does not belong to the same user as the authtoken.
     */
    @Test
    @DisplayName("Finding Other User's Person")
    void personDoesNotBelongTest() {
        // Pull an authtoken for one user, and a person of another (see SampleTestData)
        Authtoken authtoken = testData.getAuthtokens().get(0);
        Person otherUsersPerson = testData.getPeople().get(5);

        // Insert the authtoken into the database for authentication
        try {
            authtokenDAO.insert(authtoken);

        } catch (DataAccessException d) {
            fail("Inserting authtoken threw: " + d.getMessage());
        }

        // Create the request
        PersonRequest request = new PersonRequest(otherUsersPerson.getPersonID(), authtoken.getAuthtoken());

        // Create and run the service, and store the result
        PersonService personService = new PersonService(request);
        PersonResult result = personService.findPerson();

        // The result success should be false and the result should store a specific error message
        assertFalse(result.isSuccess());
        assertEquals("Error: Requested person does not belong to this user.", result.getMessage());
    }

    /**
     * Negative test case which tests finding with an invalid authtoken.
     */
    @Test
    @DisplayName("Un-Authenticated User")
    void unAuthenticatedUserTest() {
        // Get an authtoken which we will not insert into the database, and pull a person to find
        Authtoken badAuthtoken = new Authtoken("thetimetraveller");
        Person personToFind = testData.getPeople().get(0);

        // Create the request
        PersonRequest request = new PersonRequest(personToFind.getPersonID(), badAuthtoken.getAuthtoken());

        // Create and run the service, store the result
        PersonService personService = new PersonService(request);
        PersonResult result = personService.findPerson();

        // The result success should be false and the result should store an "Invalid auth token" message
        assertFalse(result.isSuccess());
        assertEquals("Error: Invalid auth token.", result.getMessage());
    }
}
