package service.requestvalidation;

import exception.InvalidRequestException;
import model.User;
import request.RegisterRequest;

/**
 * Validates {@link RegisterRequest}s.
 */
public class RegisterRequestValidator {
    /**
     * Validates a given RegisterRequest
     * @param request                   The request to validate.
     * @throws InvalidRequestException  If the request is invalid.
     */
    public static void validateRequest(RegisterRequest request) throws InvalidRequestException {
        // Pass the registration data into a new user which can be validated by ModelValidator
        User userToValidate = new User(request.getUsername(), request.getPassword(), request.getEmail(),
                request.getFirstName(), request.getLastName(), request.getGender());

        ModelValidator.validateUserObject(userToValidate);
    }
}
