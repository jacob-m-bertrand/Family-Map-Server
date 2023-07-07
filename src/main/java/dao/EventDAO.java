package dao;
import model.Event;

import java.sql.*;
import java.util.ArrayList;

public class EventDAO {
    private final Connection conn;

    /** Constructs the EventDAO object
     * @param conn  connection to the database
     */
    public EventDAO(Connection conn) {
        this.conn = conn;
    }


    /**
     * Inserts an event into the Events table
     * @param  event                Event object to insert into the Events table
     * @throws DataAccessException  thrown if there is an error with either the SQL or the database connection itself
     */
    public void insert(Event event) throws DataAccessException {
        String insertSql = String.format("INSERT INTO Event VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                event.getEventID(), event.getAssociatedUsername(), event.getPersonID(), event.getLatitude(), event.getLongitude(),
                event.getCountry(), event.getCity(), event.getEventType(), event.getYear());

        int rowsAdded = SQLTools.executeInsert(insertSql);

        // If no rows were added, throw an exception
        if(rowsAdded == 0) throw new DataAccessException("No rows were added.");

        // Commit the changes and cleanup
        Database.commit();
    }


    /**
     * Returns a specified event from the event table
     * @param eventID               The id of the event
     * @return                      An Event object representing the event from the table
     * @throws DataAccessException  In the event that there is a sql error
     * @throws NotFoundException    If the specified event isn't found
     */
    public Event find(String eventID) throws DataAccessException, NotFoundException {
        // If the event doesn't exist in the table, throw an exception
        if(ValueTools.exists("Event", "eventID", eventID)) throw new NotFoundException();

        String findSql = String.format("SELECT * FROM Event WHERE eventID = '%s'", eventID);

        // Store the results of the query, then close
        ResultSet findResult = SQLTools.executeQuery(findSql);

        return createEventObject(findResult);
    }

    public ArrayList<Event> list(String attribute, String value) throws DataAccessException, NotFoundException {
        // Ensure the attribute and value exist before moving on
        if(ValueTools.exists("Event", attribute, value)) throw new NotFoundException();

        String sql = String.format("SELECT * FROM Event WHERE %s = '%s'", attribute, value);
        ResultSet results = SQLTools.executeQuery(sql);
        ArrayList<Event> toReturn = new ArrayList<Event>();

        try {
            while (results.next()) toReturn.add(createEventObject(results));
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

        return toReturn;
    }


    /**
     * Clears the Event table
     * @throws DataAccessException if the table cannot be found
     */
    public void clear() throws DataAccessException { TableTools.clear("Event"); }


    /**
     * Helper function which turns ResultSets into Event objects
     * @param result            The ResultSet generated from a query
     * @return                  An event with the found data from a row in the table
     * @throws SQLException     If the data doesn't parse correctly
     */
    private Event createEventObject(ResultSet result) throws DataAccessException {
        try {
            String eventID = result.getString(1);
            String associatedUsername = result.getString(2);
            String personID = result.getString(3);
            float latitude = result.getFloat(4);
            float longitude = result.getFloat(5);
            String country = result.getString(6);
            String city = result.getString(7);
            String eventType = result.getString(8);
            int year = result.getInt(9);

            return new Event(eventID, associatedUsername, personID, latitude, longitude, country, city, eventType, year);
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
