package dao.tools;

import dao.Database;
import exception.DataAccessException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A set of common tools for running SQL statements.
 */
public class SQLTools {
    /** The database connection */
    private static final Connection conn;

    static {
        // Set up the database connection
        conn = Database.getConnection();
    }

    /**
     * Executes a sql query, and returns the result.
     * @param sql                   SQL containing the query to execute.
     * @return                      A ResultSet with the results of the executed query.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    public static ResultSet executeQuery(String sql) throws DataAccessException {
        try {
            // Create the statement, run and return the results
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        }
        catch(SQLException e) {
            // Caused by malformed sql
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Executes a sql insert statement, and returns the number of rows added by the insert.
     * @param sql                   SQL containing the insert statement to execute.
     * @return                      An {@code int} with the number of rows added during the insert.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    public static int executeInsert(String sql) throws DataAccessException {
        try {
            // Create the statement, execute it, and return the results
            Statement statement = conn.createStatement();
            return statement.executeUpdate(sql);
        }
        catch(SQLException e) {
            // Caused by malformed SQL
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Executes SQL statements which do not return anything.
     * @param sql                   The SQL to execute.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    public static void executeStatement(String sql) throws DataAccessException {
        try {
            // Create and execute the SQL statement
            Statement statement = conn.createStatement();
            statement.execute(sql);
        }
        catch(SQLException e) {
            // Caused by malformed SQL
            throw new DataAccessException(e.getMessage());
        }
    }
}
