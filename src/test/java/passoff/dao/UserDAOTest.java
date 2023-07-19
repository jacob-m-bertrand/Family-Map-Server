package passoff.dao;

import dao.UserDAO;
import dao.tools.TableTools;
import exception.DataAccessException;
import exception.NotFoundException;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.SampleTestData;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class for the {@link UserDAO}.
 */
public class UserDAOTest {
    /** The Data Access Object being tested. */
    private static UserDAO dao;

    /** A list of sample users that can be used for testing
     * @see SampleTestData
     * */
    private static ArrayList<User> users;

    /**
     * Sets up the testing environment.
     */
    @BeforeAll
    @DisplayName("Initializing DAO with sample data")
    static void setup() {
        // Initialize the array of Users we can use throughout the tests
        SampleTestData testData = new SampleTestData();
        users = testData.getUsers();

        // Initialize the dao object
        try {
            dao = new UserDAO();

            // Clear any existing data, so we can have a clean run
            dao.clear();

        } catch (Exception e) {
            fail("Database connection failed!");
        }
    }

    /**
     * Cleans up the testing environment between runs by clearing the User table.
     */
    @AfterEach
    void cleanup() {
        try {
            dao.clear();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Positive test case testing insertion of a single user into the table
     */
    @Test
    @DisplayName("Inserting Single User")
    void insertSingleTest() {
        // Get a single User from the Array
        User singleUser = users.get(1);
        User found = null;

        try {
            // Insert the User, store the results of trying to find it in the table
            dao.insert(singleUser);
            found = dao.find(singleUser.getUsername());

        } catch (Exception e) {
            fail("Inserting single row threw: " + e.getMessage());
        }

        // Make sure that it could actually find the User and instantiate it properly
        assertNotNull(found);

        // Check that the objects are equal
        assertEquals(found, singleUser);
    }

    /**
     * Positive test case testing the insertion of multiple users into the table at once.
     */
    @Test
    @DisplayName("Bulk Insertion")
    void insertBulkTest() {
        ArrayList<User> findResults = new ArrayList<User>();

        try {
            // Insert all the Users in the Users array
            for (User e : users) {
                dao.insert(e);
            }

            // Find each user by their id
            for (User e : users) {
                String idToFind = e.getUsername();
                findResults.add(dao.find(idToFind));
            }
        } catch (Exception e) {
            fail("Bulk insertion row threw: " + e.getMessage());
        }

        // Make sure none of the Users are null
        for (User e : findResults) {
            assertNotNull(e);
        }

        // The original array should equal the results array if all of them were added and found
        assertEquals(users, findResults);
    }

    /**
     * Negative test case which tests how the DAO handles attempted insertion of a user twice.
     */
    @Test
    @DisplayName("Duplicate Insertion Testing")
    void duplicateInsertionTest() {
        // Get a single user from the sample data
        User singleUser = users.get(1);

        try {
            dao.insert(singleUser);

        } catch (Exception e) {
            fail("Inserting single row threw: " + e.getMessage());
        }

        // This should produce an error, since the User was already added before and the table rows must be unique
        assertThrows(DataAccessException.class, () -> dao.insert(singleUser));
    }

    /**
     * Positive test case which tests finding a single user in the table.
     */
    @Test
    @DisplayName("Find Testing")
    void findTest() {
        // Get a sample user
        User toFind = users.get(3);

        try {
            // Insert all sample users into the table
            for (User e : users) {
                dao.insert(e);
            }

            // Find the user we pulled
            User returned = dao.find(toFind.getUsername());
            assertEquals(toFind, returned);

        } catch (Exception e) {
            fail("Inserting single row threw: " + e.getMessage());
        }
    }

    /**
     * Negative test case which tests finding a user who is not in the table.
     */
    @Test
    @DisplayName("Finding Non-Existent User")
    void doesNotExistTest() {
        // This User should not exist, and the DAO should throw when attempting to find it in the database
        User toFind = new User("username", "password", "email", "firstName", "lastName", "gender");

        try {
            // Insert all the sample users into the table (but not the one we just made)
            for (User e : users) {
                dao.insert(e);
            }

            // The find should fail by throwing a NotFoundException
            assertThrows(NotFoundException.class, () -> dao.find(toFind.getUsername()));

        } catch (Exception e) {
            fail("Finding non-existent User threw: " + e.getMessage());
        }
    }


    /**
     * Positive test case testing the clear functionality of the DAO.
     */
    @Test
    @DisplayName("Clear Testing")
    void clearTesting() {
        try {
            // Bulk insert items, then clear, and make sure we have an empty table
            for (User e : users) {
                dao.insert(e);
            }

            dao.clear();
            assertTrue(TableTools.tableIsEmpty("User"));
        } catch (Exception e) {
            fail("Clearing table threw: " + e.getMessage());
        }
    }
}
