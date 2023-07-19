package passoff.service;

import dao.UserDAO;
import exception.DataAccessException;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.SampleTestData;
import request.LoginRequest;
import result.LoginResult;
import service.LoginService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class for {@link LoginService}.
 */
public class LoginTest {
    /** Data Access Object for the User table. */
    private UserDAO userDAO;

    /** Sample data which is used in testing. */
    private SampleTestData testData;

    /**
     * Sets up the testing environment.
     */
    @BeforeEach
    void setup() {
        // Initialize the DAO and sample data
        userDAO = new UserDAO();
        testData = new SampleTestData();

        // Clear and reload the user table with new data
        try {
            userDAO.clear();

            for(User u : testData.getUsers()) {
                userDAO.insert(u);
            }
        } catch (DataAccessException d) {
            fail("Clearing database threw: " + d.getMessage());
        }
    }

    /**
     * Cleans up the testing environment after every test.
     */
    @AfterEach
    void cleanup() {
        try {
            userDAO.clear();

        } catch (DataAccessException d) {
            fail("Clearing database threw: " + d.getMessage());
        }
    }

    /**
     * Positive test case which tests a valid login.
     */
    @Test
    @DisplayName("Login Testing")
    void loginTesting() {
        // Pull a user from the sample data
        User sampleUser = testData.getUsers().get(0);

        // Create the request and service, and execute the login
        LoginRequest request = new LoginRequest(sampleUser.getUsername(), sampleUser.getPassword());
        LoginService service = new LoginService(request);
        LoginResult result = service.login();

        // The result success should be true and none of its attributes should be null
        assertTrue(result.isSuccess());
        assertNotNull(result.getAuthtoken());
        assertNotNull(result.getPersonID());
        assertNotNull(result.getUsername());
    }

    /**
     * Negative test case which tests the result when the wrong password is used.
     */
    @Test
    @DisplayName("Wrong Password Testing")
    void wrongPasswordTesting() {
        // Pull a user from the sample data
        User sampleUser = testData.getUsers().get(0);

        // Create the request and service, and execute the login
        LoginRequest request = new LoginRequest(sampleUser.getUsername(), "thisisthewrongpassword");
        LoginService service = new LoginService(request);
        LoginResult result = service.login();

        // The result success should be false, and the message should be that the password is incorrect
        assertFalse(result.isSuccess());
        assertEquals("Error: Invalid request (password is incorrect).", result.getMessage());
    }

    /**
     * Negative test case which tests logging in with the wrong username
     */
    @Test
    @DisplayName("Non-Existent Username Testing")
    void usernameDoesNotExistTest() {
        // Pull a user from the sample data
        User sampleUser = testData.getUsers().get(0);

        // Create the request and service, and execute the login
        LoginRequest request = new LoginRequest("thisisthewrongusername", sampleUser.getPassword());
        LoginService service = new LoginService(request);
        LoginResult result = service.login();

        // The result success should be false, and the result fields should be null
        assertFalse(result.isSuccess());
        assertNull(result.getAuthtoken());
        assertNull(result.getPersonID());
        assertNull(result.getUsername());
    }
}
