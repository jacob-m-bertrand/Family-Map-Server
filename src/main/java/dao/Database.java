package dao;

import exception.DataAccessException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Stores all Database information and implements all Database functions. It is static, preventing opening more than one
 * connection at once.
 */
public class Database {
    /** The URL of the database. */
    private static final String url = "jdbc:sqlite:Family Map Server.db";

    /** The connection to the database. */
    private static final Connection conn;

    /* Set up the connection */
    static {
        try {
            conn = DriverManager.getConnection(url);

            // Disable Auto Commit for easier testing.
            conn.setAutoCommit(false);

        } catch(Exception e) {
            throw new RuntimeException("Could not connect to Database: " + e.getMessage());
        }
    }

    /**
     * Commits any pending changes.
     * @throws DataAccessException If there is an issue accessing the database or table.
     */
    public static void commit() throws DataAccessException {
        try {
            conn.commit();

        } catch (SQLException e) {
            // Caused by an error in the sql statement
            throw new DataAccessException(e.getMessage());
        }
    }

    public static Connection getConnection() {
        return conn;
    }
}
