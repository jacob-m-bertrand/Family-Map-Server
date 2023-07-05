package dao;

import model.Person;
import model.User;

import java.sql.*;
import java.util.ArrayList;

public class UserDAO {
    private final Connection conn;

    /**
     * @param conn Server connection
     */
    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts an event into the Events table
     * @param user Event object to insert into the Events table
     */
    public void insert(User user) {

    }


    /**
     * Returns a specified user from the event table
     * @param username               The id of the event
     * @return                      An Event object representing the event from the table
     * @throws DataAccessException  In the case that the event cannot be found or the table does not exist
     */
    public User find(String username) throws DataAccessException {
        return null;
    }

    /**
     * Gets the user's person object
     * @param username              The username for whom we perform the lookup
     * @return                      A person object representing their person in the database
     * @throws DataAccessException  In the case that the event cannot be found or the table does not exist
     */
    public Person getPersonObject(String username) throws DataAccessException {
        return null;
    }

    /**
     * Clears the table
     * @throws DataAccessException in the case that the table does not exist
     */
    public void clear() throws DataAccessException {

    }
}
