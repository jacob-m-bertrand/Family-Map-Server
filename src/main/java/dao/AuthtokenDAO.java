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
        String sql = "INSERT INTO Authtoken VALUES (?, ?)";
        PreparedStatement insertStatement = null;

        try {
            insertStatement = conn.prepareStatement(sql);
            insertStatement.setString(1, authtoken.getUsername());
            insertStatement.setString(2, authtoken.getAuthtoken());

            int rowsAdded = insertStatement.executeUpdate();
            if(rowsAdded == 0) throw new DataAccessException("No rows were added.");
            insertStatement.close();
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Returns a specified event from the event table
     * @param username              The username correlated with the authtoken we want
     * @return                      An Event object representing the event from the table
     * @throws DataAccessException  In the case that the authtoken cannot be found or the table does not exist
     */
    public Authtoken find(String username) throws DataAccessException {
        String sql = "SELECT * FROM Authtoken WHERE username = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1,username);

            ResultSet findResult = statement.executeQuery();

            Authtoken toReturn = resultToObject(findResult);
            if (toReturn == null) throw new DataAccessException("Event not found!");

            return toReturn;
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private Authtoken resultToObject(ResultSet result) throws SQLException {
        if(!result.next()) return null;

        String username = result.getString(1);
        String authtoken = result.getString(2);

        return new Authtoken(username);
    }

    /**
     * Clears the table
     * @throws DataAccessException in the case that the table does not exist
     */
    public void clear() throws DataAccessException {
        try {
            TableTools.clear("Authtoken");
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }
}
