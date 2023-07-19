package passoff.dao;

import dao.PersonDAO;
import dao.tools.TableTools;
import exception.DataAccessException;
import exception.NotFoundException;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.SampleTestData;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class for the {@link PersonDAO}.
 */
public class PersonDAOTest {
    /** The Data Access Object being tested. */
    private static PersonDAO dao;

    /** A list of sample people which can be used for testing
     * @see SampleTestData
     * */
    private static ArrayList<Person> persons;

    /**
     * Sets up the testing environment.
     */
    @BeforeAll
    static void setup() {
        // Initialize the array of people we can use throughout the tests
        SampleTestData testData = new SampleTestData();
        persons = testData.getPeople();

        // Initialize the dao object
        try {
            dao = new PersonDAO();
            dao.clear(); // Clear any existing data, so we can have a clean run

        } catch (Exception e) {
            fail("Database connection failed!");
        }
    }

    /**
     * Cleans up the testing environment between runs by clearing the Person table.
     */
    @AfterEach
    void cleanup() {
        try {
            dao.clear();
        }
        catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Positive test case testing insertion of a single person into the table.
     */
    @Test
    @DisplayName("Inserting Single Person")
    void insertSingleTest() {
        // Get a single person from the Array
        Person singlePerson = persons.get(1);
        Person found = null;

        try {
            // Insert the person, store the results of trying to find it in the table
            dao.insert(singlePerson);
            found = dao.find(singlePerson.getPersonID());

        } catch (Exception e) {
            fail("Inserting single row threw: " + e.getMessage());
        }

        // Make sure that it could actually find the person and instantiate it properly
        assertNotNull(found);

        // Check that the objects are equal
        assertEquals(found, singlePerson);
    }

    /**
     * Positive test case testing insertion of multiple people into the table.
     */
    @Test
    @DisplayName("Bulk Insertion")
    void insertBulkTest() {
        // Create a new Array for people which we will find
        ArrayList<Person> findResults = new ArrayList<Person>();

        try {
            // Insert all sample people into the table
            for (Person e : persons) {
                dao.insert(e);
            }

            // Find all people by their person id
            for (Person e : persons) {
                String idToFind = e.getPersonID();
                findResults.add(dao.find(idToFind));
            }
        } catch (Exception e) {
            fail("Bulk insertion row threw: " + e.getMessage());
        }

        // Make sure no people are null
        for (Person e : findResults) {
            assertNotNull(e);
        }

        // The original array should equal the results array if all of them were added and found
        assertEquals(persons, findResults);
    }

    /**
     * Negative test case testing a person being inserted twice.
     */
    @Test
    @DisplayName("Duplicate Insertion Testing")
    void duplicateInsertionTest() {
        // Get a person from the array
        Person singlePerson = persons.get(1);

        try {
            dao.insert(singlePerson);

        } catch (Exception e) {
            fail("Inserting single row threw: " + e.getMessage());
        }

        // This should produce an error, since the person was already added before and the table rows must be unique
        assertThrows(DataAccessException.class, () -> dao.insert(singlePerson));
    }

    /**
     * Positive test case testing finding one person in the table.
     */
    @Test
    @DisplayName("Find Testing")
    void findTest() {
        // Get a person from the array
        Person toFind = persons.get(3);

        try {
            // Insert all the sample people into the table
            for (Person e : persons) {
                dao.insert(e);
            }

            // Find the person by their ID
            Person returned = dao.find(toFind.getPersonID());
            assertEquals(toFind, returned);

        } catch (Exception e) {
            fail("Inserting single row threw: " + e.getMessage());
        }
    }

    /**
     * Negative test case testing the finding of a person which is not in the table.
     */
    @Test
    @DisplayName("Finding Non-Existent Person")
    void doesNotExistTest() {
        // This person should not be in the table
        Person toFind = new Person("jmbertrand", "Time", "Traveller", "m");

        try {
            // Insert all sample people into the table (does not include toFind)
            for (Person e : persons) {
                dao.insert(e);
            }

            // Attempting to find the non-existent person should throw a NotFoundException
            assertThrows(NotFoundException.class, () -> dao.find(toFind.getPersonID()));

        } catch (Exception e) {
            fail("Finding non-existent person threw: " + e.getMessage());
        }
    }

    /**
     * Positive test case which tests listing a user's person.
     */
    @Test
    @DisplayName("List By Username")
    void listByUsernameTest() {
        // This array will store the person associated with one user
        ArrayList<Person> bestPersons = new ArrayList<Person>();

        try {
            // Insert all sample people into the table. Add the people associated with jmbertrand to bestPerson
            for(Person e : persons) {
                dao.insert(e);

                if (e.getAssociatedUsername().equals("jmbertrand")) {
                    bestPersons.add(e);
                }
            }

            // bestPerson and the array created by the list function should be equal
            assertEquals(bestPersons, dao.listUserPersons("jmbertrand"));

        } catch (Exception e) {
            fail("Listing by username threw: " + e.getMessage());
        }
    }

    /**
     * Negative test case which tests listing of a user which is not in the database.
     */
    @Test
    @DisplayName("List Not Found")
    void doesNotExistListTest(){
        try {
            // Insert all sample people into the table
            for (Person e : persons) {
                dao.insert(e);
            }

            // Time traveler shouldn't be in the database, make sure the list function throws a NotFoundException
            assertThrows(NotFoundException.class, () -> dao.listUserPersons("Time Traveler"));

        } catch (Exception e) {
            fail("Listing non-existent username threw: " + e.getMessage());
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
            for (Person e : persons) {
                dao.insert(e);
            }

            dao.clear();
            assertTrue(TableTools.tableIsEmpty("Person"));

        } catch (Exception e) {
            fail("Clearing table threw: " + e.getMessage());
        }

    }
}
