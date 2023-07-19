package dao.tools;

import dao.Database;
import exception.DataAccessException;

import java.sql.*;

/**
 * A set of common tools used on the tables in the database.
 */
public class TableTools {
    /** The database connection */
    private static final Connection conn;

    static {
        // Set up the database connection
        conn = Database.getConnection();
    }

    /**
     * Clears the specified table.
     * @param tableName             The name of the table to clear.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    public static void clear(String tableName) throws DataAccessException {
        try {
            // If the table doesn't exist, throw an error
            if (!tableExists(tableName)) throw new DataAccessException("Table doesn't exist!");

            // Build then execute the sql statement
            String sql = "DELETE FROM " + tableName;
            SQLTools.executeStatement(sql);

            // Commit the changes
            Database.commit();
        } catch (SQLException e) {
            // Caused by malformed sql
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Checks if a table with name {@code tableName} exists.
     * @param tableName     The name of the table.
     * @return              {@code true} if the table exists, {@code false} if it doesn't.
     * @throws SQLException If there is an issue with the sql being run.
     */
    public static boolean tableExists(String tableName) throws SQLException {
        // Get the list of tables in the database, and find the table with the provided tableName
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, tableName, null);

        // If there is a table in the resultSet, will return true, if not, will return false
        return resultSet.next();
    }

    /**
     * Checks if a particular table is empty.
     * @param tableName             The name of the table to check.
     * @return                      {@code true} if the specified table is empty, {@code false} if it is not.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    public static boolean tableIsEmpty(String tableName) throws DataAccessException {
        // Build the sql statement which will get the number of rows on a table
        String sql = "SELECT COUNT(*) FROM " + tableName;

        try {
            // Execute the statement, store the results
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet queryResults = statement.executeQuery();

            return queryResults.getInt(1) == 0;

        }
        catch (SQLException e) {
            // Caused by malformed sql
            throw new DataAccessException(e.getMessage());
        }
    }



}
