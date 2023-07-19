package passoff.service;

import dao.UserDAO;
import exception.DataAccessException;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.SampleTestData;
import request.RegisterRequest;
import result.RegisterResult;
import service.RegisterService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class for the {@link RegisterService}.
 */
public class RegisterTest {
    /** Data Access Object for the User table. */
    private UserDAO userDAO;

    /** Sample data which will be used for testing. */
    private SampleTestData testData;

    /**
     * Sets up the testing environment.
     */
    @BeforeEach
    void setup() {
        // Initialize DAO and test data
        userDAO = new UserDAO();
        testData = new SampleTestData();

        // Clear the user table
        try {
            userDAO.clear();

        } catch (DataAccessException d) {
            fail("Clearing database threw: " + d.getMessage());
        }
    }

    /**
     * Cleans up the test environment after each test.
     */
    @AfterEach
    void cleanup() {
        try {
            // Clear the user table
            userDAO.clear();

        } catch (DataAccessException d) {
            fail("Clearing database threw: " + d.getMessage());
        }
    }

    /**
     * Positive test case which tests registering a single user.
     */
    @Test
    @DisplayName("Register Single User")
    void singleUserRegistrationTest() {
        // Pull a sample user from the sample data
        User sampleUser = testData.getUsers().get(0);

        // Create the request and service, and execute the registration
        RegisterRequest request = new RegisterRequest(sampleUser.getUsername(), sampleUser.getPassword(),
                sampleUser.getEmail(), sampleUser.getFirstName(), sampleUser.getLastName(), sampleUser.getGender());

        RegisterService service = new RegisterService(request);
        RegisterResult result = service.register();

        // The result success should be true and the authtoken, personID, and username should be not null
        assertTrue(result.isSuccess());
        assertNotNull(result.getAuthtoken());
        assertNotNull(result.getPersonID());
        assertNotNull(result.getUsername());
    }

    /**
     * Negative test case which tests a user putting a blank password in.
     */
    @Test
    @DisplayName("Blank Password Testing")
    void wrongPasswordTesting() {
        // Pull a sample user from the sample data
        User sampleUser = testData.getUsers().get(0);

        // Create the request and service, and execute the registration
        RegisterRequest request = new RegisterRequest(sampleUser.getUsername(), "",
                sampleUser.getEmail(), sampleUser.getFirstName(), sampleUser.getLastName(), sampleUser.getGender());
        RegisterService service = new RegisterService(request);

        RegisterResult result = service.register();

        // The result success should be false, and all result fields should be null
        assertFalse(result.isSuccess());
        assertNull(result.getAuthtoken());
        assertNull(result.getPersonID());
        assertNull(result.getUsername());
    }

    /**
     * Negative test case which tests for duplicate registration.
     */
    @Test
    @DisplayName("Username Already Taken Testing")
    void usernameAlreadyTakenTesting() {
        // Pull a sample user from the sample data
        User sampleUser = testData.getUsers().get(0);

        // Insert the user into the database
        try {
            userDAO.insert(sampleUser);
        } catch (DataAccessException d) {
            fail("Inserting user threw: " + d.getMessage());
        }

        // Create the request and service, and run the registration
        RegisterRequest request = new RegisterRequest(sampleUser.getUsername(), sampleUser.getPassword(),
                sampleUser.getEmail(), sampleUser.getFirstName(), sampleUser.getLastName(), sampleUser.getGender());
        RegisterService service = new RegisterService(request);

        RegisterResult result = service.register();

        // The result success should be false, and the result message should indicate that the username is taken.
        assertFalse(result.isSuccess());
        assertEquals("Error: Username already taken.", result.getMessage());
    }
}
