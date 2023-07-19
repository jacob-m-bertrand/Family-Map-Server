package dao;

import dao.tools.SQLTools;
import dao.tools.TableTools;
import exception.DataAccessException;
import exception.NotFoundException;
import model.Authtoken;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interfaces between the server and the Authtoken table.
 */
public class AuthtokenDAO {
    /**
     * Inserts an Authtoken into the Authtoken table.
     * @param authtoken Authtoken to insert.
     */
    public void insert(Authtoken authtoken) throws DataAccessException {
        // Create the sql statement from the provided authtoken
        String insertSql = String.format("INSERT INTO Authtoken VALUES ('%s', '%s')", authtoken.getAuthtoken(),
                authtoken.getUsername());

        // Use the SQLTools execution to run the insertion, and store the number of rows added.
        int rowsAdded = SQLTools.executeInsert(insertSql);

        // If no rows were added, throw an exception
        if (rowsAdded == 0) {
            throw new DataAccessException("No rows were added.");
        }

        // Commit the changes to the Database
        Database.commit();
    }

    /**
     * Returns the username associated with a specific authtoken.
     * @param authtoken             The provided authtoken.
     * @return                      The username associated with the provided authtoken.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    public String find(String authtoken) throws DataAccessException, NotFoundException {
        // Build the sql statement from the provided authtoken
        String findSql = String.format("SELECT username FROM Authtoken WHERE authtoken = '%s'", authtoken);

        // Store the results of the query
        ResultSet findResult = SQLTools.executeQuery(findSql);

        try {
            if (!findResult.next()) {
                throw new NotFoundException();
            }

            // Return the result of the find
            return findResult.getString("username");

        } catch (SQLException s) {
            throw new DataAccessException(s.getMessage());
        }
    }

    /**
     * Clears the Authtoken table.
     * @throws DataAccessException If there is an issue accessing the database or table.
     * @see TableTools TableTools.clear()
     */
    public void clear() throws DataAccessException {
        TableTools.clear("Authtoken");
    }
}
