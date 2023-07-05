package dao;

import model.*;

import java.sql.*;

public class AuthtokenDAO {
    private final Connection conn;

    /**
     * @param conn Server connection
     */
    public AuthtokenDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts an event into the Events table
     * @param authtoken Authtoken to insert into the Events table
     */
    public void insert(Authtoken authtoken) {

    }

    /**
     * Returns a specified event from the event table
     * @param username              The username correlated with the authtoken we want
     * @return                      An Event object representing the event from the table
     * @throws DataAccessException  In the case that the authtoken cannot be found or the table does not exist
     */
    public Authtoken getByUsername(String username) throws DataAccessException {
        return null;
    }

    /**
     * Clears the table
     * @throws DataAccessException in the case that the table does not exist
     */
    public void clear() throws DataAccessException {

    }
}
