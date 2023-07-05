package dao;
import model.Event;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class EventDAO {
    private final Connection conn;

    /**
     * @param conn Server connection
     */
    public EventDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts an event into the Events table
     * @param event Event object to insert into the Events table
     */
    public void insert(Event event) throws SQLException, InsertFailedException {
        String insertEvent = "INSERT INTO Event VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement insertStatement = conn.prepareStatement(insertEvent);
        insertStatement.setString(1, event.getEventID());
        insertStatement.setString(2, event.getAssociatedUsername());
        insertStatement.setString(3, event.getPersonID());
        insertStatement.setFloat(4, event.getLatitude());
        insertStatement.setFloat(5, event.getLongitude());
        insertStatement.setString(6, event.getCountry());
        insertStatement.setString(7, event.getCity());
        insertStatement.setString(8, event.getEventType());
        insertStatement.setInt(9, event.getYear());

        int rowsAdded = insertStatement.executeUpdate();

        if(rowsAdded == 0) throw new InsertFailedException();

        insertStatement.close();
    }


    /**
     * Returns a specified event from the event table
     * @param eventID               The id of the event
     * @return                      An Event object representing the event from the table
     * @throws DataAccessException  In the case that the event cannot be found or the table does not exist
     */
    public Event find(String eventID) throws DataAccessException, SQLException {
        String sql = "SELECT * FROM Event WHERE eventID = ?";

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, eventID);

        ResultSet findResult = statement.executeQuery();

        Event toReturn = toObject(findResult);
        if(toReturn == null) throw new DataAccessException();

        return toReturn;
    }


    /**
     * List all the events which have a matching value in their column
     * @param column                column where the value is found (usually personID or associatedUsername)
     * @param value                 the value we want from the column
     * @return                      an ArrayList of Event objects who match the specifications
     * @throws SQLException         if the sql is invalid
     */
    public ArrayList<Event> list(String column, String value) throws SQLException {
        String sql = "SELECT * FROM Event WHERE ? = ?";
        ArrayList<Event> toReturn = new ArrayList<Event>();

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, column);
        statement.setString(2, value);

        ResultSet queryResults = statement.executeQuery();

        while(queryResults.next()) {
            toReturn.add(toObject(queryResults));
        }

        return toReturn;
    }

    private Event toObject(ResultSet result) throws SQLException {
        if(!result.next()) return null;

        String eventID = result.getString(1);
        String associatedUsername = result.getString(2);
        String personID = result.getString(3);
        float latitude = result.getFloat(4);
        float longitude = result.getFloat(5);
        String country = result.getString(6);
        String city = result.getString(7);
        String eventType = result.getString(8);
        int year = result.getInt(8);

        return new Event(eventID, associatedUsername, personID, latitude, longitude, country, city, eventType, year);
    }
}
