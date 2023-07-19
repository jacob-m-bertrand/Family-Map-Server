package service;

import dao.PersonDAO;
import exception.DataAccessException;
import exception.NotFoundException;
import request.AuthenticatedRequest;
import result.ListPersonsResult;
import service.requestvalidation.RequestAuthenticator;

import java.util.Objects;


/**
 * Retrieves a list of all the {@link model.Person} objects associated with the requesting user.
 */
public class ListPersonsService {
    /** Data Access Object for the Person table, used to retrieve Person objects. */
    private final PersonDAO personDAO;

    /** Authenticator object which authenticates the request. */
    private final RequestAuthenticator authenticator;

    /**
     * Constructs the ListPersonsService.
     * @param request The request which provides authentication information.
     */
    public ListPersonsService(AuthenticatedRequest request) {
        // Initialize the DAO, and pass it a database connection
        personDAO = new PersonDAO();

        // Initialize the authenticator, and pass it the request
        authenticator = new RequestAuthenticator(request);
    }

    /**
     * Processes the listing of people associated with the requesting user.
     */
    public ListPersonsResult listPersons() {
        try {
            /* Get the username of the requesting user via te authenticator. If the authtoken is not valid, it returns
            and stores "" */
            String username = authenticator.getAuthenticatedUsername();

            if (!Objects.equals(username, "")) {
                // If the authtoken is valid, store a successful result with the list of people
                return new ListPersonsResult(true, personDAO.listUserPersons(username));
            } else {
                // If the authtoken is not valid, store an unsuccessful result
                return new ListPersonsResult(false, "Error: Invalid auth token.");
            }
        } catch (DataAccessException d) {
            // Caused by a sql error or a problem accessing the database
            return new ListPersonsResult(false, "Internal server error (" + d.getMessage() + ").");

        } catch (NotFoundException n) {
            // Thrown if there are no people in the database associated with the user
            return new ListPersonsResult(false, "Internal server error (user not found).");
        }
    }
}
