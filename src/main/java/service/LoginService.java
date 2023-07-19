package service;


import dao.AuthtokenDAO;
import dao.UserDAO;
import exception.DataAccessException;
import exception.NotFoundException;
import model.Authtoken;
import model.User;
import request.LoginRequest;
import result.LoginResult;

/**
 * Verifies the user-provided login information, and generates an authtoken.
 */
public class LoginService {
    /** The request which stores the user-provided login information */
    private final LoginRequest request;

    /** The Data Access Object for the User table, which we will use to verify the request */
    private final UserDAO userDao;

    /** The Data Access Object for the Authtoken table, which we will use to insert authtokens. */
    private final AuthtokenDAO authtokenDAO;

    /**
     * Constructs the LoginService using the provided request, and initializes the DAO
     * @param request The request which stores the user-provided login information
     */
    public LoginService(LoginRequest request) {
        this.request = request;

        // Initialize the DAO, providing it with the database connection it needs
        userDao = new UserDAO();
        authtokenDAO = new AuthtokenDAO();
    }

    /**
     * Processes the login, ensuring that the username and password match, and stores the result.
     */
    public LoginResult login() {
        try {
            // Check if password is valid
            if (passwordIsValid()) {
                // Generate a new authtoken and insert it into the Authtoken table
                Authtoken userAuthtoken = new Authtoken(request.getUsername());

                // Insert the authtoken into the database for use in authenticating users
                authtokenDAO.insert(userAuthtoken);

                // If it is, store a successful result, with a new authtoken, the username, and personID
                return new LoginResult(true, userAuthtoken.getAuthtoken(), request.getUsername(), getPersonID());
            } else {
                // If not, store an unsuccessful result
                return new LoginResult(false, "Error: Invalid request (password is incorrect).");
            }
        } catch (DataAccessException e) {
            // If there's a DataAccessException, store an unsuccessful result
            return new LoginResult(false, "Error: Internal server error.");
        }
    }

    /**
     * Verifies that the password matches the username.
     * @return                      {@code true} if the username and password match, {@code false} if not.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    private boolean passwordIsValid() throws DataAccessException {
        try {
            // Get the user information from the database
            User userObject = userDao.find(request.getUsername());

            // Return whether the provided and database passwords are equal
            return request.getPassword().equals(userObject.getPassword());
        } catch (NotFoundException e) {
            // If the user isn't found the username and password automatically don't match
            return false;
        }
    }

    /**
     * Gets the person ID for the user being requested.
     * @return                      The person ID.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    private String getPersonID() throws DataAccessException {
        try {
            return userDao.find(request.getUsername()).getPersonID();
        } catch (NotFoundException e) {
            /* This function is run after we already know the user exists, so there's an internal server error if it
             * can't find it now
             */
            throw new DataAccessException(e.getMessage());
        }
    }
}
