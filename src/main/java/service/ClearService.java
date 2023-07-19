package service;

import dao.AuthtokenDAO;
import dao.EventDAO;
import dao.PersonDAO;
import dao.UserDAO;
import exception.DataAccessException;
import result.Result;

/**
 * Clears all the people, users, authtokens, and events from the table
 */
public class ClearService {
    /** Data Access Object which allows us to interface with the User table within the database. */
    private final UserDAO userDao;

    /** Data Access Object which allows us to interface with the Event table within the database. */
    private final EventDAO eventDao;

    /** Data Access Object which allows us to interface with the Person table within the database. */
    private final PersonDAO personDao;

    /** Data Access Object which allows us to interface with the Authtoken table within the database. */
    private final AuthtokenDAO authtokenDao;

    /** Constructs the ClearService, initializing the DAOs in the process. */
    public ClearService() {
        // Initialize DAOs
        userDao = new UserDAO();
        eventDao = new EventDAO();
        personDao = new PersonDAO();
        authtokenDao = new AuthtokenDAO();
    }

    /**
     * Processes the clearing. The clear API endpoint doesn't output any data, so a general {@link Result} object is
     * used, which only stores success and a message.
     */
    public Result clear() {
        try {
            // Run each table's clear function
            userDao.clear();
            eventDao.clear();
            authtokenDao.clear();
            personDao.clear();

            // If we reach this point, the clears were successful, so we can store a successful result
            return new Result(true, "Clear succeeded.");

        } catch (DataAccessException e) {
            // If an error is thrown, then store an unsuccessful result
            return new Result(false, "Error: Internal server error (" + e.getMessage() + ")");
        }

    }
}
