package service;

import dao.EventDAO;
import exception.DataAccessException;
import exception.NotFoundException;
import request.AuthenticatedRequest;
import result.ListEventsResult;
import service.requestvalidation.RequestAuthenticator;

import java.util.Objects;


/**
 * Retrieves a list of all the {@link model.Event} objects associated with the requesting user.
 */
public class ListEventsService {
    /** Data Access Object for the Event table, used to retrieve Event objects. */
    private final EventDAO eventDAO;

    /** Authenticator object which authenticates the request. */
    private final RequestAuthenticator authenticator;

    /**
     * Constructs the ListEventsService.
     * @param request The request which provides authentication information.
     */
    public ListEventsService(AuthenticatedRequest request) {
        // Initialize the DAO, and pass it a database connection
        eventDAO = new EventDAO();

        // Initialize the authenticator, and pass it the request
        authenticator = new RequestAuthenticator(request);
    }

    /**
     * Processes the listing of people associated with the requesting user.
     */
     public ListEventsResult listEvents() {
        try {
            /* Get the username of the requesting user via te authenticator. If the authtoken is not valid, it returns
            and stores "" */
            String username = authenticator.getAuthenticatedUsername();

            if (!Objects.equals(username, "")) {
                // If the authtoken is valid, store a successful result with the list of people
                return new ListEventsResult(true, eventDAO.listUserEvents(username));

            } else {
                // If the authtoken is not valid, store an unsuccessful result
                return new ListEventsResult(false, "Error: Invalid auth token.");
            }
        } catch (DataAccessException d) {
            // Caused by a sql error or a problem accessing the database
            return new ListEventsResult(false, "Internal server error (" + d.getMessage() + ").");

        } catch (NotFoundException n) {
            // Thrown if there are no people in the database associated with the user
            return new ListEventsResult(false, "Internal server error (user not found).");
        }
    }
}
