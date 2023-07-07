package dao;

import model.Event;

import java.sql.*;
import java.util.ArrayList;

public class TableTools {
    private static Connection conn;

    static {
        conn = Database.getConnection();
    }

    public static void clear(String tableName) throws DataAccessException {
        try {
            if(!tableExists(tableName)) throw new DataAccessException("Table doesnt exist!");

            StringBuilder sql = new StringBuilder("DELETE FROM " + tableName);

            Statement statement = conn.createStatement();
            statement.execute(sql.toString());

            Database.commit();

            statement.close();
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    public static boolean tableExists(String tableName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, tableName, null);

        return resultSet.next();
    }

    public static boolean tableIsEmpty(String tableName) throws DataAccessException {
        String sql = "SELECT COUNT(*) FROM " + tableName;

        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet queryResults = statement.executeQuery();

            return queryResults.getInt(1) == 0;
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }



}
