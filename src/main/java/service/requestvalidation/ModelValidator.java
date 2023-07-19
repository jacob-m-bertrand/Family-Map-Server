package service.requestvalidation;

import exception.InvalidRequestException;
import model.Event;
import model.Person;
import model.User;

/**
 * Validates objects from the {@link model} package: {@link User}, {@link Event},and {@link Person}.
 */
public class ModelValidator {
    /**
     * Validates a {@link User} object.
     * @param user                      The {@link User} object to validate.
     * @throws InvalidRequestException  In the case that the object is invalid.
     */
    protected static void validateUserObject(User user) throws InvalidRequestException {
        // Store the fields of the user for easier access
        String username = user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String gender = user.getGender();
        String personID = user.getPersonID();

        // The username has to be one word, and only alphabetical characters, _, .
        if(username == null || !username.matches("[a-zA-Z0-9._]+")) {
            throw new InvalidRequestException("Username contains characters outside a-z, A-Z, 0-9, ., and _, or is empty.");
        }

        // The first name must contain only alphabetical characters
        if(firstName == null || !firstName.matches("[A-Za-z]+(?:\\\\s+[A-Za-z]+)*")) {
            throw new InvalidRequestException("First name contains non-alphabetical characters or is empty.");
        }

        // The last name must contain only alphabetical characters
        if(lastName == null || !lastName.matches("[A-Za-z]+(?:\\\\s+[A-Za-z]+)*")) {
            throw new InvalidRequestException("Last name contains non-alphabetical characters or is empty.");
        }

        // The password must not have spaces
        if(password == null || !password.matches("\\S{3,}")) {
            throw new InvalidRequestException("Password must be 3 or more characters, and cannot contain spaces.");
        }

        // The email address must match (generally) abc123._@abc.-.com
        if(email == null || !email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) {
            throw new InvalidRequestException("Invalid Email Address.");
        }

        // The gender must be one letter, 'm' or 'f'
        if(gender == null || !gender.matches("([mf])")) {
            throw new InvalidRequestException("Gender must either be 'm' or 'f'.");
        }

        // The personID must not be empty
        if (personID == null || personID.isEmpty()) {
            throw new InvalidRequestException("personID cannot be empty.");
        }
    }

    /**
     * Validates a {@link Person} object.
     * @param person                    The {@link Person} object to validate.
     * @throws InvalidRequestException  In the case that the object is invalid.
     */
    protected static void validatePersonObject(Person person) throws InvalidRequestException {
        // Store the fields of the person for easier acccess
        String personID = person.getPersonID();
        String associatedUsername = person.getAssociatedUsername();
        String firstName = person.getFirstName();
        String lastName = person.getLastName();
        String gender = person.getGender();

        // The personID and associatedUsername fields must not be null or empty
        if (personID == null || personID.isEmpty()) {
            throw new InvalidRequestException("personID cannot be empty.");
        }

        if (associatedUsername == null || associatedUsername.isEmpty()) {
            throw new InvalidRequestException("associatedUsername cannot be empty.");
        }

        // The first name must contain only alphabetical characters
        if(firstName == null || !firstName.matches("[A-Za-z]+(?:\\\\s+[A-Za-z]+)*")) {
            throw new InvalidRequestException("First name contains non-alphabetical characters or is empty.");
        }

        // The last name must contain only alphabetical characters
        if(lastName == null || !lastName.matches("[A-Za-z]+(?:\\\\s+[A-Za-z]+)*")) {
            throw new InvalidRequestException("Last name contains non-alphabetical characters or is empty.");
        }

        // The gender must be one letter, 'm' or 'f'
        if(gender == null || !gender.matches("([mf])")) {
            throw new InvalidRequestException("Gender must either be 'm' or 'f'.");
        }
    }

    /**
     * Validates {@link Event} objects.
     * @param event                      The {@link Event} object to validate.
     * @throws InvalidRequestException  In the case that the object is invalid.
     */
    protected static void validateEventObject(Event event) throws InvalidRequestException {
        // Store the event fields for easier access
        String eventID = event.getEventID();
        String associatedUsername = event.getAssociatedUsername();
        String personID = event.getPersonID();
        float latitude = event.getLatitude();
        float longitude = event.getLongitude();
        String country = event.getCountry();
        String city = event.getCity();
        String eventType = event.getEventType();

        /* While eventID, associatedUsername, personID, country, city, and event type dont have specific requirements
           they must be non-null and non-empty */
        if(eventID == null || eventID.isEmpty()) {
            throw new InvalidRequestException("eventID cannot be empty.");
        }

        if (associatedUsername == null || associatedUsername.isEmpty()) {
            throw new InvalidRequestException("associatedUsername cannot be empty.");
        }

        if (personID == null || personID.isEmpty()) {
            throw new InvalidRequestException("personID cannot be empty.");
        }

        // Latitude is defined as + or - 90 degrees
        if(latitude < -90 || latitude > 90) {
            throw new InvalidRequestException("Invalid latitude.");
        }

        // Longitude is defined as + or - 180 degrees
        if(longitude < -180 || longitude > 180) {
            throw new InvalidRequestException("Invalid longitude.");
        }

        if(country == null || country.isEmpty()) {
            throw new InvalidRequestException("Country cannot be empty.");
        }

        if(city == null || city.isEmpty()) {
            throw new InvalidRequestException("City cannot be empty.");
        }

        if(eventType == null || eventType.isEmpty()) {
            throw new InvalidRequestException("eventType cannot be empty.");
        }
    }

}
