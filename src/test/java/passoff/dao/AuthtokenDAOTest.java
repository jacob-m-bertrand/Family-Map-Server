package passoff.dao;

import dao.AuthtokenDAO;
import dao.tools.TableTools;
import exception.DataAccessException;
import exception.NotFoundException;
import model.Authtoken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.SampleTestData;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class for the {@link AuthtokenDAO}.
 */
public class AuthtokenDAOTest {
    /** The Data Access Object being tested. */
    private static AuthtokenDAO dao;

    /** A list of sample authtokens that can be used for testing
     * @see SampleTestData
     * */
    private static ArrayList<Authtoken> authtokens;

    /**
     * Sets up the testing environment.
     */
    @BeforeAll
    static void setup() {
        // Initialize the array of authtokens we can use throughout the tests
        SampleTestData testData = new SampleTestData();
        authtokens = testData.getAuthtokens();

        // Initialize the dao object
        try {
            dao = new AuthtokenDAO();

            // Clear any existing data, so we can have a clean run
            dao.clear();

        } catch (Exception e) {
            fail("Database connection failed!");
        }
    }

    /**
     * Cleans up the testing environment between runs by clearing the Authtoken table.
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
     * Positive test case testing insertion of a single item into the Authtoken table.
     */
    @Test
    @DisplayName("Inserting Single Authtoken")
    void insertSingleTest() {
        // Get a single Authtoken from the Array
        Authtoken singleAuthtoken = authtokens.get(1);
        String found = null;

        try {
            // Insert the Authtoken, store the results of trying to find it in the table
            dao.insert(singleAuthtoken);
            found = dao.find(singleAuthtoken.getAuthtoken());

        } catch (Exception e) {
            fail("Inserting single row threw: " + e.getMessage());
        }

        // Make sure that it could actually find the Authtoken and instantiate it properly
        assertNotNull(found);

        // Check that the objects are equal
        assertEquals(found, singleAuthtoken.getUsername());
    }


    /**
     * Negative test case which tests how the DAO handles attempted insertion of an authtoken twice.
     */
    @Test
    @DisplayName("Duplicate Insertion Testing")
     void duplicateInsertionTest() {
        Authtoken singleAuthtoken = authtokens.get(1);

        try {
            dao.insert(singleAuthtoken);

        } catch (Exception e) {
            fail("Inserting single row threw: " + e.getMessage());
        }

        // This should produce an error, since the Authtoken was already added before and the table rows must be unique
        assertThrows(DataAccessException.class, () -> dao.insert(singleAuthtoken));
    }


    /**
     * Positive test case testing the finding of a single authtoken in the table.
     */
    @Test
    @DisplayName("Find Testing")
    void findTest() {
        // Get an authtoken from the array
        Authtoken toFind = authtokens.get(3);

        try {
            // Insert all authtokens into the table
            for (Authtoken e : authtokens) {
                dao.insert(e);
            }

            // Try to find the authtoken in the table
            String returned = dao.find(toFind.getAuthtoken());

            // Make sure the DAO found the right one
            assertEquals(toFind.getUsername(), returned);

        }  catch (Exception e) {
            fail("Inserting single row threw: " + e.getMessage());
        }
    }

    /**
     * Negative test case testing the finding of an authtoken not in the table.
     */
    @Test
    @DisplayName("Finding Non-Existent Authtoken")
    void doesNotExistTest() {
        // This Authtoken should not exist, and the DAO should throw when attempting to find it in the database
        Authtoken toFind = new Authtoken("DoesNotExist");

        try {
            // Insert all the authtokens from the array into the table (does not include the authtoken we just made)
            for (Authtoken e : authtokens) {
                dao.insert(e);
            }

            // The find function should throw a NotFoundException
            assertThrows(NotFoundException.class, () -> dao.find(toFind.getAuthtoken()));

        } catch (Exception e) {
            fail("Finding non-existent Authtoken threw: " + e.getMessage());
        }
    }


    /**
     * Positive test case testing the clear functionality.
     */
    @Test
    @DisplayName("Clear Testing")
    void clearTesting() {
        try {
            // Bulk insert items, then clear, and make sure we have an empty table
            for (Authtoken e : authtokens) {
                dao.insert(e);
            }

            dao.clear();
            assertTrue(TableTools.tableIsEmpty("Authtoken"));

        } catch (Exception e) {
            fail("Clearing table threw: " + e.getMessage());
        }
    }
}
