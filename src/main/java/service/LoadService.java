package service;

import dao.EventDAO;
import dao.PersonDAO;
import dao.UserDAO;
import exception.DataAccessException;
import exception.InvalidRequestException;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import result.Result;
import service.requestvalidation.LoadRequestValidator;

/**
 * Clears the database, and loads in new data.
 */
public class LoadService {
    /** The request which contains the data needed to reload the database. */
    private final LoadRequest request;

    /** Data Access Object which allows modification of the User table. */
    private final UserDAO userDAO;

    /** Data Access Object which allows modification of the Person table. */
    private final PersonDAO personDAO;

    /** Data Access Object which allows modification of the Event table. */
    private final EventDAO eventDAO;

    /**
     * Constructs the LoadService object, taking in the request and initializing the DAOs.
     * @param request The received request.
     */
    public LoadService(LoadRequest request) {
        this.request = request;

        // Initialize the DAOs
        userDAO = new UserDAO();
        personDAO = new  PersonDAO();
        eventDAO = new EventDAO();
    }

    /**
     * Processes the load. Clears the database, then inserts the new data into the tables.
     */
    public Result load() {
        try {
            // Validate the request
            LoadRequestValidator.validateRequest(request);

            // Clear the database
            if (!clearDatabase().isSuccess()) {
                return new Result(false, "Error: Internal server error.");
            }

            // Insert new data
            int insertedUsers = insertUsers();
            int insertedPersons = insertPersons();
            int insertedEvents = insertEvents();

            // If no error is thrown, store a successful result
            String successMessage = String.format("Successfully added %d users, %d persons, and %d events to the database.",
                    insertedUsers, insertedPersons, insertedEvents);
            return new Result(true, successMessage);

        } catch (DataAccessException d) {
            // Caused by a SQL error or other problems accessing the database
            return new Result(false, "Error: Internal server error (" + d.getMessage() + ")");

        } catch (InvalidRequestException i) {
            // Caused by passing in an invalid request
            return new Result(false, "Error: Invalid request (" + i.getMessage() + ")");
        }
    }


    /**
     * Clears the database using the ClearService.
     * @return                      The result of the clear.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    private Result clearDatabase() throws DataAccessException {
        ClearService clearService = new ClearService();

        return clearService.clear();
    }

    /**
     * Inserts all the new users into the database.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    private int insertUsers() throws DataAccessException {
        int insertedUsers = 0;

        // Loop through all the users in the request, insert them into the database
        for (User u : request.getUsers()) {
            userDAO.insert(u);
            ++insertedUsers;
        }

        return insertedUsers;
    }

    /**
     * Inserts all the new people into the database.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    private int insertPersons() throws DataAccessException {
        int insertedPersons = 0;

        // Loop through all the people in the request, insert them into the database
        for (Person p : request.getPersons()) {
            personDAO.insert(p);
            ++insertedPersons;
        }

        return insertedPersons;
    }

    /**
     * Insert all the new events into the database.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    private int insertEvents() throws DataAccessException {
        int insertedEvents = 0;

        // Loop through all the events in the request, insert them into the database
        for (Event e : request.getEvents()) {
            eventDAO.insert(e);
            ++insertedEvents;
        }

        return insertedEvents;
    }
}
