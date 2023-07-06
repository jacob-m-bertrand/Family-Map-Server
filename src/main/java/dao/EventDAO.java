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
     * @param event                 Event object to insert into the Events table
     * @throws DataAccessException  thrown if there is an error with either the SQL or the database connection itself
     */
    public void insert(Event event) throws DataAccessException {
        String insertSql = "INSERT INTO Event VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            // Prepare the SQL statement, replacing all the placeholders with their value
            PreparedStatement insertStatement = conn.prepareStatement(insertSql);
            insertStatement.setString(1, event.getEventID());
            insertStatement.setString(2, event.getAssociatedUsername());
            insertStatement.setString(3, event.getPersonID());
            insertStatement.setFloat(4, event.getLatitude());
            insertStatement.setFloat(5, event.getLongitude());
            insertStatement.setString(6, event.getCountry());
            insertStatement.setString(7, event.getCity());
            insertStatement.setString(8, event.getEventType());
            insertStatement.setInt(9, event.getYear());

            // Execute the sql, and get the number of rows added
            int rowsAdded = insertStatement.executeUpdate();

            // If no rows were added, throw an exception
            if(rowsAdded == 0) throw new DataAccessException("No rows were added.");

            // Commit the changes and cleanup
            Database.commit();
            insertStatement.close();
        }
        catch(SQLException e) { // If a SQLException is encountered, wrap it in a DataAccessException for easier handling
            throw new DataAccessException(e.getMessage());
        }
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
        if(TableTools.exists("Event", "eventID", eventID)) throw new NotFoundException();

        String findSql = "SELECT * FROM Event WHERE eventID = ?";

        try {
            // Prepare the statement to be executed, filling in the placeholder in the sql.
            PreparedStatement findStatement = conn.prepareStatement(findSql);
            findStatement.setString(1, eventID);

            // Store the results of the query, then close
            ResultSet findResult = findStatement.executeQuery();
            Event newEvent = toObject(findResult);

            // Cleanup
            findStatement.close();

            return newEvent;
        }
        catch(SQLException e) { // Wrap any SQLExceptions which are thrown
            throw new DataAccessException(e.getMessage());
        }
    }


    /**
     * List all the events which have a matching value in their column
     * @param attribute             the attribute we're trying to match
     * @param value                 the desired value of the specified attribute
     * @return                      an ArrayList of Event objects which match the specifications
     * @throws DataAccessException  In the event that there is a SQLException
     * @throws NotFoundException    If the attribute or value are not found
     */
    public ArrayList<Event> list(String attribute, String value) throws DataAccessException, NotFoundException {
        // Ensure the attribute and value exist before moving on
        if(TableTools.exists("Event", attribute, value)) throw new NotFoundException();

        String sql = "SELECT * FROM Event WHERE " + attribute + " = ?";

        try {
            // Prepare the statement by inserting the value
            PreparedStatement listStatement = conn.prepareStatement(sql);
            listStatement.setString(1, value);

            // Store the results of the query
            ResultSet queryResults = listStatement.executeQuery();

            // Add resuts to the ArrayList
            ArrayList<Event> toReturn = new ArrayList<Event>();
            while (queryResults.next()) {
                toReturn.add(toObject(queryResults));
            }

            listStatement.close(); // Cleanup

            return toReturn;
        }
        catch(SQLException e) { // Wrap thrown SQLExceptions
            throw new DataAccessException(e.getMessage());
        }
    }


    /**
     * Clears the Event table
     * @throws DataAccessException if the table cannot be found
     */
    public void clear() throws DataAccessException {
        try {
            TableTools.clear("Event");
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    /**
     * Helper function which turns ResultSets into Event objects
     * @param result            The ResultSet generated from a query
     * @return                  An event with the found data from a row in the table
     * @throws SQLException     If the data doesn't parse correctly
     */
    private Event toObject(ResultSet result) throws DataAccessException {
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
