package dao;


import dao.tools.SQLTools;
import dao.tools.TableTools;
import exception.DataAccessException;
import exception.NotFoundException;
import model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interfaces between the server and the Event table.
 */
public class EventDAO {
    /**
     * Inserts an event into the Events table.
     * @param  event                Event to insert.
     * @throws DataAccessException  If there is an issue accessing the database, or inserting into the Event table.
     */
    public void insert(Event event) throws DataAccessException {
        // Build sql statement using provided event information
        String insertSql = String.format("INSERT INTO Event VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                event.getEventID(), event.getAssociatedUsername(), event.getPersonID(), event.getLatitude(),
                event.getLongitude(), event.getCountry(), event.getCity(), event.getEventType(), event.getYear());

        // Use the SQLTools execution to run the insertion, and store the number of rows added
        int rowsAdded = SQLTools.executeInsert(insertSql);

        // If no rows were added, throw an exception
        if (rowsAdded == 0) {
            throw new DataAccessException("No rows were added.");
        }

        // Commit the changes
        Database.commit();
    }

    /**
     * Finds an event from the table.
     * @param eventID               The eventID of the requested event.
     * @return                      The requested {@link Event} object.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     * @throws NotFoundException    If there is no event with the provided eventID.
     */
    public Event find(String eventID) throws DataAccessException, NotFoundException {
        // Create the sql statement using the provided eventID
        String findSql = String.format("SELECT * FROM Event WHERE eventID = '%s'", eventID);

        // Store the results of the query, then close
        ResultSet findResult = SQLTools.executeQuery(findSql);

        try {
            if (!findResult.next()) {
                throw new NotFoundException();
            }
        } catch (SQLException s) {
            throw new DataAccessException(s.getMessage());
        }

        return createEventObject(findResult);
    }

    /**
     * Clears the Event table.
     * @throws DataAccessException If there is an issue accessing the database or table.
     */
    public void clear() throws DataAccessException {
        TableTools.clear("Event");
    }

    /**
     * Removes all events in the table associated with a certain user.
     * @param username              The username of the user.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    public void removeByUsername(String username) throws DataAccessException {
        // Build the sql statement then execute the statement
        String sql = String.format("DELETE FROM Event WHERE associatedUsername='%s'", username);
        SQLTools.executeStatement(sql);
    }

    /**
     * Lists all events associated with a certain user.
     * @param username              The username of the user for whom we are retrieving events.
     * @return                      An {@link ArrayList} of {@link Event} objects.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     * @throws NotFoundException    If there is no event with the provided associatedUsername.
     */
    public ArrayList<Event> listUserEvents(String username) throws DataAccessException, NotFoundException {
        // Build the sql statement, execute, and store the results
        String sql = String.format("SELECT * FROM Event WHERE associatedUsername = '%s'", username);
        ResultSet results = SQLTools.executeQuery(sql);

        // Initialize the ArrayList that will be returned
        ArrayList<Event> toReturn = new ArrayList<Event>();

        try {
            // Add all the results to the ArrayList
            while (results.next()) {
                toReturn.add(createEventObject(results));
            }

            // If there were no rows found, throw a not found exception
            if (toReturn.isEmpty()) {
                throw new NotFoundException();
            }

            return toReturn;

        } catch (SQLException e) {
            // Caused by a bad SQL statement
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Helper function which turns jdbc ResultSets into Event objects.
     * @param result                The ResultSet generated from a query.
     * @return                      An event with the data from a row in the table.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    private Event createEventObject(ResultSet result) throws DataAccessException {
        try {
            // Get all the details of the event from the result
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
        catch (SQLException e) {
            // Caused by a mismatch of requested and actual types
            throw new DataAccessException(e.getMessage());
        }
    }
}
