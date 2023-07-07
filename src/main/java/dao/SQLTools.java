package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLTools {
    private static Connection conn;

    static {
        conn = Database.getConnection();
    }

    protected static ResultSet executeQuery(String sql) throws DataAccessException {
        try {
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    protected static int executeInsert(String sql) throws DataAccessException {
        try {
            Statement statement = conn.createStatement();
            return statement.executeUpdate(sql);
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
