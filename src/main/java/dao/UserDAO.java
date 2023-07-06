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
        String sql = "INSERT INTO User VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement insertStatement = null;

        try {
            insertStatement = conn.prepareStatement(sql);
            insertStatement.setString(1, user.getUsername());
            insertStatement.setString(2, user.getPassword());
            insertStatement.setString(3, user.getEmail());
            insertStatement.setString(4, user.getFirstName());
            insertStatement.setString(5, user.getLastName());
            insertStatement.setString(6, user.getPersonID());

            int rowsAdded = insertStatement.executeUpdate();
            if(rowsAdded == 0) throw new DataAccessException("No rows were added.");
            insertStatement.close();
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    /**
     * Returns a specified user from the event table
     * @param username               The id of the event
     * @return                      An Event object representing the event from the table
     * @throws DataAccessException  In the case that the event cannot be found or the table does not exist
     */
    public User find(String username) throws DataAccessException {
        String sql = "SELECT * FROM User WHERE username = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);

            ResultSet findResult = statement.executeQuery();

            User toReturn = toObject(findResult);
            if (toReturn == null) throw new DataAccessException("User not found!");

            return toReturn;
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private User toObject(ResultSet result) throws SQLException {
        if(!result.next()) return null;

        String username = result.getString(1);
        String password = result.getString(2);
        String email = result.getString(3);
        String firstName = result.getString(4);
        String lastName = result.getString(5);
        String gender = result.getString(6);
        String personID = result.getString(7);

        return new User(username, password, email, firstName, lastName, gender);
    }

    /**
     * Clears the table
     * @throws DataAccessException in the case that the table does not exist
     */
    public void clear() throws DataAccessException {
        try {
            TableTools.clear("User");
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
