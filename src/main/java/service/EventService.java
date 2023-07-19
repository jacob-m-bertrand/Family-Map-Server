package service;

import dao.EventDAO;
import exception.DataAccessException;
import exception.NotFoundException;
import model.Event;
import request.EventRequest;
import result.EventResult;
import service.requestvalidation.RequestAuthenticator;

import java.util.Objects;

/**
 * Finds an event in the database.
 */
public class EventService {
    /** The request which provides information for authenticating the user and finding the requested event. */
    private final EventRequest request;

    /** Data Access Object for the Event table, used to find the requested event in the database. */
    private final EventDAO eventDao;

    /** Authenticator object which authenticates the request. */
    private final RequestAuthenticator authenticator;

    /**
     * Constructs the EventService object with the request and initializes the DAO and authenticator.
     * @param request The request which provides information for authenticating the user and finding the requested event.
     */
    public EventService(EventRequest request) {
        this.request = request;

        // Initialize DAO, giving it the connection to the Database.
        eventDao = new EventDAO();

        // Initialize authenticator and hand it the request
        authenticator = new RequestAuthenticator(request);
    }

    /**
     * Processes the retrieval of the requested event.
     */
    public EventResult findEvent() {
        try {
            /* Get the username from the authenticator, which validates the authtoken and finds the corresponding
               username in the database. Will return "" if the authtoken isn't in the database */
            String username = authenticator.getAuthenticatedUsername();

            if (!Objects.equals(username, "")) {
                // If the authtoken is valid, find the event in the database
                Event requestedEvent = eventDao.find(request.getEventID());

                /* If the event's associated username matches the username corresponding with the authtoken, store a
                   successful result. If not, store an unsuccessful one. */
                if (eventBelongsToUser(requestedEvent, username)) {
                    return new EventResult(true, requestedEvent);
                } else {
                    return new EventResult(false, "Error: Requested event does not belong to this user.");
                }

            } else {
                // If the authtoken is invalid, store an unsuccessful result.
                return new EventResult(false, "Error: Invalid auth token.");
            }
        } catch (DataAccessException d) {
            // Caused by an issue with the sql or in accessing the database
            return new EventResult(false, "Error: Internal server error (" + d.getMessage() + ").");
        } catch (NotFoundException n) {
            // Caused by not finding the event in the database
            return new EventResult(false, "Error: Invalid eventID parameter.");
        }
    }

    /**
     * Verifies that the found event belongs to the user requesting the retrieval.
     * @param foundEvent            The event for whom we are matching usernames.
     * @param username              The username of the requesting user.
     * @return                      {@code true} if the event object belongs to the user, {@code false} otherwise.
     */
    private boolean eventBelongsToUser(Event foundEvent, String username) {
        return Objects.equals(foundEvent.getAssociatedUsername(), username);
    }
}
