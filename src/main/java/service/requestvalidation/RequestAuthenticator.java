package service.requestvalidation;

import dao.AuthtokenDAO;
import exception.DataAccessException;
import exception.NotFoundException;
import request.AuthenticatedRequest;

/**
 * Authenticates requests which contain an authtoken.
 */
public class RequestAuthenticator {
    /** The request to authenticate. */
    private final AuthenticatedRequest request;

    /** Data Access Object which is used to access the Authtoken table in the database. */
    private final AuthtokenDAO authtokenDao;

    /**
     * Constructs a new RequestAuthenticator using the provided request.
     * @param request The provided request.
     */
    public RequestAuthenticator(AuthenticatedRequest request) {
        this.request = request;

        authtokenDao = new AuthtokenDAO();
    }

    /**
     * Gets the username from the provided authtoken. This authenticates the user.
     * @return                      The username of the user, now authenticated.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    public String getAuthenticatedUsername() throws DataAccessException {
        try {
            // Find the authtoken in the table, and return the username if it is found.
            return authtokenDao.find(request.getAuthtoken());

        } catch (NotFoundException n) {
            // Thrown when the authtoken is not found. Return "" which will us later that the user is not authenticated.
            return "";
        }
    }

}
