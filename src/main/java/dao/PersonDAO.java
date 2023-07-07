package dao;

import model.Event;
import model.Person;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;


public class PersonDAO {
    private final Connection conn;

    /**
     * Constructs the PersonDAO Object
     * @param conn Server connection
     */
    public PersonDAO(Connection conn) {
        this.conn = conn;
    }


    /**
     * Inserts an event into the Events table
     * @param person Person object to insert into the Events table
     */
    public void insert(Person person) throws DataAccessException {
        String insertSql = String.format("INSERT INTO Person VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                person.getPersonID(), person.getAssociatedUsername(), person.getFirstName(), person.getLastName(), person.getGender(),
                person.getFatherID(), person.getMotherID(), person.getSpouseID());

        int rowsAdded = SQLTools.executeInsert(insertSql);

        // If no rows were added, throw an exception
        if(rowsAdded == 0) throw new DataAccessException("No rows were added.");

        // Commit the changes and cleanup
        Database.commit();
    }

    public Person find(String personID) throws DataAccessException, NotFoundException {
        // If the event doesn't exist in the table, throw an exception
        if(ValueTools.exists("Person", "personID", personID)) throw new NotFoundException();

        String findSql = String.format("SELECT * FROM Person WHERE eventID = '%s'", personID);

        // Store the results of the query, then close
        ResultSet findResult = SQLTools.executeQuery(findSql);

        return createPersonObject(findResult);
    }

    /**
     * Clears the table
     * @throws DataAccessException in the case that the table does not exist
     */
    public void clear() throws DataAccessException { TableTools.clear("Person"); }


    public ArrayList<Person> list(String attribute, String value) throws DataAccessException, NotFoundException {
        // Ensure the attribute and value exist before moving on
        if (ValueTools.exists("Person", attribute, value)) throw new NotFoundException();

        String sql = String.format("SELECT * FROM Person WHERE %s = '%s'", attribute, value);
        ResultSet results = SQLTools.executeQuery(sql);
        ArrayList<Person> toReturn = new ArrayList<Person>();

        try {
            while (results.next()) toReturn.add(createPersonObject(results));
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

        return toReturn;
    }

    private Person createPersonObject(ResultSet result) throws DataAccessException {

        try {
            String personID = result.getString(1);
            String associatedUsername = result.getString(2);
            String firstName = result.getString(3);
            String lastName = result.getString(4);
            String gender = result.getString(5);
            String fatherID = result.getString(6);
            String motherID = result.getString(7);
            String spouseID = result.getString(8);

            return new Person(personID, associatedUsername, firstName, lastName, gender, fatherID, motherID, spouseID);
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
