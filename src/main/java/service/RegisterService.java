package service;

import dao.AuthtokenDAO;
import dao.PersonDAO;
import dao.UserDAO;
import exception.DataAccessException;
import exception.InvalidRequestException;
import exception.NotFoundException;
import model.Authtoken;
import model.Person;
import model.User;
import request.FillRequest;
import request.RegisterRequest;
import result.RegisterResult;
import result.Result;
import service.requestvalidation.RegisterRequestValidator;

import java.util.UUID;

/**
 * Registers a user into the database, generates family history data for them, then generates information used to
 * log the user in.
 */
public class RegisterService {
    /** The request which contains specific information about the registration. */
    private final RegisterRequest request;

    /** The Data Access Object for the User table, used to insert the new user into the table. */
    private final UserDAO userDao;

    /** The Data Access Object for the Person table, used to insert the user's person into the table. */
    private final PersonDAO personDAO;

    /** The Data Access Object for the Authtoken table, used to insert a new Authtoken into the table. */
    private final AuthtokenDAO authtokenDAO;

    /**
     * Constructs the RegisterService object with the request, and initializes the DAOs.
     * @param request The request which contains specific information about the regstration.
     */
    public RegisterService(RegisterRequest request) {
        this.request = request;

        // Initialize the DAOs
        userDao = new UserDAO();
        personDAO = new PersonDAO();
        authtokenDAO = new AuthtokenDAO();
    }

    /**
     * Processes the registration, setting the result depending on the success of the registration.
     */
    public RegisterResult register() {
        try {
            // Validate the request
            RegisterRequestValidator.validateRequest(request);
            if(userAlreadyExists()) return new RegisterResult(false, "Error: Username already taken.");

            // Register the user into the database, and store the result.
            RegisterResult registerResult = registerUser();

            // Generate family history data for the user, and store the result.
            Result fillResult = generateFamilyHistoryData();

            /*
             * If generating family history data was successful, the end result of the register is the first result we
             * stored. If not, the end result is the unsuccessful result of the fill service.
             */
            if (fillResult.isSuccess()) {
                return registerResult;
            } else {
                return new RegisterResult(false, fillResult.getMessage());
            }
        } catch (DataAccessException e) {
            // If we get a DataAccessException, then we will store the end result as an "Internal server error."
            return new RegisterResult(false, "Error: Internal server error (" + e.getMessage() + ")");
        } catch (InvalidRequestException i) {
            return new RegisterResult(false, "Error: Invalid request (" + i.getMessage() + ")");
        }

    }

    /**
     * Registers the requesting user to the database, and their accompanying person object.
     * @return                      A RegisterResult with the information required to log a user in.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    private RegisterResult registerUser() throws DataAccessException {
        // Create the user object based on request data
        User userToInsert = new User(request.getUsername(), request.getPassword(), request.getEmail(),
                request.getFirstName(), request.getLastName(), request.getGender());

        // Insert the user into the database
        userDao.insert(userToInsert);

        // Create a person object from the user
        Person userPerson = new Person(userToInsert);

        // Insert the user's person into the database
        personDAO.insert(userPerson);

        // Get information for constructing the result
        String authtoken = getAuthtoken();

        String username = request.getUsername();

        authtokenDAO.insert(new Authtoken(username, authtoken));

        String personID = userPerson.getPersonID();

        // Return the result of the registration
        return new RegisterResult(true, authtoken, username, personID);
    }

    /**
     * Generates family history data for a user, using the fill service.
     * @return The result of the family history generation.
     */
    private Result generateFamilyHistoryData() {
        FillRequest fillRequest = new FillRequest(request.getUsername(), 4);
        FillService fillService = new FillService(fillRequest);

        return fillService.generateFamilyHistoryData();
    }

    /**
     * Checks If the user already exists in the database.
     * @return                      {@code true} if the user is in the database, {@code false} if the user is not.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    private boolean userAlreadyExists() throws DataAccessException {
        try {
            userDao.find(request.getUsername());
            return true;
        } catch (NotFoundException n) {
            return false;
        }
    }

    /**
     * Generates a new authtoken for the user.
     * @return A string representing the new authtoken.
     */
    private String getAuthtoken() {
        return UUID.randomUUID().toString();
    }
}
