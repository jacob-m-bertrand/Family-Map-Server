package dao;
import model.Person;
import model.User;

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
    public void insert(Person person) {

    }

    /**
     * Returns a list of all people associated with a particular user
     * @param associatedUsername    The user for whom the lookup is performed
     * @return                      An ArrayList of all the people associateed with the username
     * @throws DataAccessException  In the case that associatedUsername is not found, or the table does not exist
     */
    public ArrayList<User> listByAssociatedUsername(String associatedUsername) throws DataAccessException {
        return null;
    }

    /**
     * Returns a specified event from the event table
     * @param personID               The id of the event
     * @return                      An Event object representing the event from the table
     * @throws DataAccessException  In the case that the event cannot be found or the table does not exist
     */
    public Person find(String personID) throws DataAccessException {
        return null;
    }

    /**
     * Clears the table
     * @throws DataAccessException in the case that the table does not exist
     */
    public void clear() throws DataAccessException {

    }
}
