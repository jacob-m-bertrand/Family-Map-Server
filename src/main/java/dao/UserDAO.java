package dao;

import dao.tools.SQLTools;
import dao.tools.TableTools;
import exception.DataAccessException;
import exception.NotFoundException;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interfaces between the server and the User table.
 */
public class UserDAO {
    /**
     * Inserts a user into the User table.
     * @param user                  User object to insert into the User table.
     * @throws DataAccessException  If there is an issue accessing the database, or inserting into the Event table.
     */
    public void insert(User user) throws DataAccessException {
        // Build sql statement using provided user information
        String insertSql = String.format("INSERT INTO User VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstName(), user.getLastName(),
                user.getGender(), user.getPersonID());

        // Execute the insert and store the number of rows that were added
        int rowsAdded = SQLTools.executeInsert(insertSql);

        // If no rows were added, throw an exception
        if (rowsAdded == 0) {
            throw new DataAccessException("No rows were added.");
        }

        // Commit the changes and cleanup
        Database.commit();
    }


    /**
     * Finds a user from the table.
     * @param username              The username of the user to find.
     * @return                      The requested {@link User} object.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    public User find(String username) throws DataAccessException, NotFoundException {
        // Create the sql statement using the provided username
        String findSql = String.format("SELECT * FROM User WHERE username = '%s'", username);

        // Store the results of the query, then close
        ResultSet findResult = SQLTools.executeQuery(findSql);

        try {
            if (!findResult.next()) {
                throw new NotFoundException();
            }
        }
        catch(SQLException s) {
            throw new DataAccessException(s.getMessage());
        }

        return createUserObject(findResult);
    }

    /**
     * Clears the User table.
     * @throws DataAccessException If there is an issue accessing the database or table.
     */
    public void clear() throws DataAccessException {
        TableTools.clear("User");
    }

    /**
     * Helper function which turns jdbc ResultSets into a User object.
     * @param result                The ResultSet generated from a query.
     * @return                      A user with the data from a row in the table.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    private User createUserObject(ResultSet result) throws DataAccessException {
        try {
            String username = result.getString(1);
            String password = result.getString(2);
            String email = result.getString(3);
            String firstName = result.getString(4);
            String lastName = result.getString(5);
            String gender = result.getString(6);
            String personID = result.getString(7);

            return new User(username, password, email, firstName, lastName, gender, personID);

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
