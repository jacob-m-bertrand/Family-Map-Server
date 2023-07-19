package service;

import dao.PersonDAO;
import exception.DataAccessException;
import exception.NotFoundException;
import model.Person;
import request.PersonRequest;
import result.PersonResult;
import service.requestvalidation.RequestAuthenticator;

import java.util.Objects;

/**
 * Finds a person in the database.
 */
public class PersonService {
    /** The request which provides information for authenticating the user and finding the requested person. */
    private final PersonRequest request;

    /** Data Access Object for the Person table, used to find the requested person in the database. */
    private final PersonDAO personDao;

    /** Authenticator object which authenticates the request. */
    private final RequestAuthenticator authenticator;

    /**
     * Constructs the PersonService object with the request and intializes the DAO and authenticator.
     * @param request The request which provides information for authenticating the user and finding the requested person.
     */
    public PersonService(PersonRequest request) {
        this.request = request;

        // Initialize DAO, giving it the connection to the Database.
        personDao = new PersonDAO();

        // Initialize authenticator and hand it the request
        authenticator = new RequestAuthenticator(request);
    }

    /**
     * Processes the retrieval of the requested person.
     */
    public PersonResult findPerson() {
        try {
            /* Get the username from the authenticator, which validates the authtoken and finds the corresponding
               username in the database. Will return "" if the authtoken isn't in the database */
            String username = authenticator.getAuthenticatedUsername();

            if (!Objects.equals(username, "")) {
                // If the authtoken is valid, find the person in the database
                Person requestedPerson = personDao.find(request.getPersonID());

                /* If the person's associated username matches the username corresponding with the authtoken, store a
                   successful result. If not, store an unsuccessful one. */
                if (personBelongsToUser(requestedPerson, username)) {
                    return new PersonResult(true, requestedPerson);
                } else {
                    return new PersonResult(false, "Error: Requested person does not belong to this user.");
                }

            } else {
                // If the authtoken is invalid, store an unsuccessful result.
                return new PersonResult(false, "Error: Invalid auth token.");
            }
        } catch (DataAccessException d) {
            // Caused by an issue with the sql or in accessing the database
            return new PersonResult(false, "Error: Internal server error (" + d.getMessage() + ").");

        } catch (NotFoundException n) {
            // Caused by not finding the person in the database
            return new PersonResult(false, "Error: Invalid personID parameter.");
        }
    }

    /**
     * Verifies that the found person belongs to the user requesting the retrieval.
     * @param foundPerson The person for whom we are matching usernames.
     * @param username    The username of the requesting user.
     * @return            {@code true} if the person object belongs to the user, {@code false} otherwise.
     */
    private boolean personBelongsToUser(Person foundPerson, String username) {

        return Objects.equals(foundPerson.getAssociatedUsername(), username);
    }
}

