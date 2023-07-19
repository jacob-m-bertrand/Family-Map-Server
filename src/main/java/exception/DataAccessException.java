package exception;

/**
 * A general exception for when there is an error accessing the data within the database.
 */
public class DataAccessException extends Exception {
    /** The error message. */
    private final String message;

    /**
     * Constructs a new DataAccessException with the provided message.
     * @param message The provided error message.
     */
    public DataAccessException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
