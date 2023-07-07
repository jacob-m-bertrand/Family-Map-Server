package dao;

import model.Event;
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
    public void insert(User user) throws DataAccessException {
        String insertSql = String.format("INSERT INTO User VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s'')",
                user.getPersonID(), user.getPassword(), user.getEmail(), user.getFirstName(), user.getLastName(),
                user.getGender(), user.getPersonID());

        int rowsAdded = SQLTools.executeInsert(insertSql);

        // If no rows were added, throw an exception
        if(rowsAdded == 0) throw new DataAccessException("No rows were added.");

        // Commit the changes and cleanup
        Database.commit();
    }


    /**
     * Returns a specified user from the event table
     * @param username               The id of the event
     * @return                      An Event object representing the event from the table
     * @throws DataAccessException  In the case that the event cannot be found or the table does not exist
     */
    public User find(String username) throws DataAccessException, NotFoundException {
        // If the event doesn't exist in the table, throw an exception
        if(ValueTools.exists("User", "username", username)) throw new NotFoundException();

        String findSql = String.format("SELECT * FROM User WHERE eventID = '%s'", username);

        // Store the results of the query, then close
        ResultSet findResult = SQLTools.executeQuery(findSql);

        return createUserObject(findResult);
    }

    private User createUserObject(ResultSet result) throws DataAccessException {
        try {
            String username = result.getString(1);
            String password = result.getString(2);
            String email = result.getString(3);
            String firstName = result.getString(4);
            String lastName = result.getString(5);
            String gender = result.getString(6);
            String personID = result.getString(7);

            return new User(username, password, email, firstName, lastName, gender);
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Clears the table
     * @throws DataAccessException in the case that the table does not exist
     */
    public void clear() throws DataAccessException { TableTools.clear("User"); }
}
