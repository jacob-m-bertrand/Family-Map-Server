package dao;

import model.*;

import javax.xml.crypto.Data;
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
    public void insert(Authtoken authtoken) throws DataAccessException {
        String insertSql = String.format("INSERT INTO Authtoken VALUES ('%s', '%s')",
                authtoken.getUsername(), authtoken.getAuthtoken());

        int rowsAdded = SQLTools.executeInsert(insertSql);

        // If no rows were added, throw an exception
        if(rowsAdded == 0) throw new DataAccessException("No rows were added.");

        // Commit the changes and cleanup
        Database.commit();
    }

    /**
     * Returns a specified event from the event table
     * @param username              The username correlated with the authtoken we want
     * @return                      An Event object representing the event from the table
     * @throws DataAccessException  In the case that the authtoken cannot be found or the table does not exist
     */
    public Authtoken find(String username) throws DataAccessException, NotFoundException {
        // If the event doesn't exist in the table, throw an exception
        if(ValueTools.exists("Authtoken", "username", username)) throw new NotFoundException();

        String findSql = String.format("SELECT * FROM Authtoken WHERE username = '%s'", username);

        // Store the results of the query, then close
        ResultSet findResult = SQLTools.executeQuery(findSql);

        return createAuthtokenObject(findResult);
    }

    private Authtoken createAuthtokenObject(ResultSet result) throws DataAccessException {
        try {
            String username = result.getString(1);
            String authtoken = result.getString(2);

            return new Authtoken(username);
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Clears the table
     * @throws DataAccessException in the case that the table does not exist
     */
    public void clear() throws DataAccessException { TableTools.clear("Authtoken"); }
}
