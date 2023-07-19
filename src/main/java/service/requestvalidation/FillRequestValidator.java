package service.requestvalidation;

import exception.InvalidRequestException;
import request.FillRequest;

/**
 * Validates {@link FillRequest}s.
 */
public class FillRequestValidator {
    /**
     * Validates a given FillRequest.
     * @param request                   The request to validate.
     * @throws InvalidRequestException  If the request is invalid.
     */
    public static void validateRequest(FillRequest request) throws InvalidRequestException {
        // If the username has spaces or other illegal characters in it, throw an exception
        if(!request.getUsername().matches("[a-zA-Z0-9._]+")) {
            throw new InvalidRequestException("Username contains characters outside a-z, A-Z, 0-9, ., and _.");
        }

        /* If we get negative generations, or we get too many (after 7 starts to significantly impact performance),
           throw an exception. */
        else if(request.getGenerations() < 0 || request.getGenerations() > 7) {
            throw new InvalidRequestException("Number of generations is outside of range. Valid numbers are between " +
                    "0 and 6, inclusive.");
        }
    }

}
