package exception;

/**
 * Exception thrown when a service receives an invalid request.
 */
public class InvalidRequestException extends Exception {
    /** Message explaining why the request is invalid. */
    private final String message;

    /**
     * Constructs a new InvalidRequestException with the given message.
     * @param message The provided error message.
     */
    public InvalidRequestException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
