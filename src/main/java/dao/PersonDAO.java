package dao;
import model.Event;
import model.Person;
import model.User;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;


public class PersonDAO {
    private final Connection conn;

    /**
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
        String sql = "INSERT INTO Event VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement insertStatement = conn.prepareStatement(sql);
            insertStatement.setString(1, person.getPersonID());
            insertStatement.setString(2, person.getAssociatedUsername());
            insertStatement.setString(3, person.getFirstName());
            insertStatement.setString(4, person.getLastName());
            insertStatement.setString(5, person.getGender());

            if(person.getFatherID() != null) insertStatement.setString(6, person.getFatherID());
            if(person.getMotherID() != null) insertStatement.setString(7, person.getMotherID());
            if(person.getSpouseID() != null) insertStatement.setString(8, person.getSpouseID());

            int rowsAdded = insertStatement.executeUpdate();

            if(rowsAdded == 0) throw new DataAccessException("Zero rows added.");

            insertStatement.close();
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    public Person find(String personID) throws DataAccessException {
        String sql = "SELECT * FROM Person WHERE personID = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, personID);

            ResultSet findResult = statement.executeQuery();

            Person toReturn = toObject(findResult);
            if (toReturn == null) throw new DataAccessException("Event not found!");

            return toReturn;
        }
        catch(Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private Person toObject(ResultSet result) throws SQLException {
        if(!result.next()) return null;

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


    public ArrayList<Person> list(String column, String value) throws DataAccessException {
        String sql = "SELECT * FROM Event WHERE ? = ?";
        ArrayList<Person> toReturn = new ArrayList<Person>();

        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, column);
            statement.setString(2, value);

            ResultSet queryResults = statement.executeQuery();

            while (queryResults.next()) {
                toReturn.add(toObject(queryResults));
            }

            return toReturn;
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Clears the table
     * @throws DataAccessException in the case that the table does not exist
     */
    public void clear() throws DataAccessException {
        try {
            TableTools.clear("Person");
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
