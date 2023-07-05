package dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class TableTools {
    private final Connection conn;

    /**
     * @param conn Server connection
     */
    public TableTools(Connection conn) {
        this.conn = conn;
    }

    public void clear(String tableName) throws SQLException, DataAccessException {
        if(!exists(tableName)) throw new DataAccessException();

        String sql = "DELETE FROM ?";

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, tableName);

        statement.executeUpdate();
        statement.close();

    }

    public boolean exists(String tableName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, tableName, null);

        return resultSet.next();
    }
}
