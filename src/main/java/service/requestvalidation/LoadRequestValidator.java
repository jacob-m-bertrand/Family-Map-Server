package service.requestvalidation;

import exception.InvalidRequestException;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;

/**
 * Validates {@link LoadRequest}s.
 */
public class LoadRequestValidator {
    /**
     * Validates the provided LoadRequest.
     * @param request                   The provided LoadRequest.
     * @throws InvalidRequestException  If the request is invalid.
     */
    public static void validateRequest(LoadRequest request) throws InvalidRequestException {
        // If any of the three provided ArrayLists is empty, the request is invalid.
        if (request.getUsers().isEmpty()) {
            throw new InvalidRequestException("Users list is empty.");
        }

        if (request.getPersons().isEmpty()) {
            throw new InvalidRequestException("Persons list is empty.");
        }

        if (request.getEvents().isEmpty()) {
            throw new InvalidRequestException("Events list is empty.");
        }

        // Verify that the objects within the Arrays are valid as well
        for(User u : request.getUsers()) {
            ModelValidator.validateUserObject(u);
        }

        for(Person p : request.getPersons()) {
            ModelValidator.validatePersonObject(p);
        }

        for(Event e : request.getEvents()) {
            ModelValidator.validateEventObject(e);
        }
    }
}
