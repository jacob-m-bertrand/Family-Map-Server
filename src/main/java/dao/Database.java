package dao;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String url = "jdbc:sqlite:C:\\Users\\jacob\\OneDrive - Brigham Young University\\School\\Summer 2023\\C S 240\\FamilyMap SQL\\FMS.db";
    private static final Connection conn;

    static {
        try {
            conn = DriverManager.getConnection(Database.getURL());
            conn.setAutoCommit(false);
        }
        catch(SQLException e) {
            throw new RuntimeException("TableTools could not connect to the database!");
        }
    }

    public static Connection getConnection() { return conn; }

    public static void commit() throws DataAccessException {
        try {
            conn.commit();
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public static String getURL() { return url; }

    public static void rollback() throws DataAccessException {
        try {
            conn.rollback();
        }
        catch(SQLException e) {
            throw new DataAccessException("Database could not be rolled back.");
        }
    }
}
