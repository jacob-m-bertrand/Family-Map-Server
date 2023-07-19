package dao;

import dao.tools.SQLTools;
import dao.tools.TableTools;
import exception.DataAccessException;
import exception.NotFoundException;
import model.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interfaces between the server and the Person table.
 */
public class PersonDAO {
    /**
     * Inserts a new person into the Person table.
     * @param person Person object to insert into the Person table.
     */
    public void insert(Person person) throws DataAccessException {
        // Build sql statement using provided person information
        String insertSql = String.format("INSERT INTO Person VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                person.getPersonID(), person.getAssociatedUsername(), person.getFirstName(), person.getLastName(),
                person.getGender(), person.getFatherID(), person.getMotherID(), person.getSpouseID());

        // Execute sql, store number of rows added
        int rowsAdded = SQLTools.executeInsert(insertSql);

        // If no rows were added, throw an exception
        if (rowsAdded == 0) {
            throw new DataAccessException("No rows were added.");
        }

        // Commit the changes and cleanup
        Database.commit();
    }

    /**
     * Finds a Person in the table using the provided personID.
     * @param personID              The personID of the requested person.
     * @return                      The requested {@link Person} object.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     * @throws NotFoundException    If there is no person with the provided personID.
     */
    public Person find(String personID) throws DataAccessException, NotFoundException {

        String findSql = String.format("SELECT * FROM Person WHERE personID = '%s'", personID);

        // Store the results of the query, then close
        ResultSet findResult = SQLTools.executeQuery(findSql);

        try {
            if (!findResult.next()) {
                throw new NotFoundException();
            }
        } catch (SQLException s) {
            throw new DataAccessException(s.getMessage());
        }

        return createPersonObject(findResult);
    }

    /**
     * Clears the Person table.
     * @throws DataAccessException If there is an issue accessing the database or table.
     */
    public void clear() throws DataAccessException {
        TableTools.clear("Person");
    }

    /**
     * Removes all people in the table associated with a certain user.
     * @param username              The username of the user.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    public void removeByUsername(String username) throws DataAccessException {
        // Build the sql statement, then execute the statement
        String sql = String.format("DELETE FROM Person WHERE associatedUsername = '%s'", username);
        SQLTools.executeStatement(sql);
    }

    /**
     * Lists all people associated with a certain user.
     * @param username              The username of the user for whom we are retrieving people.
     * @return                      An {@link ArrayList} of {@link Person} objects.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     * @throws NotFoundException    If there is no event with the provided associatedUsername.
     */
    public ArrayList<Person> listUserPersons(String username) throws DataAccessException, NotFoundException {
        // Build the sql statement, execute, and store the results
        String sql = String.format("SELECT * FROM Person WHERE associatedUsername = '%s'", username);
        ResultSet results = SQLTools.executeQuery(sql);

        // Initialize the ArrayList that will be returned
        ArrayList<Person> toReturn = new ArrayList<Person>();

        try {
            // Add all the results to the ArrayList
            while (results.next()) toReturn.add(createPersonObject(results));

            // If there were no rows found, throw a not found exception
            if (toReturn.isEmpty()) throw new NotFoundException();

            return toReturn;

        } catch (SQLException e) {
            // Caused by a bad SQL statement
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Helper function which turns jdbc ResultSets into Person objects.
     * @param result                The ResultSet generated from a query.
     * @return                      A person with the data from a row in the table.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    private Person createPersonObject(ResultSet result) throws DataAccessException {
        try {
            // Get all person information from the result
            String personID = result.getString(1);
            String associatedUsername = result.getString(2);
            String firstName = result.getString(3);
            String lastName = result.getString(4);
            String gender = result.getString(5);
            String fatherID = result.getString(6);
            String motherID = result.getString(7);
            String spouseID = result.getString(8);

            // If any of the father/mother/spouse IDs are null, set them to actual null for proper encoding later
            if(fatherID.equals("null")) fatherID = null;
            if(motherID.equals("null")) motherID = null;
            if(spouseID.equals("null")) spouseID = null;

            return new Person(personID, associatedUsername, firstName, lastName, gender, fatherID, motherID, spouseID);

        } catch (SQLException e) {
            // Caused by a mismatch of requested and actual types
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Sets a person's motherID in the database.
     * @param personID              The person to update.
     * @param motherID              The motherID to add.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    public void setMotherId(String personID, String motherID) throws DataAccessException {
        String sql = String.format("UPDATE Person SET motherID = '%s' WHERE personId = '%s'", motherID, personID);

        SQLTools.executeStatement(sql);
    }

    /**
     * Sets a person's fatherID in the database.
     * @param personID              The person to update.
     * @param fatherID              The fatherID to add.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    public void setFatherId(String personID, String fatherID) throws DataAccessException {
        String sql = String.format("UPDATE Person SET fatherID = '%s' WHERE personId = '%s'", fatherID, personID);

        SQLTools.executeStatement(sql);
    }

    /**
     * Sets a person's spouseID in the database.
     * @param personID              The person to update.
     * @param spouseID              The spouseID to add.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    public void setSpouseId(String personID, String spouseID) throws DataAccessException {
        String sql = String.format("UPDATE Person SET spouseID = '%s' WHERE personID = '%s'", spouseID, personID);

        SQLTools.executeStatement(sql);
    }
}
