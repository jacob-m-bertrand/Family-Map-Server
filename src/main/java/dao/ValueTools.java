package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ValueTools {
    private static Connection conn;

    static {
        conn = Database.getConnection();
    }

    public static boolean exists(String tableName, String attribute, String value) throws DataAccessException {
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + attribute + " = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, value);
            ResultSet queryResults = statement.executeQuery();

            return queryResults.getInt(1) == 0;
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
